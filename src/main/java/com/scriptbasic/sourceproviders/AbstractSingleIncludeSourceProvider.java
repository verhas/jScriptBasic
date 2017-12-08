package com.scriptbasic.sourceproviders;

import com.scriptbasic.interfaces.SingleIncludeChecker;
import com.scriptbasic.api.SourceReader;

import java.io.IOException;

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
 */
public abstract class AbstractSingleIncludeSourceProvider extends
        AbstractSourceProvider {

    private SingleIncludeChecker singleInclude = new BasicSingleIncludeChecker();

    public SingleIncludeChecker getSingleInclude() {
        return this.singleInclude;
    }

    public void setSingleInclude(final SingleIncludeChecker singleInclude) {
        this.singleInclude = singleInclude;
    }

    @Override
    public final SourceReader get(final String sourceName) throws IOException {
        this.singleInclude.check(getKeyName(sourceName));
        return getSource(sourceName);
    }

    @Override
    public final SourceReader get(final String sourceName,
                                  final String referencingSource) throws IOException {
        this.singleInclude.check(getKeyName(sourceName, referencingSource));
        return getSource(sourceName, referencingSource);
    }

    protected abstract SourceReader getSource(String sourceName) throws IOException;

    protected abstract SourceReader getSource(String sourceName,
                                              String referencingSource) throws IOException;

    protected abstract String getKeyName(String sourceName);

    protected abstract String getKeyName(String sourceName,
                                         String referencingSource);
}
