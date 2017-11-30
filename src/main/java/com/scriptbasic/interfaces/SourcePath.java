package com.scriptbasic.interfaces;

import java.util.Deque;

/**
 * Source path is similar to Java class path. It contains source locations
 * (directories usually) where the source files are.
 * <p>
 * The source providers depending on SourcePath will use some implementation of
 * this interface to keep track the SourcePath elements and trying to locate the
 * source files.
 * 
 * @author Peter Verhas
 * 
 */
public interface SourcePath extends Iterable<String> {

    /**
     * Add an element to the source path, to the end of the list.
     * 
     * @param path
     */
    void add(String path);
}
