/**
 *
 */
package com.scriptbasic.security;
/**
 * @author Peter Verhas
 * date July 29, 2012
 *
 */
public class CommandPermission extends ScriptBasicPermission {
    public CommandPermission(Class<?> actiongClass) {
        super(actiongClass);
    }
}