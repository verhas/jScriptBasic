/**
 * 
 */
package configuration;

import java.util.Properties;
import java.util.Set;

import com.scriptbasic.interfaces.Configuration;

/**
 * @author Peter Verhas
 * @date Jul 23, 2012
 * 
 */
public class BasicConfiguration implements Configuration {
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
        if (configProperties.containsKey(key)) {
            configValue = configProperties.getProperty(key);
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
     * @see com.scriptbasic.interfaces.Configuration#getConfigKeys()
     */
    @Override
    public Set<String> getConfigKeys() {
        return configProperties.stringPropertyNames();
    }
}
