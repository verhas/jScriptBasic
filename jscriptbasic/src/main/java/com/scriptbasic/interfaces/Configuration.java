/**
 * 
 */
package com.scriptbasic.interfaces;

import java.util.Set;

/**
 * Manage the configuration of the interpreter. Configuration parameters can be
 * requested from the implementing class. Configuration is read from input
 * stream and created by a {@see ConfigurationBuilder}
 * 
 * @author Peter Verhas
 * @date Jul 23, 2012
 * 
 */
public interface Configuration {

    public Set<String> getConfigKeys();
    
    public String getConfigValue(String key);

    public String getConfigValue(String key, String defaultValue);
}
