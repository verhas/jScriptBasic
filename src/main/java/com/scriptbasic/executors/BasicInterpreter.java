package com.scriptbasic.executors;

import com.scriptbasic.api.*;
import com.scriptbasic.context.Context;
import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.hooks.NullHook;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.memory.MixedBasicVariableMap;
import com.scriptbasic.spi.Command;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.InterpreterHook;
import com.scriptbasic.spi.RightValue;
import com.scriptbasic.utility.*;
import com.scriptbasic.utility.functions.BasicRuntimeFunctionRegisterer;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Peter Verhas date June 22, 2012
 */
public final class BasicInterpreter implements Interpreter {

    private final MixedBasicVariableMap variables = new MixedBasicVariableMap();
    private final Map<String, Object> interpreterStateMap = new HashMap<>();
    private final Map<String, Class<?>> useMap = new HashMap<>();
    private final MethodRegistry basicMethodRegistry = new BasicMethodRegistry();
    private final Stack<Command> commandStack = new Stack<>();
    private final Stack<Command> nextCommandStack = new Stack<>();
    private final Context ctx;
    private final List<Class<?>> deferredFunctionsRegistrations = new LinkedList<>();
    private final List<DeferredJavaMethodRegistration> deferredJavaMethodRegistrations = new LinkedList<>();
    private BuildableProgram program;
    private Reader reader;
    private Writer output;
    private Writer error;
    private InterpreterHook hook = null;
    private InterpreterHook hookedHook = null;
    private Command nextCommand;
    private Command currentCommand;
    private boolean executePreTask = true;
    private RightValue returnValue;

    /**
     * Create a new interpreter using the context. Also register the null hook, so that other hooks should not
     * worry about the 'next hook' value, it is guaranteed to be not null.
     *
     * @param ctx
     */
    public BasicInterpreter(final Context ctx) {
        this.ctx = ctx;
        registerHook(new NullHook());
    }

