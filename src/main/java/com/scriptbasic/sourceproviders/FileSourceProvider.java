package com.scriptbasic.sourceproviders;

import com.scriptbasic.api.SourcePath;
import com.scriptbasic.api.SourceReader;
import com.scriptbasic.readers.GenericHierarchicalSourceReader;
import com.scriptbasic.readers.GenericSourceReader;

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
            final String sourceFileName = path + PATH_SEPARATOR + sourceName;
            final File sourceFile = new File(sourceFileName);
            if (sourceFile.exists()) {
                final SourceReader reader = new GenericSourceReader(new FileReader(sourceFile), this, sourceFileName);
                return new GenericHierarchicalSourceReader(reader);
            }
        }
        throw new IOException("can not find included file '" + sourceName + "'");
    }
}
