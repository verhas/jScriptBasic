package com.scriptbasic.interfaces;

/**
 * Source code reader. This class is almost works as the usual
 * {@code java.io.Reader} and the implementation {@see GenericReader} actually
 * wraps the standard {@code java.io.Reader} class. The extra functionality is
 * keeping track of the file name, the line number and the position to help
 * error reporting of lexical and syntax analyzers and to allow character push
 * back when the lexical analyzer needs to push back some characters.
 * <p>
 * When it comes to reading the only way to get character from the stream is
 * calling the method {@code get()}.
 * 
 * @author Peter Verhas
 * 
 */
public interface Reader extends FactoryManaged, SourceLocationBound {

    void set(String sourceFileName);

    /**
     * Readers should support lexical analyzers offering the possibility to push
     * some characters back to the input stream, when a lexical analyzer can not
     * decide its selection only consuming extra characters.
     * <p>
     * Some of the readers may limit the operation of this push back
     * functionality not supporting tracking line numbers, position and file
     * name when this method is used.
     * <p>
     * Lexical analyzers should push back the characters that were read from the
     * reader the backward order as they were read. (Read last pushed back
     * first.)
     * <p>
     * Implementation should ignore {@code null} parameter.
     * 
     * @param character
     *            the character to push back
     */
    void pushBack(Integer character);

    /**
     * Get the next character from the input stream.
     * 
     * @return
     */
    Integer get();

    /**
     * Get the source provider that provided this reader.
     * 
     * @return the source provider object.
     */
    SourceProvider getSourceProvider();
}