    @Override
    public InterpreterHook getHook() {
        return hook;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#disableHook()
     */
    @Override
    public void disableHook() {
        if (hook != null) {
            hookedHook = hook;
            hook = null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#enableHook()
     */
    @Override
    public void enableHook() {
        if (hookedHook != null) {
            hook = hookedHook;
            hookedHook = null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#registerHook(com.scriptbasic.
     * interfaces.InterpreterHook)
     */
    @Override
    public void registerHook(final InterpreterHook hook) {
        hook.setNext(this.hook);
        hook.setInterpreter(this);
        this.hook = hook;
    }

    /**
     * @return the reader
     */
    @Override
    public Reader getInput() {
        return reader;
    }

    /**
     * @param reader the reader to set
     */
    @Override
    public void setInput(final Reader reader) {
        this.reader = reader;
    }

    /**
     * @return the writer
     */
    @Override
    public Writer getOutput() {
        return output;
    }

    /**
     * @param writer the writer to set
     */
    @Override
    public void setOutput(final Writer writer) {
        this.output = writer;
    }

    /**
     * @return the error
     */
    @Override
    public Writer getErrorOutput() {
        return error;
    }

    /**
     * @param errorWriter the error to set
     */
    @Override
    public void setErrorOutput(final Writer errorWriter) {
        this.error = errorWriter;
    }

    @Override
    public HierarchicalVariableMap getVariables() {
        return variables;
    }

    @Override
    public CommandSub getSubroutine(final String name) {
        final Command command = program.getNamedCommand(name);
        if (command instanceof CommandSub) {
            return (CommandSub) command;
        }
        return null;
    }

    @Override
    public void registerFunctions(final Class<?> klass) {
        if (executePreTask) {
            deferredFunctionsRegistrations.add(klass);
        } else {
            try {
                MethodRegisterUtility.registerFunctions(klass, this);
            } catch (final BasicRuntimeException e) {
                throw new BasicInterpreterInternalError(
                        "Registering functions from class '"
                                + klass
                                + "' caused exception. Probably double defining a function alias. "
                                + "Since this declared in Java code and not in BASIC this is an internal error of "
                                + "the embedding application. For more detail have a look at the exception that caused this.",
                        e);
            }
        }
    }

    private void preExecuteTask() throws ScriptBasicException {
        if (executePreTask) {
            executePreTask = false;
            if (program == null) {
                throw new BasicRuntimeException("Program code was not loaded");
            }
            HookRegisterUtility.registerHooks(this);
            if (hook != null) {
                hook.init();
            }
            deferredFunctionsRegistrations.stream().forEach(this::registerFunctions);
            deferredJavaMethodRegistrations.stream().forEach(this::registerJavaMethod);
            BasicRuntimeFunctionRegisterer.registerBasicRuntimeFunctions(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#execute()
     */
    @Override
    public void execute() throws ScriptBasicException {
        preExecuteTask();
        final Command command = program.getStartCommand();
        hook.beforeExecute();
        execute(command);
        hook.afterExecute();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.Interpreter#execute(com.scriptbasic
     * .interfaces.Command)
     */
    @Override
    public void execute(final Command startCommand) throws ScriptBasicException {
        preExecuteTask();
        Command command = startCommand;
        while (command != null) {
            nextCommand = command.getNextCommand();
            currentCommand = command;
            if (hook != null) {
                hook.beforeExecute(command);
            }
            command.checkedExecute(this);
            if (hook != null) {
                hook.afterExecute(command);
            }
            currentCommand = null;
            command = nextCommand;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#setVariable(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public void setVariable(final String name, final Object value)
            throws ScriptBasicException {
        final RightValue rightValue = RightValueUtility.createRightValue(value);
        getVariables().setVariable(name, rightValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#getVariable(java.lang.String)
     */
    @Override
    public Object getVariable(final String name) throws ScriptBasicException {
        return CastUtility.toObject(getVariables().getVariableValue(name));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#call(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public Object call(final String functionName, final Object[] arguments)
            throws ScriptBasicException {
        preExecuteTask();
        final CommandSub commandSub = getSubroutine(functionName);
        if (commandSub == null) {
            throw new BasicRuntimeException("There is no such subroutine '"
                    + functionName + "'");
        }
        return CastUtility.toObject(ExpressionUtility.callBasicFunction(this,
                RightValueUtility.createRightValues(arguments), commandSub,
                functionName));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#getProgram()
     */
    @Override
    public BuildableProgram getProgram() {
        return this.program;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.Interpreter#setProgram(com.scriptbasic.interfaces
     * .BuildableProgram)
     */
    @Override
    public void setProgram(final BuildableProgram buildableProgram) {
        this.program = buildableProgram;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.Interpreter#delayedSetProgramCounter
     * (java.lang.Integer)
     */
    @Override
    public void setNextCommand(final Command nextCommand) {
        this.nextCommand = nextCommand;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#getMap()
     */
    @Override
    public Map<String, Object> getMap() {
        return interpreterStateMap;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#getUseMap()
     */
    @Override
    public Map<String, Class<?>> getUseMap() {
        return useMap;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.Interpreter#getJavaMethod(java.lang
     * .Class, java.lang.String)
     */
    @Override
    public Method getJavaMethod(final Class<?> klass, final String methodName)
            throws ScriptBasicException {
        return basicMethodRegistry.getJavaMethod(klass, methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.Interpreter#registerJavaMethod(java
     * .lang.String, java.lang.Class, java.lang.String, java.lang.Class<?>[])
     */
    @Override
    public void registerJavaMethod(final String alias, final Class<?> klass,
                                   final String methodName, final Class<?>[] argumentTypes)
            throws BasicRuntimeException {
        if (executePreTask) {
            deferredJavaMethodRegistrations.add(DeferredJavaMethodRegistration.of(alias, klass, methodName, argumentTypes));
        } else {
            hook.beforeRegisteringJavaMethod(alias, klass, methodName, argumentTypes);
            basicMethodRegistry.registerJavaMethod(alias, klass, methodName, argumentTypes);
        }

    }

    private void registerJavaMethod(final DeferredJavaMethodRegistration registration) {
        try {
            registerJavaMethod(registration.alias, registration.klass, registration.methodName, registration.argumentTypes);
        } catch (BasicRuntimeException e) {
            throw new BasicInterpreterInternalError(
                    "Registering method " +
                            registration.klass.getName() + "." + registration.methodName +
                            "' caused exception." +
                            " Since this registration was executed from Java code before starting the" +
                            " actual BASIC program this is likely to be internal error of" +
                            " the embedding application. For more detail have a look at the exception that caused this.",
                    e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#push(com.scriptbasic.
     * interfaces.Command)
     */
    @Override
    public void push(final Command command) {
        if (hook != null) {
            hook.beforePush(command);
        }
        commandStack.push(command);
        nextCommandStack.push(nextCommand);
        getVariables().newFrame();
        if (hook != null) {
            hook.afterPush(command);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#push()
     */
    public void push() {
        push(currentCommand);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#pop()
     */
    @Override
    public Command pop() {
        if (hook != null) {
            hook.beforePop();
        }
        getVariables().dropFrame();
        nextCommand = nextCommandStack.pop();
        final Command command = commandStack.pop();
        if (hook != null) {
            hook.afterPop(command);
        }
        return command;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#getReturnValue()
     */
    @Override
    public RightValue getReturnValue() {
        return returnValue;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.Interpreter#setReturnValue(com.scriptbasic
     * .interfaces.RightValue)
     */
    @Override
    public void setReturnValue(final RightValue returnValue) {
        this.returnValue = returnValue;
        if (hook != null) {
            hook.setReturnValue(returnValue);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#getConfiguration()
     */
    @Override
    public Configuration getConfiguration() {
        return ctx.configuration;
    }

    private static class DeferredJavaMethodRegistration {
        String alias;
        Class<?> klass;
        String methodName;
        Class<?>[] argumentTypes;

        static DeferredJavaMethodRegistration of(String alias,
                                                 Class<?> klass,
                                                 String methodName,
                                                 Class<?>[] argumentTypes
        ) {
            DeferredJavaMethodRegistration it = new DeferredJavaMethodRegistration();
            it.alias = alias;
            it.klass = klass;
            it.methodName = methodName;
            it.argumentTypes = argumentTypes;
            return it;
        }
    }

}
