package com.scriptbasic.exceptions;

import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.LexicalElement;

/**
 * @author Peter Verhas
 * date June 15, 2012
 */
public abstract class GeneralAnalysisException extends AnalysisException {

    private static final long serialVersionUID = 1L;
    private String fileName;
    private int lineNumber;
    private int position;

    public GeneralAnalysisException() {
        super();
    }

    public GeneralAnalysisException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public GeneralAnalysisException(final String message) {
        super(message);
    }
    public GeneralAnalysisException(final Throwable cause) {
        super(cause);
    }

    public void setLocation(final LexicalElement le) {
        this.fileName = le.getFileName();
        this.lineNumber = le.getLineNumber();
        this.position = le.getPosition();
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public void setLineNumber(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(final int position) {
        this.position = position;
    }
}
