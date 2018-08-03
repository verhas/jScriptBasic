package com.scriptbasic.utility.functions.file;

import java.io.BufferedReader;
import java.io.IOException;

public class TextFileReader implements FileHandler, AutoCloseable {
    private final BufferedReader reader;

    protected TextFileReader(final BufferedReader br) {
        reader = br;
    }

    public void close() throws IOException {
        reader.close();
    }

    protected String readLine() throws IOException {
        return reader.readLine();
    }
}
