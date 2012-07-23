/**
 * 
 */
package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.interfaces.Configuration;
import com.scriptbasic.interfaces.ConfigurationBuilder;

/**
 * @author Peter Verhas
 * @date July 23, 2012
 * 
 */
public class BasicConfigurationBuilder implements ConfigurationBuilder {
    Logger LOG = LoggerFactory.getLogger(BasicConfigurationBuilder.class);
    private BasicConfiguration configuration = null;

    private void loadDefaultConfiguration() {
        final InputStream is = this.getClass().getResourceAsStream(
                "sb4j.properties");
        loadConfiguration(is);
    }

    private void loadConfiguration(final InputStream is) {
        if (configuration == null) {
            configuration = new BasicConfiguration();
        }
        final Properties configProperties = new Properties();
        try {
            configProperties.load(is);
            configuration.setConfigProperties(configProperties);
        } catch (final IOException e) {
            LOG.error("Can not load the configuration.", e);
            configuration = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.ConfigurationBuilder#getConfiguration()
     */
    @Override
    public Configuration getConfiguration() {
        if (configuration == null) {
            loadDefaultConfiguration();
        }
        return configuration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.ConfigurationBuilder#setConfigurationSource
     * (java.io.InputStream)
     */
    @Override
    public void setConfigurationSource(final InputStream is) {
        loadConfiguration(is);
    }

}
