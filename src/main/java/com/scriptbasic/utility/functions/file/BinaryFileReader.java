package com.scriptbasic.utility.functions.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class BinaryFileReader implements FileHandler {
	private InputStream stream;

	public BinaryFileReader(InputStream is) {
		stream = is;
	}

	public void close() throws IOException {
		stream.close();
	}

	public byte[] read(int len) throws IOException {
		byte[] b = new byte[len];
		int bytesRead = stream.read(b);
		if (bytesRead < len) {
			b = Arrays.copyOf(b, bytesRead);
		}
		return b;
	}
}
