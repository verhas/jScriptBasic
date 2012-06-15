package com.scriptbasic.interfaces;

import java.io.IOException;

/**
 * Checks that a file is included only once. This is important to prevent
 * circular includes. When the LexicalAnalyzer realizes that a BASIC program
 * tries to include another file, it uses this checker.
 * 
 * @author Peter Verhas
 * 
 */
public interface SingleIncludeChecker {
    /**
     * Check that the source file was not included yet. If the file was already
     * included then the method throws {@code IOException}.
     * 
     * @param key
     *            in a unique key that identifies a file. This is usually the
     *            full path to the file. It is important that the {@code key}
     *            for the file is always the same otherwise the checker can not
     *            recognize the repeated include.
     * @throws IOException
     *             if the file was already included.
     */
    void check(String key) throws IOException;
}
