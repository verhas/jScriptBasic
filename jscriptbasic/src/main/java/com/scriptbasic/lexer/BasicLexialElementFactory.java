package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.Reader;

/**
 * Factory to create a new BasicLexicalElement initializing some fields from the
 * reader.
 * 
 * @author Peter Verhas
 * 
 */
public class BasicLexialElementFactory {

    /**
     * Create a new BasicLexicalElement and fill in the FileName, LineNumber and
     * the Position from the current position of the reader.
     * 
     * @param reader
     *            from where we are going to read the lexical element
     * 
     * @return a new and initialized lexical element object
     */
    public static BasicLexicalElement create(final Reader reader) {
        final BasicLexicalElement le = new BasicLexicalElement();
        le.setFileName(reader.fileName());
        le.setLineNumber(reader.lineNumber());
        le.setPosition(reader.position());
        return le;
    }

    public static BasicLexicalElement create(final Reader reader, final int type) {
        final BasicLexicalElement le = create(reader);
        le.setType(type);
        return le;
    }
}
