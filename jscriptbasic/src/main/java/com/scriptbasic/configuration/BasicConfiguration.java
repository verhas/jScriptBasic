/**
 * 
 */
package com.scriptbasic.configuration;

import java.util.Properties;

import com.scriptbasic.interfaces.Configuration;

/**
 * @author Peter Verhas
 * @date July 23, 2012
 * 
 */
public abstract class BasicConfiguration implements Configuration {
    Properties configProperties;

    /**
     * @param configProperties
     *            the configProperties to set
     */
    public void setConfigProperties(final Properties configProperties) {
        this.configProperties = configProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Configuration#getConfig(java.lang.String)
     */
    @Override
    public String getConfigValue(final String key) {
        String configValue = null;
        if (configProperties != null && configProperties.containsKey(key)) {
            configValue = configProperties.getProperty(key);
        }
        String envKey = "sb4j." + key;
        if (System.getenv(envKey) != null) {
            configValue = System.getenv(envKey);
        }
        if (System.getProperty(envKey) != null) {
            configValue = System.getProperty(envKey);
        }
        return configValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Configuration#getConfig(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getConfigValue(final String key, final String defaultValue) {
        final String configValue = getConfigValue(key);
        return configValue == null ? defaultValue : configValue;
    }

}
