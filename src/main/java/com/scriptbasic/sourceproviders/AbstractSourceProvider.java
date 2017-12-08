package com.scriptbasic.sourceproviders;

import com.scriptbasic.api.SourceProvider;
import com.scriptbasic.api.SourceReader;

import java.io.IOException;

/**
 * An abstract source provider to be extended by the source provider
 * implementations.
 *
 * @author Peter Verhas
 */
public abstract class AbstractSourceProvider implements SourceProvider {

    @Override
    public abstract SourceReader get(String sourceName) throws IOException;

    @Override
    public abstract SourceReader get(String sourceName, String referencingSource)
            throws IOException;

}
