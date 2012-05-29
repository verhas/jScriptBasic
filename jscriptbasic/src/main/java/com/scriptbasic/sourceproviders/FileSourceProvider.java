package com.scriptbasic.sourceproviders;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.readers.GenericReader;

/**
 * A source provider that reads the sources from files. This provide includes a
 * file only once and does not support relative file name including. Also this
 * implementation uses the SourcePath to find a file.
 * 
 * @author Peter Verhas
 * 
 */
public class FileSourceProvider extends
        SingleIncludeSourcePathNonRelativeSourceProvider {
    private static final String ps = System.getProperty("path.separator");

    @Override
    public Reader getSource(String sourceName) throws IOException {
        for (String path : getSourcePath()) {
            String sourceFileName = path + ps + sourceName;
            File sourceFile = new File(sourceFileName);
            if (sourceFile.exists()) {
                GenericReader reader = new GenericReader();
                reader.set(sourceFileName);
                reader.setSourceProvider(this);
                reader.set(new FileReader(sourceFile));
                return reader;
            }
        }
        throw new IOException("can not find included file '" + sourceName + "'");
    }
}
