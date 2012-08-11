/**
 *
 */
package com.scriptbasic.exceptions;

import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.SourceLocationBound;

/**
 * @author Peter Verhas date June 15, 2012
 * 
 */
public abstract class GeneralAnalysisException extends AnalysisException
        implements SourceLocationBound {
    public GeneralAnalysisException() {
        super();
    }

    public GeneralAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralAnalysisException(String message) {
        super(message);
    }

    public GeneralAnalysisException(Throwable cause) {
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
        this.fileName = le.getFileName();
        this.lineNumber = le.getLineNumber();
        this.position = le.getPosition();
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
