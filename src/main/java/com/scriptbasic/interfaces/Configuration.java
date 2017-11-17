/**
 * 
 */
package com.scriptbasic.interfaces;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

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
 * date Jul 23, 2012
 * 
 */
public interface Configuration extends FactoryManaged {
    /**
     * Configuration of the script engine comes from standard Java properties.
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
    Optional<String> getConfigValue(String key);

    /**
     * Get an indexed value. The indexed value is simple associated with the
     * {@code key.i} property.
     *
     * @param key the string key
     * @param i the index value usually goes from zero
     * @return the configuration value
     */
    default Optional<String> getConfigValue(String key, int i) {
        return getConfigValue(key + "." + i);
    }

    /**
     * Return the configuration values assigned to a keys 'key.0', 'key.1'... 'key.n' as a stream.
     *
     * @param key the key to which the values are assigned using the numberic key postfix
     *
     * @return the stream of the values
     */
    default Stream<String> getConfigValueStream(String key){
        return getConfigValueList(key).stream();
    }

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
