package com.scriptbasic.utility.functions.file;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class BinaryFileWriter implements FileHandler {
	private BufferedOutputStream stream;

	public void close() throws IOException {
		stream.close();
	}

	public void write(byte[] buffer) throws IOException{
		stream.write(buffer, 0, buffer.length);
	}
	
	public BinaryFileWriter(BufferedOutputStream bos) {
		stream = bos;
	}
}
