package com.scriptbasic.sourceproviders;

import com.scriptbasic.interfaces.SingleIncludeChecker;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * A very simple (thus basic) single include checker implementation.
 * <p>
 * {@inheritDoc}
 *
 * @author Peter Verhas
 */
public class BasicSingleIncludeChecker implements SingleIncludeChecker {
    private final Set<String> keySet = new HashSet<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void check(final String key) throws IOException {
        if (!keySet.add(key)) {
            throw new IOException("File '" + key + "' was included twice");
        }
    }

}
