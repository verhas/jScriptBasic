package com.scriptbasic.utility.functions.file;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class BinaryFileWriter implements FileHandler {
    private final BufferedOutputStream stream;

    public BinaryFileWriter(BufferedOutputStream stream) {
        this.stream = stream;
    }

    public void close() throws IOException {
        stream.close();
    }

    public void write(byte[] buffer) throws IOException {
        stream.write(buffer, 0, buffer.length);
    }
}
