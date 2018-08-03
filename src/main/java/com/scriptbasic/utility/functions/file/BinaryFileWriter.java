package com.scriptbasic.utility.functions.file;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class BinaryFileWriter implements FileHandler, AutoCloseable {
    private final BufferedOutputStream stream;

    public BinaryFileWriter(final BufferedOutputStream stream) {
        this.stream = stream;
    }

    public void close() throws IOException {
        stream.close();
    }

    public void write(final byte[] buffer) throws IOException {
        stream.write(buffer, 0, buffer.length);
    }
}
