package com.scriptbasic.utility.functions.file;

import java.io.BufferedReader;
import java.io.IOException;

public class TextFileReader implements FileHandler {
    private BufferedReader reader = null;

    protected TextFileReader(BufferedReader br) {
        reader = br;
    }

    public void close() throws IOException {
        reader.close();
    }

    protected String readLine() throws IOException {
        return reader.readLine();
    }
}
