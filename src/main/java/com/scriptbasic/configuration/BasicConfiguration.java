package com.scriptbasic.configuration;

import com.scriptbasic.api.Configuration;
import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Peter Verhas
 * date July 23, 2012
 */
public class BasicConfiguration implements Configuration {
    private static final Logger LOG = LoggerFactory.getLogger();
    private final Map<String, List<String>> lists = new HashMap<>();
    private Properties configProperties;
    public BasicConfiguration() {
        try {
            loadDefaultConfiguration();
        } catch (final Exception e) {
            LOG.error("Configuration was not loaded", e);
        }
    }


    public Properties getConfigProperties(){
        return configProperties;
    }

    /**
     * @param configProperties the configProperties to set
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
    public Optional<String> getConfigValue(final String key) {
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
        return Optional.ofNullable(configValue);
    }

    @Override
    public List<String> getConfigValueList(final String key) {
        if (lists.containsKey(key)) {
            return lists.get(key);
        }
        final List<String> list = new LinkedList<>();
        for (int i = 0; getConfigValue(key, i).isPresent(); i++) {
            list.add(getConfigValue(key, i).get());
        }
        lists.put(key, list);
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
        LOG.info("Reading configuration from file {}",configurationFileName);
        final InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(configurationFileName);
        if (null == is) {
            LOG.info("Configuration file does not exist.");
            setConfigProperties(new Properties());
        }else{
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
            setConfigProperties(new Properties());
        }
    }

    @Override
    public void set(final String name, final String value) {
        configProperties.put(name, value);
    }
    @Override
    public void set(final String command) {
        int index = command.indexOf("=");
        if( index == -1 ){
            throw new BasicInterpreterInternalError("Configuration command '"+command+"' is invalid.");
        }
        configProperties.put(command.substring(0,index), command.substring(index+1));
    }
}
