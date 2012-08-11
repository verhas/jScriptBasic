package com.scriptbasic.sourceproviders;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.readers.GenericReader;
/**
 * This implementation provides the source from strings.
 *
 * @author Peter Verhas
 *
 */
public class StringSourceProvider extends
        SingleIncludeNonRelativeSourceProvider {
    private final Map<String, String> sourceMap = new HashMap<String, String>();
    /**
     * Add a new source to the set of available sources.
     *
     * @param fileName
     *            the name of the source. This name can be used in include
     *            statements in other BASIC sources and also this can be used to
     *            get the reader from the provider.
     * @param sourceCode
     *            The actual code of the source program.
     */
    public void addSource(final String fileName, final String sourceCode) {
        this.sourceMap.put(fileName, sourceCode);
    }
    /**
     * {@inheritDoc}
     *
     * This implementation returns a {@see GenericReader}.
     */
    @Override
    public Reader getSource(final String sourceName) throws IOException {
        if (!this.sourceMap.containsKey(sourceName)) {
            throw new IOException("The source '" + sourceName
                    + "' was not set.");
        }
        final GenericReader reader = new GenericReader();
        reader.set(sourceName);
        reader.setSourceProvider(this);
        reader.set(new StringReader(this.sourceMap.get(sourceName)));
        return reader;
    }
}