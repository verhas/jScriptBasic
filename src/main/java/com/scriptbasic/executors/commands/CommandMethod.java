package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExtendedInterpreter;

public class CommandMethod extends AbstractCommand {

    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        interpreter.registerJavaMethod(alias, klass, methodName, argumentTypes);
    }

    private Class<?> klass = null;
    private String methodName = null;
    private String alias = null;
    private Class<?>[] argumentTypes = null;

    /**
     * @return the klass
     */
    public Class<?> getKlass() {
        return klass;
    }

    /**
     * @param klass
     *            the klass to set
     */
    public void setKlass(Class<?> klass) {
        this.klass = klass;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName
     *            the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @return the argumentTypes
     */
    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }

    /**
     * @param argumentTypes
     *            the argumentTypes to set
     */
    public void setArgumentTypes(Class<?>[] argumentTypes) {
        this.argumentTypes = argumentTypes.clone();
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias
     *            the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

}
