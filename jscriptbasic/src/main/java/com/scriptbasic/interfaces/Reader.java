package com.scriptbasic.interfaces;

/**
 * Source code reader.
 * 
 * @author Peter Verhas
 * 
 */
public interface Reader {

	public void set(java.io.Reader sourceReader);

	public void set(String sourceFileName);

	public void set(Reader parent);

	public Reader getParent();

	public String fileName();

	public int lineNumber();

	public int position();

	/**
	 * Readers should support lexical analyzers offering the possibility to push
	 * some characters back to the input stream, when a lexical analyzer can not
	 * decide its selection only consuming extra characters.
	 * <p>
	 * Some of the readers may limit the operation of this push back
	 * functionality not supporting tracking line numbers, position and file
	 * name when this method is used.
	 * <p>
	 * Lexical analyzers should push back the characters that were read from the reader
	 * the backward order as they were read. (Read last pushed back first.)
	 * <p>
	 * 
	 * @param ch the character to push back
	 */
	public void pushBack(Integer ch);

	/**
	 * Get the next character from the input stream.
	 * 
	 * @return
	 */
	public Integer get();
}
