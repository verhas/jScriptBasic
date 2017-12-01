package com.scriptbasic.interfaces;

/**
 * This interface is usually implemented some {@code SourceProvider}
 * implementations. This way the setup code can access the {@code SourcePath} to
 * manage it and to use it.
 *
 * @author Peter Verhas
 */
public interface SourcePathProvider {
    SourcePath getSourcePath();

    void setSourcePath(SourcePath sourcePath);
}
