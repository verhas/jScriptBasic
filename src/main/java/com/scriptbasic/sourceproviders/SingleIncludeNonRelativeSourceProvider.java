package com.scriptbasic.sourceproviders;

import com.scriptbasic.readers.SourceReader;

import java.io.IOException;

/**
 * Abstract class to be extended by source path implementations that include a
 * file only once and which include file only using full path.
 * <p>
 * The {@code NonRelative} source providers fall back to the functionality of
 * the single parameter method {@code get()} from the version that also
 * specifies the name of the file where the include statement is. In other words
 * for such source provider it is indifferent which file includes the included
 * file. Still in other words it is not possible to include a source using
 * relative source (file) name.
 *
 * @author Peter Verhas
 */
public abstract class SingleIncludeNonRelativeSourceProvider extends
        AbstractSingleIncludeSourceProvider {

    @Override
    protected abstract SourceReader getSource(String sourceName) throws IOException;

    @Override
    protected SourceReader getSource(final String sourceName,
                                     final String referencingSource) throws IOException {
        return getSource(sourceName);
    }

    @Override
    protected String getKeyName(final String sourceName) {
        return sourceName;
    }

    @Override
    protected String getKeyName(final String sourceName,
                                final String referencingSource) {
        return getKeyName(sourceName);
    }

}
