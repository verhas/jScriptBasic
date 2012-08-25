package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.Reader;

/**
 * Factory to create a new BasicLexicalElement initializing some fields from the
 * reader.
 * 
 * @author Peter Verhas
 * 
 */
public final class BasicLexialElementFactory {

    private BasicLexialElementFactory(){}
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
        final BasicLexicalElement lexicalElement = new BasicLexicalElement();
        lexicalElement.setFileName(reader.getFileName());
        lexicalElement.setLineNumber(reader.getLineNumber());
        lexicalElement.setPosition(reader.getPosition());
        return lexicalElement;
    }

    public static BasicLexicalElement create(final Reader reader, final int type) {
        final BasicLexicalElement lexicalElement = create(reader);
        lexicalElement.setType(type);
        return lexicalElement;
    }
}
