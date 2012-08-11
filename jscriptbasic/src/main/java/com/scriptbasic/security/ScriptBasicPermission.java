/**
 *
 */
package com.scriptbasic.security;
import java.security.Permission;
/**
 * @author Peter Verhas
 * date July 29, 2012
 *
 */
public class ScriptBasicPermission extends Permission {
    private Class<?> actingClass;
    private String actingClassName;
    public ScriptBasicPermission(Class<?> actingClass) {
        super("ScriptBasicPermission");
        this.setActingClass(actingClass);
    }
    /**
     * @return the actingClass
     */
    public Class<?> getActingClass() {
        return actingClass;
    }
    /**
     * @param actingClass the actingClass to set
     */
    private void setActingClass(Class<?> actingClass) {
        this.actingClass = actingClass;
        this.actingClassName = actingClass.getName();
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((actingClassName == null) ? 0 : actingClassName.hashCode());
        return result;
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ScriptBasicPermission)) {
            return false;
        }
        ScriptBasicPermission other = (ScriptBasicPermission) obj;
        if (actingClassName == null) {
            if (other.actingClassName != null) {
                return false;
            }
        } else if (!actingClassName.equals(other.actingClassName)) {
            return false;
        }
        return true;
    }
    /*
     * (non-Javadoc)
     *
     * @see java.security.Permission#implies(java.security.Permission)
     */
    @Override
    public boolean implies(Permission permission) {
        return false;
    }
    /*
     * (non-Javadoc)
     *
     * @see java.security.Permission#getActions()
     */
    @Override
    public String getActions() {
        return null;
    }
}