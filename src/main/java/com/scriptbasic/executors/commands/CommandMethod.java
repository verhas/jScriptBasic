package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Interpreter;

public class CommandMethod extends AbstractCommand {

    private Class<?> klass = null;
    private String methodName = null;
    private String alias = null;
    private Class<?>[] argumentTypes = null;

    @Override
    public void execute(final Interpreter interpreter) throws BasicRuntimeException {
        interpreter.registerJavaMethod(alias, klass, methodName, argumentTypes);
    }

    /**
     * @return the klass
     */
    public Class<?> getKlass() {
        return klass;
    }

    /**
     * @param klass the klass to set
     */
    public void setKlass(final Class<?> klass) {
        this.klass = klass;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }

    /**
     * @return the argumentTypes
     */
    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }

    /**
     * @param argumentTypes the argumentTypes to set
     */
    public void setArgumentTypes(final Class<?>[] argumentTypes) {
        this.argumentTypes = argumentTypes.clone();
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(final String alias) {
        this.alias = alias;
    }

}
