package com.scriptbasic.interfaces;

/**
 * This interface is usually implemented some Ê{@see SourceProvider}
 * implementations. This way the setup code can access the {@see SourcePath} to
 * manage it and to use it.
 * 
 * @author Peter Verhas
 * 
 */
public interface SourcePathProvider {
    public SourcePath getSourcePath();

    public void setSourcePath(SourcePath sourcePath);
}
