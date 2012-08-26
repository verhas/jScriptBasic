/**
 * 
 */
package com.scriptbasic.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.scriptbasic.interfaces.Configuration;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;

/**
 * @author Peter Verhas
 * date July 23, 2012
 * 
 */
public class BasicConfiguration implements Configuration {
    private static final Logger LOG = LoggerFactory
            .getLogger(BasicConfiguration.class);

    Factory factory;

    public BasicConfiguration() {
        try {
            loadDefaultConfiguration();
        } catch (Exception e) {
            LOG.error("Configuration was not loaded", e);
        }
    }

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
        lists.clear();
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
        if (configProperties != null && configProperties.containsKey(key)) {
            configValue = configProperties.getProperty(key);
        }
        String sysValue = null;
        if ((sysValue = System.getenv(envKey)) != null) {
            configValue = sysValue;
        }
        if ((sysValue = System.getProperty(envKey)) != null) {
            configValue = sysValue;
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

    private final HashMap<String, List<String>> lists = new HashMap<String, List<String>>();

    @Override
    public List<String> getConfigValueList(final String key) {
        if (lists.containsKey(key)) {
            return lists.get(key);
        }
        List<String> list = new LinkedList<String>();
        String keyi;
        String value;
        for (int i = 0; (keyi = key + "." + i) != null
                && (value = getConfigValue(keyi)) != null; i++) {
            list.add(value);
        }
        lists.put(keyi, list);
        return list;
    }

    /**
     * The default configuration is stored in the file {@code sb4j.properties}
     * or in the file defined by the system property named
     * {@code sb4j.configuration}.
     * 
     * @see Configuration#loadDefaultConfiguration()
     */
    @Override
    public void loadDefaultConfiguration() {
        final String systemPropertyDefinedConfiguration = System
                .getProperty("sb4j.configuration");
        final String configurationFileName = systemPropertyDefinedConfiguration == null ? "sb4j.properties"
                : systemPropertyDefinedConfiguration;
        final InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(configurationFileName);
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
