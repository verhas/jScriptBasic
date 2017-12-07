package com.scriptbasic.interfaces;

import com.scriptbasic.api.Configuration;

import java.io.InputStream;

/**
 * Implementing classes should be able to build configuration from some textual
 * source, or programatically.
 *
 * @author Peter Verhas
 * date Jul 23, 2012
 */
public interface ConfigurationBuilder {

    /**
     * Get the built configuration
     *
     * @return
     */
    Configuration getConfiguration();

    void setConfigurationSource(InputStream is);
}
