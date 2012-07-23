/**
 * 
 */
package configuration;

/**
 * @author Peter Verhas
 * @date Jul 23, 2012
 * 
 */
public class ScriptBasicConfiguration {
    private BasicConfiguration configuration;

    /**
     * @param configuration
     *            the configuration to set
     */
    public void setConfiguration(BasicConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean classificationIsPermitted(Class<?>[] classifications) {
        return false;
    }
}
