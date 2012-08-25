/**
 * 
 */
package com.scriptbasic.interfaces;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Manage the configuration of the interpreter. Configuration parameters can be
 * requested from the implementing class. Configuration is read from input
 * stream, properties files.
 * <p>
 * When requesting individual configuration keys the implementation can look for
 * the values in different sources. Typical implementation will look up values
 * in the system properties and environment first before searching the
 * configuration properties.
 * 
 * @author Peter Verhas
 * @date Jul 23, 2012
 * 
 */
public interface Configuration extends FactoryManaged {
    /**
     * Configuration of the script engine is fed from standard Java properties.
     * Calling this method the caller can set the properties for the
     * configuration. The properties passed as argument will be used as
     * configuration solely and will NOT be merged with the keys and values of
     * other property files.
     * <p>
     * The fact that all other, possibly already loaded properties are not taken
     * into account does not affect the fact that the configuration
     * implementation itself may search other sources in addition to the
     * properties to locate values for certain configuration keys.
     * 
     * @param configProperties
     *            the properties to be used as configuration properties.
     */
    void setConfigProperties(Properties configProperties);

    /**
     * Get the value of the key.
     * 
     * @param key
     *            the configuration key for which the value is looked up.
     * @return the string value of the configuration or {@code null} if the key
     *         is not configured.
     */
    String getConfigValue(String key);

    /**
     * Complimentary method calling {@see #getConfigValue(String)} but returning
     * the {@code defaultValue} instead of {@code null} if the {@code key} is
     * not configured.
     * 
     * @param key
     *            the configuration key, same as in {@see
     *            #getConfigValue(String)}.
     * @param defaultValue
     *            is the default value string to return if the configuration key
     *            is not found
     * @return the string value of the configuration or {@code defaultValue} if
     *         the key is not configured.
     */
    String getConfigValue(String key, String defaultValue);

    /**
     * Returns a list of strings that are the values assigned to the key in the
     * configuration. List of strings in the configuration should be defined
     * using the notation {@code key}'.n' where 'n' starts with zero and should
     * increment by one continuously.
     * 
     * @param key
     * @return the list of configured strings
     */
    List<String> getConfigValueList(final String key);

    /**
     * Load the default configuration. The implementation should define some
     * default properties file to load the configuration from.
     */
    void loadDefaultConfiguration();

    /**
     * Load the configuration from an input stream.
     * 
     * @param is
     *            the input stream that is used to read the content of the
     *            properties file.
     */
    void loadConfiguration(final InputStream is);
}
