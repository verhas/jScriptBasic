/**
 * 
 */
package com.scriptbasic.exceptions;

import com.scriptbasic.interfaces.LexicalElement;

/**
 * @author Peter Verhas
 * @date June 15, 2012
 *
 */
public abstract class AnalysisException extends Exception {
	
    public AnalysisException() {
		super();
	}

	public AnalysisException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnalysisException(String message) {
		super(message);
	}

	public AnalysisException(Throwable cause) {
		super(cause);
	}

	private String fileName;
    private int lineNumber;
    private int position;

    public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setLocation(final LexicalElement le) {
        this.fileName = le.fileName();
        this.lineNumber = le.lineNumber();
        this.position = le.position();
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getPosition() {
        return this.position;
    }
}
