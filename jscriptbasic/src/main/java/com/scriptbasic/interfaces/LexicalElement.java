package com.scriptbasic.interfaces;

/**
 * A single lexical element that was created by the LexicalAnalyer
 * 
 * @author Peter Verhas
 * 
 */
public interface LexicalElement {

	/**
	 * Get the original representation of the lexical element the way it was
	 * specified in the source code.
	 * 
	 * @return the lexical element as string
	 */
	public String get();

	/**
	 * Get the name of the file where the lexical element is.
	 * 
	 * @return
	 */
	public String fileName();

	/**
	 * Get the line number where the lexical element is in the file.
	 * 
	 * @return
	 */
	public int lineNumber();

	/**
	 * Get the type of the lexical element.
	 * 
	 * @return
	 */
	public int type();

	/**
	 * Get the string value of the lexical element. This method should be called
	 * only when the lexical element is a string literal. Otherwise the
	 * implementation will throw IllegalArgumentException();
	 * 
	 * @return
	 */
	public String stringValue() throws IllegalArgumentException;

	public Long longValue() throws IllegalArgumentException;

	public Double doubleValue() throws IllegalArgumentException;
}
