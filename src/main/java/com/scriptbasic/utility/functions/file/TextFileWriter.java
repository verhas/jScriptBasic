package com.scriptbasic.utility.functions.file;

import java.io.BufferedWriter;
import java.io.IOException;

public class TextFileWriter implements FileHandler {
    private BufferedWriter writer;

    public TextFileWriter(final BufferedWriter bw) {
        writer = bw;
    }

    public void close() throws IOException {
        writer.close();
    }

    public void print(final String line) throws IOException {
        writer.write(line);
    }

    public void newLine() throws IOException {
        writer.newLine();
    }
}
