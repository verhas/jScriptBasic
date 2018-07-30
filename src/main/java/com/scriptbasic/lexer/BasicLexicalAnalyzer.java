package com.scriptbasic.lexer;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.exceptions.BasicLexicalException;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.readers.HierarchicalSourceReader;
import com.scriptbasic.readers.SourceProvider;
import com.scriptbasic.readers.SourceReader;
import com.scriptbasic.utility.CharUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BasicLexicalAnalyzer implements LineOrientedLexicalAnalyzer {
    private static final Logger LOG = LoggerFactory.getLogger();
    private final List<LexicalElementAnalyzer> analyzers = new LinkedList<>();
    private final SourceReader reader;
    private List<LexicalElement> allElements = new LinkedList<>();
    private Iterator<LexicalElement> elements = this.allElements.iterator();
    private LexicalElement peekElement = null;

    public BasicLexicalAnalyzer(final SourceReader reader) {
        this.reader = reader;
        LOG.debug("constructor created {}", this);
    }

    private static boolean stringIsIncludeOrImport(final String s) {
        return s.equalsIgnoreCase("INCLUDE");

    }

    /**
     * Checks that the line starts with the keyword INCLUDE or IMPORT. It does
     * work when one or both keywords are defined as keywords in the interpreter
     * and also when these are just identifiers in the language.
     *
     * @param lexicalElement the lexical element to examine if it is INLCUDE or IMPORT word
     * @return {@code true} if it is include or import
     */
    private static boolean isIncludeOrImport(final LexicalElement lexicalElement) {
        return (lexicalElement.isSymbol() || lexicalElement.isIdentifier())
                && stringIsIncludeOrImport(lexicalElement.getLexeme());
    }

    @Override
    public void registerElementAnalyzer(final LexicalElementAnalyzer lea) {
        LOG.debug("lexical element analyzer {} was registered", lea);
        this.analyzers.add(lea);
    }

    @Override
    public void resetLine() {
        elements = this.allElements.iterator();
        peekElement = null;
    }

    private void emptyLexicalElementQueue() {
        this.allElements = new LinkedList<>();
    }

    @Override
    public LexicalElement get() throws AnalysisException {
        LexicalElement le = null;
        le = peek();
        this.peekElement = null;
        return le;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LexicalElement peek() throws AnalysisException {
        if (this.peekElement == null) {
            if (!elements.hasNext()) {
                Integer ch = reader.get();
                if (ch == null) {
                    // do not read beyond the last line
                    return null;
                }
                reader.unget(ch);
                readTheNextLine();
                resetLine();
            }
            if (!this.allElements.isEmpty()) {
                this.peekElement = elements.next();
            }
        }
        return this.peekElement;
    }

    private Integer getFirstNonWhitespaceCharacter(final SourceReader reader, final Integer firstCharacter) {
        Integer characterToSkip = firstCharacter;
        while (characterToSkip != null
                && CharUtils.isWhitespace(characterToSkip)
                && !CharUtils.isNewLine(characterToSkip)) {
            characterToSkip = reader.get();
        }
        return characterToSkip;
    }

    private void readTheNextLine() throws AnalysisException {
        boolean lineEndFound = false;
        emptyLexicalElementQueue();
        Integer ch;
        for (ch = reader.get(); ch != null && !lineEndFound; ch = reader.get()) {
            ch = getFirstNonWhitespaceCharacter(reader, ch);
            lineEndFound = CharUtils.isNewLine(ch);
            if (ch != null) {
                reader.unget(ch);
                boolean analyzed = false;
                for (final LexicalElementAnalyzer analyzer : analyzers) {
                    final LexicalElement element = analyzer.read();
                    if (element != null) {
                        analyzed = true;
                        LOG.debug("{} could analyze the characters", analyzer);
                        LOG.debug("the result is: {}", element.toString());
                        this.allElements.add(element);
                        break;
                    }
                }
                if (!analyzed) {
                    LOG.error("None of the lexical analyzers could analyze the line");
                    throw new BasicInterpreterInternalError("no lexical element analyzer could analyze the input");
                }
            }
        }
        reader.unget(ch);
        processSourceInclude();
    }

    private void processSourceInclude() throws AnalysisException {
        resetLine();
        if (!this.allElements.isEmpty()) {
            final LexicalElement statement = elements.next();
            if (isIncludeOrImport(statement)) {
                final LexicalElement lexicalElement = elements.next();
                assertIncludeFileIsSpecifiedAsString(lexicalElement);
                assertThereAreNoExtraCharactersAtTheEndOfTheLine();
                final SourceProvider sourceProvider = reader.getSourceProvider();
                SourceReader childReader = null;
                try {
                    childReader = sourceProvider.get(lexicalElement.stringValue(), reader.getFileName());
                } catch (final IllegalArgumentException e) {
                    LOG.error("", e);
                } catch (final IOException e) {
                    throw new BasicLexicalException(
                            "Can not open included file '"
                                    + lexicalElement.stringValue() + "'", e);
                }
                if (reader instanceof HierarchicalSourceReader) {
                    ((HierarchicalSourceReader) reader).include(childReader);
                } else {
                    LOG.error("Cannot include or import with normal reader.");
                    throw new BasicSyntaxException("INCLUDE is not allowed in this environment.");
                }
                emptyLexicalElementQueue();
                readTheNextLine();
                resetLine();
            }
        }
    }

    private void assertThereAreNoExtraCharactersAtTheEndOfTheLine() throws BasicSyntaxException {
        final LexicalElement newLine = elements.hasNext() ?
                elements.next() : null;
        if (newLine != null && !newLine.isLineTerminator()) {
            LOG.error("There are extra characters on the line after the include file name string");
            throw new BasicSyntaxException(
                    "There are extra chars at the end of the INCLUDE statement");
        }
    }

    private void assertIncludeFileIsSpecifiedAsString(final LexicalElement lexicalElement) throws BasicSyntaxException {
        if (!lexicalElement.isString()) {
            LOG.error("This is not a string following the keyword INCLUDE");
            throw new BasicSyntaxException("String has to be used after import or include.");
        }
    }
}
