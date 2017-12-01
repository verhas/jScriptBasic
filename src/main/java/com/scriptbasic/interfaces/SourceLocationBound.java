package com.scriptbasic.interfaces;

/**
 * Any object that is some way bound to a location of the BASIC program. The
 * bounding relation is manifested via the name of the BASIC source file, the
 * line number and the character position on that line.
 *
 * @author Peter Verhas
 * June 15, 2012
 */
public interface SourceLocationBound {
    /**
     * Get the name of the file to which the object is bound to.
     *
     * @return the name of the file
     */
    String getFileName();

    /**
     * Get the line number to which the object is bound to.
     *
     * @return the line number in the file
     */
    int getLineNumber();

    /**
     * Get the position on the line to which the object is bound to.
     *
     * @return the position within the line
     */
    int getPosition();
}
