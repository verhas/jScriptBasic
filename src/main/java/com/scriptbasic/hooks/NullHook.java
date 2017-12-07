package com.scriptbasic.hooks;

import com.scriptbasic.api.Command;
import com.scriptbasic.api.InterpreterHook;
import com.scriptbasic.api.LeftValueList;
import com.scriptbasic.api.RightValue;
import com.scriptbasic.interfaces.*;

import java.lang.reflect.Method;

/**
 * The simplest implementation of the InterfaceHook interface. This implements
 * each of the methods of the interface, each doing nothing.
 * <p>
 * This hook is used to chain into the hook chain first so that hook classes
 * need not check if there is a next hook object in the chain.
 *
 * @author Peter Verhas
 * date Aug 15, 2012
 */
public class NullHook implements InterpreterHook {

    @Override
    public void setNext(final InterpreterHook next) {
    }

    @Override
    public void setInterpreter(final Interpreter interpreter) {
    }

}
