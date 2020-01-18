package com.scriptbasic.api;

import java.util.Arrays;
import java.util.List;

/**
 * @author Peter Verhas
 * date July 26, 2012
 */
public class Version {
    public static final String engineName = "ScriptBasic";
    /**
     * 1L initial release
     * 2L Java 9 compatible version
     */
    public static final long MAJOR = 2L;
    public static final long MINOR = 1L;
    public static final long BUGFIX = 0L;
    public static final String version = MAJOR + "." + MINOR + "." + BUGFIX;
    public static final List<String> extensions =
            Arrays.asList("bas", "sb");
    public static final List<String> mimeTypes =
            Arrays.asList("application/x-scriptbasic", "application/x-basic");
    public static final List<String> names =
            Arrays.asList("basic", "sb4j", "scriptbasic", "jscriptbasic");

    public static final String language = "scriptbasic";
    /**
     * language version 4.0j was the initial version to tribute the previous 3 version (totally different other than BASIC)
     * which were implemented in C
     *
     * In the language version the suffix 'j' signals Java. This is significant for the language syntax because BASIC
     * programs can call Java methods and thus in this language version there are commands like METHOD and USE.
     *
     * Version 5.0j introduces : as command separator, one line IF statement and case insensitive calls to declared
     * Java methods.
     *
     */
    public static final String languageVersion = "5.0j";
}
