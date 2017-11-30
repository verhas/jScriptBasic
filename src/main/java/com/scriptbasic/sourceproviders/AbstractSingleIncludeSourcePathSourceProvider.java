package com.scriptbasic.sourceproviders;

import com.scriptbasic.interfaces.SingleIncludeChecker;
import com.scriptbasic.interfaces.SourceReader;

import java.io.IOException;

/**
 * Abstract class to be extended by source provider implementations that include
 * a single file only once and use source path.
 * <p>
 * The implementations should not override the methods {@code get()} but should
 * implement the abstract methods {@code getSource()}. The method
 * {@code getSource()} is invoked only when it is ensured that the source was
 * not yet included.
 * 
 * @author Peter Verhas
 * 
 */
public abstract class AbstractSingleIncludeSourcePathSourceProvider extends
        AbstractSourcePathSourceProvider {

    private SingleIncludeChecker singleInclude = new BasicSingleIncludeChecker();

    /**
     * 
     * @return the actual value of the single include checker
     */
    public SingleIncludeChecker getSingleInclude() {
        return this.singleInclude;
    }

    /**
     * This setter can be called if the BasicSingleIncludeChecker as
     * implementation is not appropriate. The default constructor will create
     * one such object but later dependency injection can insert any object
     * implementing the SingleIncludeChecker interface.
     * <p>
     * This method allows configuration of these source providers in a DI
     * container.
     * 
     * @param singleInclude
     */
    public void setSingleInclude(final SingleIncludeChecker singleInclude) {
        this.singleInclude = singleInclude;
    }

    @Override
    public final SourceReader get(final String sourceName) throws IOException {
        SourceReader reader;
        if (this.singleInclude == null) {
            reader = null;
        } else {
            this.singleInclude.check(getKeyName(sourceName));
            reader = getSource(sourceName);
        }
        return reader;
    }

    @Override
    public final SourceReader get(final String sourceName,
                                  final String referencingSource) throws IOException {
        SourceReader reader;
        if (this.singleInclude == null) {
            reader = null;
        } else {
            this.singleInclude.check(getKeyName(sourceName, referencingSource));
            reader = getSource(sourceName, referencingSource);
        }
        return reader;
    }

    protected abstract SourceReader getSource(String sourceName) throws IOException;

    protected abstract SourceReader getSource(String sourceName,
                                              String referencingSource) throws IOException;

    protected abstract String getKeyName(String sourceName);

    protected abstract String getKeyName(String sourceName,
            String referencingSource);
}
