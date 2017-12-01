package com.scriptbasic.sourceproviders;

import com.scriptbasic.interfaces.HierarchicalSourceReader;
import com.scriptbasic.interfaces.SourceReader;
import com.scriptbasic.readers.GenericHierarchicalSourceReader;
import com.scriptbasic.readers.GenericSourceReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * This implementation provides the source from strings.
 *
 * @author Peter Verhas
 */
public class StringSourceProvider extends
        SingleIncludeNonRelativeSourceProvider {

    private final Map<String, String> sourceMap = new HashMap<>();

    /**
     * Add a new source to the set of available sources.
     *
     * @param fileName   the name of the source. This name can be used in include
     *                   statements in other BASIC sources and also this can be used to
     *                   get the reader from the provider.
     * @param sourceCode The actual code of the source program.
     */
    public void addSource(final String fileName, final String sourceCode) {
        this.sourceMap.put(fileName, sourceCode);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns a {@link GenericSourceReader}.
     */
    @Override
    public SourceReader getSource(final String sourceName) throws IOException {
        if (!this.sourceMap.containsKey(sourceName)) {
            throw new IOException("The source '" + sourceName
                    + "' was not set.");
        }
        final SourceReader reader = new GenericSourceReader(new StringReader(this.sourceMap.get(sourceName)), this, sourceName);
        final HierarchicalSourceReader hreader = new GenericHierarchicalSourceReader(reader);
        return hreader;
    }
}
