/**
 * 
 */
package com.scriptbasic.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peter Verhas
 * @date July 23, 2012
 * 
 */
public abstract class BuildingScriptBasicConfiguration extends
        BasicConfiguration {
    Logger LOG = LoggerFactory
            .getLogger(BuildingScriptBasicConfiguration.class);

    protected BuildingScriptBasicConfiguration() {
        super();
        loadDefaultConfiguration();
    }

    protected void loadDefaultConfiguration() {
        final InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream("sb4j.properties");
        if (null != is) {
            loadConfiguration(is);
        }
    }

    protected void loadConfiguration(final InputStream is) {
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
