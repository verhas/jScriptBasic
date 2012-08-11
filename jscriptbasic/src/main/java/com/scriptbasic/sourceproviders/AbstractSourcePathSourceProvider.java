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
    @Override
    public SourcePath getSourcePath() {
        return this.sourcePath;
    }
    @Override
    public void setSourcePath(final SourcePath sourcePath) {
        this.sourcePath = sourcePath;
    }
}