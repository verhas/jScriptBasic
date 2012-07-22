/**
 * 
 */
package com.scriptbasic.executors;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.interfaces.BuildableProgram;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.HierarchicalVariableMap;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.memory.MixedBasicVariableMap;
import com.scriptbasic.utility.MethodRegisterUtility;
import com.scriptbasic.utility.RightValueUtility;
import com.scriptbasic.utility.RuntimeUtility;

/**
 * @author Peter Verhas
 * @date June 22, 2012
 * 
 */
public final class BasicExtendedInterpreter implements ExtendedInterpreter {
    private BasicExtendedInterpreter() {
    }

    private BuildableProgram program;
    private java.io.Reader reader;
    private java.io.Writer writer;
    private java.io.Writer errorWriter;

    /**
     * @return the reader
     */
    @Override
    public java.io.Reader getReader() {
        return reader;
    }

    /**
     * @param reader
     *            the reader to set
     */
    @Override
    public void setReader(final java.io.Reader reader) {
        this.reader = reader;
    }

    /**
     * @return the writer
     */
    @Override
    public java.io.Writer getWriter() {
        return writer;
    }

    /**
     * @param writer
     *            the writer to set
     */
    @Override
    public void setWriter(final java.io.Writer writer) {
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
     * @param errorWriter
     *            the errorWriter to set
     */
    @Override
    public void setErrorWriter(final Writer errorWriter) {
        this.errorWriter = errorWriter;
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

    private final MixedBasicVariableMap variables = new MixedBasicVariableMap();

    @Override
    public HierarchicalVariableMap getVariables() {
        return variables;
    }

    private Command nextCommand;
    private Command currentCommand;

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
        MethodRegisterUtility.registerFunctions(klass, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Interpreter#execute()
     */
    @Override
    public void execute() throws ExecutionException {
        final Command command = program.getStartCommand();
        MethodRegisterUtility.registerFunctions(RuntimeUtility.class, this);
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
        Command command = startCommand;
        while (command != null) {
            nextCommand = command.getNextCommand();
            currentCommand = command;
            command.execute(this);
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
    @SuppressWarnings("unchecked")
    @Override
    public Object getVariable(final String name) throws ExecutionException {
        final RightValue rightValue = getVariables().getVariableValue(name);
        return rightValue == null ? null
                : ((AbstractPrimitiveRightValue<Object>) rightValue).getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Interpreter#call(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public Object call(final String functionName, final Object[] arguments) {
        // TODO implement calling a function from an already executed BASIC
        // program
        return null;
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

    private final Map<String, Object> interpreterStateMap = new HashMap<String, Object>();

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#getMap()
     */
    @Override
    public Map<String, Object> getMap() {
        return interpreterStateMap;
    }

    // private Factory factory;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.FactoryManaged#setFactory(com.scriptbasic.
     * interfaces.Factory)
     */
    @Override
    public void setFactory(final Factory factory) {
        // this.factory = factory;
    }

    private final Map<String, Class<?>> useMap = new HashMap<>();

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#getUseMap()
     */
    @Override
    public Map<String, Class<?>> getUseMap() {
        return useMap;
    }

    private final MethodRegistry methodRegistry = new MethodRegistry();

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
        return methodRegistry.getJavaMethod(klass, methodName);
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
            final String methodName, final Class<?>[] argumentTypes) {
        methodRegistry.registerJavaMethod(alias, klass, methodName,
                argumentTypes);

    }

    private final Stack<Command> commandStack = new Stack<>();
    private final Stack<Command> nextCommandStack = new Stack<>();

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#push(com.scriptbasic.
     * interfaces.Command)
     */
    @Override
    public void push(final Command command) {
        commandStack.push(command);
        nextCommandStack.push(nextCommand);
        getVariables().newFrame();
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
        getVariables().dropFrame();
        nextCommand = nextCommandStack.pop();
        return commandStack.pop();
    }

    private RightValue returnValue;

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

}
