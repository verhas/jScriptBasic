package com.scriptbasic.executors;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.factories.Context;
import com.scriptbasic.hooks.NullHook;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.memory.MixedBasicVariableMap;
import com.scriptbasic.utility.*;
import com.scriptbasic.utility.functions.BasicRuntimeFunctionRegisterer;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Peter Verhas date June 22, 2012
 */
public final class BasicExtendedInterpreter implements ExtendedInterpreter {

    private final MixedBasicVariableMap variables = new MixedBasicVariableMap();
    private final Map<String, Object> interpreterStateMap = new HashMap<>();
    private final Map<String, Class<?>> useMap = new HashMap<>();
    private final MethodRegistry basicMethodRegistry = new BasicMethodRegistry();
    private final Stack<Command> commandStack = new Stack<>();
    private final Stack<Command> nextCommandStack = new Stack<>();
    private final Context ctx;
    private BuildableProgram program;
    private Reader reader;
    private Writer writer;
    private Writer errorWriter;
    private InterpreterHook hook = null;
    private InterpreterHook hookedHook = null;
    private Command nextCommand;
    private Command currentCommand;
    private boolean executePreTask = true;
    private RightValue returnValue;

    public BasicExtendedInterpreter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public InterpreterHook getHook() {
        return hook;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#disableHook()
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
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#enableHook()
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
    public Reader getReader() {
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
    public Writer getWriter() {
        return writer;
    }

    /**
     * @param writer the writer to set
     */
    @Override
    public void setOutput(final Writer writer) {
        this.writer = writer;
    }

    /**
     * @return the errorWriter
     */
    @Override
    public Writer getErrorWriter() {
        return errorWriter;
    }

    /**
     * @param errorWriter the errorWriter to set
     */
    @Override
    public void setError(final Writer errorWriter) {
        this.errorWriter = errorWriter;
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
        try {
            MethodRegisterUtility.registerFunctions(klass, this);
        } catch (BasicRuntimeException e) {
            throw new BasicInterpreterInternalError(
                    "Registering functions from class '"
                            + klass
                            + "' caused exception. Probably double defining a function alias. "
                            + "Since this declared in Java code and not in BASIC this is an internal error of "
                            + "the embedding application. For more detail have a look at the exception that caused this.",
                    e);
        }
    }

    private void preExecuteTask() throws ExecutionException {
        if (executePreTask) {
            if (program == null) {
                throw new BasicRuntimeException("Program code was not loaded");
            }
            BasicRuntimeFunctionRegisterer.registerBasicRuntimeFunctions(this);
            registerHook(new NullHook());
            HookRegisterUtility.registerHooks(this);
            if (hook != null) {
                hook.init();
            }
            executePreTask = false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#execute()
     */
    @Override
    public void execute() throws ExecutionException {
        preExecuteTask();
        final Command command = program.getStartCommand();
        execute(command);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.ExtendedInterpreter#execute(com.scriptbasic
     * .interfaces.Command)
     */
    @Override
    public void execute(final Command startCommand) throws ExecutionException {
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
            throws ExecutionException {
        final RightValue rightValue = RightValueUtility.createRightValue(value);
        getVariables().setVariable(name, rightValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Interpreter#getVariable(java.lang.String)
     */
    @Override
    public Object getVariable(final String name) throws ExecutionException {
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
            throws ExecutionException {
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
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#getProgram()
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
     * com.scriptbasic.interfaces.ExtendedInterpreter#delayedSetProgramCounter
     * (java.lang.Integer)
     */
    @Override
    public void setNextCommand(final Command nextCommand) {
        this.nextCommand = nextCommand;
    }

    @Override
    public Command getCurrentCommand() {
        return currentCommand;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#getMap()
     */
    @Override
    public Map<String, Object> getMap() {
        return interpreterStateMap;
    }


    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#getUseMap()
     */
    @Override
    public Map<String, Class<?>> getUseMap() {
        return useMap;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.ExtendedInterpreter#getJavaMethod(java.lang
     * .Class, java.lang.String)
     */
    @Override
    public Method getJavaMethod(final Class<?> klass, final String methodName)
            throws ExecutionException {
        return basicMethodRegistry.getJavaMethod(klass, methodName);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.ExtendedInterpreter#registerJavaMethod(java
     * .lang.String, java.lang.Class, java.lang.String, java.lang.Class<?>[])
     */
    @Override
    public void registerJavaMethod(final String alias, final Class<?> klass,
                                   final String methodName, final Class<?>[] argumentTypes)
            throws BasicRuntimeException {
        if (hook != null) {
            hook.beforeRegisteringJavaMethod(alias, klass, methodName,
                    argumentTypes);
        }
        basicMethodRegistry.registerJavaMethod(alias, klass, methodName,
                argumentTypes);

    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#push(com.scriptbasic.
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
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#push()
     */
    public void push() {
        push(currentCommand);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#pop()
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
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#getReturnValue()
     */
    @Override
    public RightValue getReturnValue() {
        return returnValue;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.ExtendedInterpreter#setReturnValue(com.scriptbasic
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
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#getConfiguration()
     */
    @Override
    public Configuration getConfiguration() {
        return ctx.configuration;
    }

}
