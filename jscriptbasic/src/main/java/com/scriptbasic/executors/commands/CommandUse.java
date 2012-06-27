package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExtendedInterpreter;

public class CommandUse extends AbstractCommand implements IfOrElse {

    private Class<?> klass = null;
    private String alias = null;

    /**
     * @return the klass
     */
    public Class<?> getKlass() {
        return klass;
    }

    /**
     * @param klass the klass to set
     */
    public void setKlass(Class<?> klass) {
        this.klass = klass;
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
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        interpreter.getUseMap().put(alias, klass);
    }

}
