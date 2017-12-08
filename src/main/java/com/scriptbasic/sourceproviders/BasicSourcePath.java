package com.scriptbasic.sourceproviders;

import com.scriptbasic.readers.SourcePath;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple SourcePath implementation. Maintains the path elements in a linked list.
 *
 * @author Peter Verhas
 */
public class BasicSourcePath implements SourcePath {
    private final List<String> sourcePath = new LinkedList<>();

    @Override
    public void add(final String path) {
        this.sourcePath.add(path);
    }

    @Override
    public Iterator<String> iterator() {
        return this.sourcePath.iterator();
    }
}
