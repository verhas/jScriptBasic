package com.scriptbasic.api;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Verhas
 * date July 26, 2012
 * 
 */
public class Version {
    public static final String engineName = "ScriptBasic";
    /**
     * 1L initial release
     * 2L Java 9 compatible version
     */
    public static final long MAJOR = 2L;
    public static final Long MINOR = 0L;
    public static final Long BUGFIX = 0L;
    public static final String version = MAJOR + "." + MINOR + "." + BUGFIX;
    public static final List<String> extensions =
            Arrays.asList("bas","sb");
    public static final List<String> mimeTypes =
            Arrays.asList("application/x-scriptbasic","application/x-basic");
    public static final List<String> names =
            Arrays.asList("basic","sb4j","scriptbasic","jscriptbasic");

    public static final String language = "scriptbasic";
    public static final String languageVersion = "4.0j";
}
