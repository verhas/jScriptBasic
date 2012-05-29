package com.scriptbasic.sourceproviders;

import java.io.IOException;

import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.interfaces.SourceProvider;

/**
 * An abstract source provider to be extended by the source provider implementations.
 * @author Peter Verhas
 *
 */
public abstract class AbstractSourceProvider implements SourceProvider {

    @Override
    public abstract Reader get(String sourceName) throws IOException;

    @Override
    public abstract Reader get(String sourceName, String referencingSource)
            throws IOException;

}
