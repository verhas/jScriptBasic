package com.scriptbasic.sourceproviders;

import com.scriptbasic.interfaces.SourcePath;
import com.scriptbasic.interfaces.SourcePathProvider;
import com.scriptbasic.interfaces.SourceProvider;

/**
 * An abstract source provider extended by all the source provider
 * implementations that rely on a SourcePath implementation.
 * 
 * @author Peter Verhas
 * 
 */
public abstract class AbstractSourcePathSourceProvider extends
        AbstractSourceProvider implements SourcePathProvider, SourceProvider {
    private SourcePath sourcePath;

    public SourcePath getSourcePath() {
        return this.sourcePath;
    }

    public void setSourcePath(SourcePath sourcePath) {
        this.sourcePath = sourcePath;
    }

}
