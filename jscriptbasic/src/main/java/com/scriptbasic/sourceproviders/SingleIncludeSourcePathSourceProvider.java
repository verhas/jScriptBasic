package com.scriptbasic.sourceproviders;

import java.io.IOException;

import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.interfaces.SingleIncludeChecker;

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
public abstract class SingleIncludeSourcePathSourceProvider extends
        AbstractSourcePathSourceProvider {

    private SingleIncludeChecker singleInclude = new BasicSingleIncludeChecker();

    /**
     * 
     * @return the actual value of the single include checker
     */
    public SingleIncludeChecker getSingleInclude() {
        return singleInclude;
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
    public void setSingleInclude(SingleIncludeChecker singleInclude) {
        this.singleInclude = singleInclude;
    }

    @Override
    public final Reader get(String sourceName) throws IOException {
        if (singleInclude == null) {
            return null;
        } else {
            singleInclude.check(getKeyName(sourceName));
            return getSource(sourceName);
        }
    }

    @Override
    public final Reader get(String sourceName, String referencingSource)
            throws IOException {
        if (singleInclude == null) {
            return null;
        } else {
            singleInclude.check(getKeyName(sourceName, referencingSource));
            return getSource(sourceName, referencingSource);
        }
    }

    protected abstract Reader getSource(String sourceName) throws IOException;

    protected abstract Reader getSource(String sourceName,
            String referencingSource) throws IOException;

    protected abstract String getKeyName(String sourceName);

    protected abstract String getKeyName(String sourceName,
            String referencingSource);
}
