/**
 * 
 */
package com.scriptbasic.configuration;

/**
 * Just a class to use to compile the class BasicConfiguration instead of
 * java.lang.System when compiling for test. Later this class can be mocked
 * using PowerMock. java.lang.System can not be mocked.
 * 
 * @author Peter Verhas
 * @date Aug 3, 2012
 * 
 */
public class System {
    public static String getenv(String key) {
        return null;
    }

    public static String getProperty(String key) {
        return null;
    }
}
