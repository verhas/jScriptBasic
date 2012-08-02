/**
 * 
 */
package com.scriptbasic.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.interfaces.Configuration;
import com.scriptbasic.interfaces.Factory;

/**
 * @author Peter Verhas
 * @date July 23, 2012
 * 
 */
public class BasicConfiguration implements Configuration {
    private static final Logger LOG = LoggerFactory
            .getLogger(BasicConfiguration.class);

    Factory factory;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.FactoryManaged#setFactory(com.scriptbasic.
     * interfaces.Factory)
     */
    @Override
    public void setFactory(final Factory factory) {
        this.factory = factory;
    }

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
        final String envKey = "sb4j." + key;
        if (System.getProperty(envKey) != null) {
            configValue = System.getProperty(envKey);
        }
        if (configProperties != null && configProperties.containsKey(key)) {
            configValue = configProperties.getProperty(key);
        }
        if (System.getenv(envKey) != null) {
            configValue = System.getenv(envKey);
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

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Configuration#loadDefaultConfiguration()
     */
    @Override
    public void loadDefaultConfiguration() {
        final InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream("sb4j.properties");
        if (null != is) {
            loadConfiguration(is);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Configuration#loadConfiguration(java.io.
     * InputStream)
     */
    @Override
    public void loadConfiguration(final InputStream is) {
        final Properties configProperties = new Properties();
        try {
            configProperties.load(is);
            setConfigProperties(configProperties);
        } catch (final IOException e) {
            LOG.error("Can not load the configuration.", e);
            setConfigProperties(null);
        }
    }

}
