package com.scriptbasic.sourceproviders;

import java.io.IOException;

import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.interfaces.SingleIncludeChecker;

/**
 * An abstract class to be extended by the source provider implementations that
 * do not want to include one source more than once.
 * <p>
 * The implementations should not override the methods {@code get()} but should
 * implement the abstract methods {@code getSource()}. The method
 * {@code getSource()} is invoked only when it is ensured that the source was
 * not yet included.
 * 
 * @author Peter Verhas
 * 
 */
public abstract class SingleIncludeSourceProvider extends
        AbstractSourceProvider {

    private SingleIncludeChecker singleInclude = new BasicSingleIncludeChecker();

    public SingleIncludeChecker getSingleInclude() {
        return singleInclude;
    }

    public void setSingleInclude(SingleIncludeChecker singleInclude) {
        this.singleInclude = singleInclude;
    }

    @Override
    public final Reader get(String sourceName) throws IOException {
        singleInclude.check(getKeyName(sourceName));
        return getSource(sourceName);
    }

    @Override
    public final Reader get(String sourceName, String referencingSource)
            throws IOException {
        singleInclude.check(getKeyName(sourceName, referencingSource));
        return getSource(sourceName, referencingSource);
    }

    protected abstract Reader getSource(String sourceName) throws IOException;

    protected abstract Reader getSource(String sourceName,
            String referencingSource) throws IOException;

    protected abstract String getKeyName(String sourceName);

    protected abstract String getKeyName(String sourceName,
            String referencingSource);
}
