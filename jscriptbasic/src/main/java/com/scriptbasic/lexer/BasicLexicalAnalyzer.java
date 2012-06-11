package com.scriptbasic.lexer;

import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.HierarchicalReader;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalElementAnalyzer;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.LineOrientedLexicalAnalyzer;
import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.interfaces.SourceProvider;
import com.scriptbasic.readers.GenericHierarchicalReader;
import com.scriptbasic.utility.CharUtils;

public class BasicLexicalAnalyzer implements LineOrientedLexicalAnalyzer {
    private Logger log = LoggerFactory.getLogger(BasicLexicalAnalyzer.class);
    private Reader reader;
    protected Factory factory;

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    protected BasicLexicalAnalyzer() {
        log.debug("constructor created " + this);
    }

    private final Deque<LexicalElementAnalyzer> analyzerQueue = new LinkedList<LexicalElementAnalyzer>();

    @Override
    public void set(final Reader reader) {
        log.debug("reader was set to " + reader);
        if(this.analyzerQueue == null ){
            log.error("analyzerQueue is still empty when setting reader "+reader);
            log.error("all analyzers have to be registered be setting the reader");
            throw new BasicInterpreterInternalError("LexicalElementAnalyzer queue was not intialized");
        }
        this.reader = reader;
        for (final LexicalElementAnalyzer lea : this.analyzerQueue) {
            lea.setReader(reader);
        }
    }

    @Override
    public void registerElementAnalyzer(final LexicalElementAnalyzer lea) {
        log.debug("lexical element analyzer " + lea + " was registered");
        this.analyzerQueue.add(lea);
    }

    Deque<LexicalElement> lexicalElementQueue = new LinkedList<LexicalElement>();
    Iterator<LexicalElement> lexicalElementQueueIterator = this.lexicalElementQueue
            .iterator();

    @Override
    public void resetLine() {
        this.lexicalElementQueueIterator = this.lexicalElementQueue.iterator();
    }

    private void emptyLexicalElementQueue() {
        this.lexicalElementQueue = new LinkedList<LexicalElement>();
    }

    private LexicalElement peekElement = null;

    @Override
    public LexicalElement get() throws LexicalException {
        LexicalElement le = null;
        le = peek();
        this.peekElement = null;
        return le;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LexicalElement peek() throws LexicalException {
        if (this.peekElement == null) {
            if (!this.lexicalElementQueueIterator.hasNext()) {
                readTheNextLine();
                resetLine();
            }
            if (!this.lexicalElementQueue.isEmpty()) {
                this.peekElement = this.lexicalElementQueueIterator.next();
            }
        }
        return this.peekElement;
    }

    private Integer skipWhiteSpaces(Integer ch) {
        while (ch != null && Character.isWhitespace(ch)
                && !CharUtils.isNewLine(ch)) {
            ch = this.reader.get();
        }
        return ch;
    }

    private boolean stringIsIncludeOrImport(final String s) {
        return s.equalsIgnoreCase("INCLUDE") || s.equalsIgnoreCase("IMPORT");

    }

    private boolean isIncludeOrImport(final LexicalElement le) {
        return (le.isSymbol() || le.isIdentifier())
                && stringIsIncludeOrImport(le.get());
    }

    private void readTheNextLine() throws LexicalException {
        Boolean lineEndFound = false;
        emptyLexicalElementQueue();
        Integer ch;
        for (ch = this.reader.get(); ch != null && !lineEndFound; ch = this.reader
                .get()) {
            LexicalElement le = null;
            ch = skipWhiteSpaces(ch);
            lineEndFound = CharUtils.isNewLine(ch);
            if (ch != null) {
                this.reader.pushBack(ch);
                for (final LexicalElementAnalyzer lea : this.analyzerQueue) {
                    le = lea.read();
                    if (le != null) {
                        this.lexicalElementQueue.add(le);
                        break;
                    }
                }
            }
        }
        this.reader.pushBack(ch);
        if (this.reader instanceof HierarchicalReader) {
            processSourceInclude();
        }
    }

    private void processSourceInclude() throws LexicalException {
        resetLine();
        final GenericHierarchicalReader hreader = (GenericHierarchicalReader) this.reader;
        if (!this.lexicalElementQueue.isEmpty()) {
            LexicalElement le = this.lexicalElementQueueIterator.next();
            if (isIncludeOrImport(le)) {
                le = this.lexicalElementQueueIterator.next();
                if (le.isString()) {
                    // TODO check that there are no extra chars on the line
                    final SourceProvider sp = hreader.getSourceProvider();
                    Reader childReader = null;
                    try {
                        childReader = sp.get(le.stringValue(),
                                hreader.fileName());
                    } catch (final IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (final IOException e) {
                        throw new BasicLexicalException(
                                "Can not open included file '"
                                        + le.stringValue() + "'", e);
                    }
                    hreader.include(childReader);
                    emptyLexicalElementQueue();
                    readTheNextLine();
                    resetLine();
                } else {
                    // TODO throw error that it is not string after the
                    // INCLUDE
                }
            }
        }
    }
}
