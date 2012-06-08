package com.scriptbasic.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TextFileResource {
    ArrayList<String> lines = null;

    public TextFileResource(final Class<? extends Object> loadingClass,
            final String fileName) throws IOException {
        final InputStream is = loadingClass.getResourceAsStream(fileName);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                is));
        String line = reader.readLine();
        this.lines = new ArrayList<String>();
        while (line != null) {
            this.lines.add(line);
            line = reader.readLine();
        }
    }

    public TextFileResource stripSpaces() {
        for (String line : this.lines) {
            line = line.replaceAll("\\s*", "");
        }
        return this;
    }

    public TextFileResource stripComments(final CommentFilter filter) {
        final ArrayList<String> newLines = new ArrayList<String>();
        for (final String line : this.lines) {
            if (!filter.isComment(line)) {
                newLines.add(line);
            }
        }
        this.lines = newLines;
        return this;
    }

    public TextFileResource stripEmptyLines() {
        return stripComments(new EmptyLineFilter());
    }

    public String[] getArray() {
        if (this.lines != null) {
            return this.lines.toArray(new String[this.lines.size()]);
        } else {
            return null;
        }
    }

    public Set<String> getSet() {
        if (this.lines != null) {
            final Set<String> lineSet = new HashSet<String>();
            for (final String op : this.lines) {
                lineSet.add(op);
            }
            return lineSet;
        } else {
            return null;
        }
    }
}
