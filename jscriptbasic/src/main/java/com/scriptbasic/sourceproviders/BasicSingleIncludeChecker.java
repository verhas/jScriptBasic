package com.scriptbasic.sourceproviders;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.scriptbasic.interfaces.SingleIncludeChecker;

/**
 * A very simple (thus basic) single include checker implementation.
 * 
 * {@inheritDoc}
 * 
 * @author Peter Verhas
 */
public class BasicSingleIncludeChecker implements SingleIncludeChecker {
    private final Set<String> keySet = new HashSet<String>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void check(final String key) throws IOException {
        if (keySet.contains(key)) {
            throw new IOException("File '" + key + "' was included twice");
        }
        keySet.add(key);
    }

}
