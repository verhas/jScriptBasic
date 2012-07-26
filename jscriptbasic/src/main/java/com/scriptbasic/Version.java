/**
 * 
 */
package com.scriptbasic;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Verhas
 * @date July 26, 2012
 * 
 */
public class Version {
    public static final long MAJOR = 1L;
    public static final Long MINOR = 0L;
    public static final Long BUGFIX = 0L;
    public static final String version = MAJOR + "." + MINOR + "." + BUGFIX;
    public static final List<String> extensions = new LinkedList<>();
    static {
        extensions.add("bas");
        extensions.add("sb");
    }
    public static final List<String> mimeTypes = new LinkedList<>();
    static {
        mimeTypes.add("application/x-scriptbasic");
        mimeTypes.add("application/x-basic");
    }
    public static final List<String> names = new LinkedList<>();
    static {
        names.add("basic");
        names.add("sb4j");
        names.add("scriptbasic");
        names.add("jscriptbasic");
    }
    
    public static final String language = "scriptbasic";
    public static final String languageVersion = "4.0j";
}
