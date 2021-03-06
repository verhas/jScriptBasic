package com.scriptbasic.sourceproviders;

import com.scriptbasic.readers.GenericHierarchicalSourceReader;
import com.scriptbasic.readers.GenericSourceReader;
import com.scriptbasic.readers.SourcePath;
import com.scriptbasic.readers.SourceReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * A source provider that reads the sources from files. This provider includes a
 * file only once and does not support relative file name 'include'. Also this
 * implementation uses the {@link SourcePath} to find a file.
 *
 * @author Peter Verhas
 */
public class FileSourceProvider extends SingleIncludeSourcePathNonRelativeSourceProvider {
    private static final String PATH_SEPARATOR = File.separator;

    @Override
    public SourceReader getSource(final String sourceName) throws IOException {
        for (final String path : getSourcePath()) {
            final var sourceFileName = path + PATH_SEPARATOR + sourceName;
            final var sourceFile = new File(sourceFileName);
            if (sourceFile.exists()) {
                final var reader = new GenericSourceReader(new FileReader(sourceFile), this, sourceFileName);
                return new GenericHierarchicalSourceReader(reader);
            }
        }
        throw new IOException("can not find included file '" + sourceName + "'");
    }
}
