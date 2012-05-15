package com.scriptbasic.interfaces;

/**
 * A generic lexical analyzer that reads the characters from a reader and returns the
 * LexicalElements one after the other.
 * <p>
 * The actual implementation specifies what lexical elements that identifies and returns.
 * @author Peter Verhas
 *
 */
public interface LexicalAnalyzer {
	/**
	 * Set the reader from where the lexical analyzer has to read the input.
	 * 
	 * @param reader
	 */
	public void set(Reader reader);
	/**
	 * Get the next lexical element from the input stream. If there are no more lexical elements
	 * then return {@code null}
	 * @return
	 */
	public LexicalElement get();
}
