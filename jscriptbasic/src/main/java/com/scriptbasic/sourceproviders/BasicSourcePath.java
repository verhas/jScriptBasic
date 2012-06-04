package com.scriptbasic.sourceproviders;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import com.scriptbasic.interfaces.SourcePath;

/**
 * A very simple SourcePath implementation.
 * 
 * @author Peter Verhas
 * 
 */
public class BasicSourcePath implements SourcePath {
    private Deque<String> sourcePath = new LinkedList<String>();

    @Override
    public void setSourcePath(final Deque<String> sourcePath) {
        this.sourcePath = sourcePath;
    }

    @Override
    public void add(final String path) {
        sourcePath.add(path);
    }

    @Override
    public Iterator<String> iterator() {
        return sourcePath.iterator();
    }
}
