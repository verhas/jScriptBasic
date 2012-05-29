package com.scriptbasic.lexer;

import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

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
    private Reader reader;

    private Deque<LexicalElementAnalyzer> analyzerQueue = new LinkedList<LexicalElementAnalyzer>();

    public void set(Reader reader) {
        this.reader = reader;
        for (LexicalElementAnalyzer lea : analyzerQueue) {
            lea.setReader(reader);
        }
    }

    @Override
    public void registerElementAnalyzer(LexicalElementAnalyzer lea) {
        analyzerQueue.add(lea);
    }

    Deque<LexicalElement> lexicalElementQueue = new LinkedList<LexicalElement>();
    Iterator<LexicalElement> lexicalElementQueueIterator = lexicalElementQueue
            .iterator();

    @Override
    public void resetLine() {
        lexicalElementQueueIterator = lexicalElementQueue.iterator();
    }

    private void emptyLexicalElementQueue() {
        lexicalElementQueue = new LinkedList<LexicalElement>();
    }

    private LexicalElement peekElement = null;

    public LexicalElement get() throws LexicalException {
        LexicalElement le = null;
        le = peek();
        peekElement = null;
        return le;
    }

    /**
     * {@inheritDoc}
     */
    public LexicalElement peek() throws LexicalException {
        if (peekElement == null) {
            if (!lexicalElementQueueIterator.hasNext()) {
                readTheNextLine();
                resetLine();
            }
            if (!lexicalElementQueue.isEmpty()) {
                peekElement = lexicalElementQueueIterator.next();
            }
        }
        return peekElement;
    }

    private Integer skipWhiteSpaces(Integer ch) {
        while (ch != null && Character.isWhitespace(ch)
                && !CharUtils.isNewLine(ch)) {
            ch = reader.get();
        }
        return ch;
    }

    private boolean stringIsIncludeOrImport(String s) {
        return s.equalsIgnoreCase("INCLUDE") || s.equalsIgnoreCase("IMPORT");

    }

    private boolean isIncludeOrImport(LexicalElement le) {
        return (le.isSymbol() || le.isIdentifier())
                && stringIsIncludeOrImport(le.get());
    }

    private void readTheNextLine() throws LexicalException {
        Boolean lineEndFound = false;
        emptyLexicalElementQueue();
        Integer ch;
        for (ch = reader.get(); ch != null && !lineEndFound; ch = reader.get()) {
            LexicalElement le = null;
            ch = skipWhiteSpaces(ch);
            lineEndFound = CharUtils.isNewLine(ch);
            if (ch != null) {
                reader.pushBack(ch);
                for (LexicalElementAnalyzer lea : analyzerQueue) {
                    le = lea.read();
                    if (le != null) {
                        lexicalElementQueue.add(le);
                        break;
                    }
                }
            }
        }
        reader.pushBack(ch);
        if (reader instanceof HierarchicalReader) {
            processSourceInclude();
        }
    }

    private void processSourceInclude() throws LexicalException {
        resetLine();
        GenericHierarchicalReader hreader = (GenericHierarchicalReader) reader;
        if (!lexicalElementQueue.isEmpty()) {
            LexicalElement le = lexicalElementQueueIterator.next();
            if (isIncludeOrImport(le)) {
                le = lexicalElementQueueIterator.next();
                if (le.isString()) {
                    // TODO check that there are no extra chars on the line
                    SourceProvider sp = hreader.getSourceProvider();
                    Reader childReader = null;
                    try {
                        childReader = sp.get(le.stringValue(),
                                hreader.fileName());
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
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
