package com.scriptbasic.sourceproviders;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.readers.GenericReader;

/**
 * A source provider that reads the sources from files. This provider includes a
 * file only once and does not support relative file name 'include'. Also this
 * implementation uses the {@link com.scriptbasic.interfaces.SourcePath} to find a file.
 * 
 * @author Peter Verhas
 * 
 */
public class FileSourceProvider extends
		SingleIncludeSourcePathNonRelativeSourceProvider {
	private static final String PATH_SEPARATOR = File.separator;

	@Override
	public Reader getSource(final String sourceName) throws IOException {
		for (final String path : getSourcePath()) {
			final String sourceFileName = path + PATH_SEPARATOR + sourceName;
			final File sourceFile = new File(sourceFileName);
			if (sourceFile.exists()) {
				final GenericReader reader = new GenericReader(new FileReader(sourceFile),this, sourceFileName);
				return reader;
			}
		}
		throw new IOException("can not find included file '" + sourceName + "'");
	}
}
