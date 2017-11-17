package com.scriptbasic.lexer;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.exceptions.BasicLexicalException;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.readers.GenericHierarchicalReader;
import com.scriptbasic.utility.CharUtils;

import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class BasicLexicalAnalyzer implements LineOrientedLexicalAnalyzer {
    private static final Logger LOG = LoggerFactory.getLogger();
    private final Deque<LexicalElementAnalyzer> analyzerQueue = new LinkedList<LexicalElementAnalyzer>();
    private Reader reader;
    private Factory factory;
    private Deque<LexicalElement> lexicalElementQueue = new LinkedList<LexicalElement>();
    private Iterator<LexicalElement> lexicalElementQueueIterator = this.lexicalElementQueue.iterator();
    private LexicalElement peekElement = null;

    protected BasicLexicalAnalyzer() {
        LOG.debug("constructor created {}", this);
    }

    private static boolean stringIsIncludeOrImport(final String s) {
        return s.equalsIgnoreCase("INCLUDE") || s.equalsIgnoreCase("IMPORT");

    }

    /**
     * Checks that the line starts with the keyword INCLUDE or IMPORT. It does
     * work when one or both keywords are defined as keywords in the interpreter
     * and also when these are just identifiers in the language.
     *
     * @param le the lexical element to examine if it is INLCUDE or IMPORT word
     * @return {@code true} if it is include or import
     */
    private static boolean isIncludeOrImport(final LexicalElement le) {
        return (le.isSymbol() || le.isIdentifier())
                && stringIsIncludeOrImport(le.getLexeme());
    }

    @Override
    public void set(final Reader reader) {
        this.reader = reader;
        for (final LexicalElementAnalyzer lea : this.analyzerQueue) {
            lea.setReader(reader);
        }
    }

    @Override
    public void registerElementAnalyzer(final LexicalElementAnalyzer lea) {
        LOG.debug("lexical element analyzer {} was registered", lea);
        lea.setReader(reader);
        this.analyzerQueue.add(lea);
    }

    @Override
    public void resetLine() {
        lexicalElementQueueIterator = this.lexicalElementQueue.iterator();
    }

    private void emptyLexicalElementQueue() {
        this.lexicalElementQueue = new LinkedList<LexicalElement>();
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
            if (!lexicalElementQueueIterator.hasNext()) {
                readTheNextLine();
                resetLine();
            }
            if (!this.lexicalElementQueue.isEmpty()) {
                this.peekElement = lexicalElementQueueIterator.next();
            }
        }
        return this.peekElement;
    }

    private Integer skipWhiteSpaces(final Integer firstCharacter) {
        Integer characterToSkip = firstCharacter;
        while (characterToSkip != null
                && CharUtils.isWhitespace(characterToSkip)
                && !CharUtils.isNewLine(characterToSkip)) {
            characterToSkip = this.reader.get();
        }
        return characterToSkip;
    }

    private void readTheNextLine() throws AnalysisException {
        Boolean lineEndFound = false;
        emptyLexicalElementQueue();
        Integer character;
        for (character = this.reader.get(); character != null && !lineEndFound; character = this.reader
                .get()) {
            LexicalElement le = null;
            character = skipWhiteSpaces(character);
            lineEndFound = CharUtils.isNewLine(character);
            if (character != null) {
                this.reader.pushBack(character);
                boolean analyzed = false;
                for (final LexicalElementAnalyzer lea : this.analyzerQueue) {
                    le = lea.read();
                    if (le != null) {
                        analyzed = true;
                        LOG.debug("{} could analyze the characters", lea);
                        LOG.debug("the result is: {}", le.toString());
                        this.lexicalElementQueue.add(le);
                        break;
                    }
                }
                if (!analyzed) {
                    LOG.error("None of the lexical analyzers could analyze the line");
                    throw new BasicInterpreterInternalError(
                            "no lexical element analyzer could analyze the input");
                }
            }
        }
        this.reader.pushBack(character);
        if (this.reader instanceof HierarchicalReader) {
            processSourceInclude();
        }
    }

    private void processSourceInclude() throws AnalysisException {
        resetLine();
        final GenericHierarchicalReader hreader = (GenericHierarchicalReader) this.reader;
        if (!this.lexicalElementQueue.isEmpty()) {
            LexicalElement lexicalElement = lexicalElementQueueIterator
                    .next();
            if (isIncludeOrImport(lexicalElement)) {
                lexicalElement = lexicalElementQueueIterator.next();
                if (lexicalElement.isString()) {
                    LexicalElement newLine = lexicalElementQueueIterator.hasNext() ? lexicalElementQueueIterator
                            .next() : null;
                    if (newLine != null && !newLine.isLineTerminator()) {
                        LOG.error("There are extra characters on the line after the include file name string");
                        throw new GenericSyntaxException(
                                "There are extra chars at the end of the INCLUDE statement");
                    }
                    final SourceProvider sp = hreader.getSourceProvider();
                    Reader childReader = null;
                    try {
                        childReader = sp.get(lexicalElement.stringValue(),
                                hreader.getFileName());
                    } catch (final IllegalArgumentException e) {
                        LOG.error("", e);
                    } catch (final IOException e) {
                        throw new BasicLexicalException(
                                "Can not open included file '"
                                        + lexicalElement.stringValue() + "'", e);
                    }
                    hreader.include(childReader);
                    emptyLexicalElementQueue();
                    readTheNextLine();
                    resetLine();
                } else {
                    LOG.error("This is not a string following the keyword INCLUDE");
                    throw new GenericSyntaxException(
                            "String has to be used after import or include.");
                }
            }
        }
    }
}
