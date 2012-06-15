/**
 * 
 */
package com.scriptbasic.interfaces;

/**
 * 
 * Any object that is some way bound to a location of the BASIC program. The
 * bounding relation is manifested via the name of the BASIC source file, the
 * line number and the character position on that line.
 * 
 * @author Peter Verhas
 * @date June 15, 2012
 * 
 */
public interface SourceLocationBound {
    /**
     * Get the name of the file to which the object is bound to.
     * 
     * @return
     */
    String getFileName();

    /**
     * Get the line number to which the object is bound to.
     * 
     * @return
     */
    int getLineNumber();

    /**
     * Get the position on the line to which the object is bound to.
     * 
     * @return
     */
    int getPosition();
}
