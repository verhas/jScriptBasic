/**
 * 
 */
package com.scriptbasic.configuration;

import com.scriptbasic.interfaces.Factory;

/**
 * @author Peter Verhas
 * @date July 23, 2012
 * 
 */
public class ScriptBasicConfiguration extends BuildingScriptBasicConfiguration {
    private ScriptBasicConfiguration() {
        super();
    }

    public boolean classificationIsPermitted(Class<?>[] classifications) {
        boolean permitUse = true;
        for (Class<?> classification : classifications) {
            final String classClassification = getConfigValue("classification."
                    + classification.getName());
            if ("deny".equals(classClassification)) {
                permitUse = false;
            }
        }
        return permitUse;
    }

    Factory factory;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.FactoryManaged#setFactory(com.scriptbasic.
     * interfaces.Factory)
     */
    @Override
    public void setFactory(Factory factory) {
        this.factory = factory;
    }

}
