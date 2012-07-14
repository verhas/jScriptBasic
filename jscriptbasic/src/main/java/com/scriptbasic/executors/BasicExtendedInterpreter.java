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
import com.scriptbasic.utility.RightValueUtility;

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
    public void setReader(java.io.Reader reader) {
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
    public void setWriter(java.io.Writer writer) {
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
    public void setErrorWriter(Writer errorWriter) {
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
    public void setProgram(BuildableProgram buildableProgram) {
        this.program = buildableProgram;
    }

    private final MixedBasicVariableMap variables = new MixedBasicVariableMap();

    @Override
    public HierarchicalVariableMap getVariables() {
        return variables;
    }

    private Command nextCommand;
    private Command currentCommand;

    private Map<String, CommandSub> subroutineMap = new HashMap<>();

    /**
     * Collect all the subroutines and build the subroutine map to ease the
     * location of subroutines based on name.
     * 
     * @param startCommand
     */
    private void collectSubroutines(final Command startCommand) {
        for (Command command = startCommand; command != null; command = command
                .getNextCommand()) {
            if (command instanceof CommandSub) {
                subroutineMap.put(((CommandSub) command).getSubName(),
                        (CommandSub) command);
            }
        }
    }

    @Override
    public CommandSub getSubroutine(String name) {
        return subroutineMap.get(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Interpreter#execute()
     */
    @Override
    public void execute() throws ExecutionException {
        Command command = program.getStartCommand();
        collectSubroutines(command);
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
    public void setVariable(String name, Object value)
            throws ExecutionException {
        RightValue rightValue = RightValueUtility.createRightValue(value);
        getVariables().setVariable(name, rightValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Interpreter#getVariable(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object getVariable(String name) throws ExecutionException {
        RightValue rightValue = getVariables().getVariableValue(name);
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
    public Object call(String functionName, Object[] arguments) {
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
    public void setNextCommand(Command nextCommand) {
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

    private Map<String, Class<?>> useMap = new HashMap<>();

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#getUseMap()
     */
    @Override
    public Map<String, Class<?>> getUseMap() {
        return useMap;
    }

    private MethodRegistry methodRegistry = new MethodRegistry();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.ExtendedInterpreter#getJavaMethod(java.lang
     * .Class, java.lang.String)
     */
    @Override
    public Method getJavaMethod(Class<?> klass, String methodName)
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
    public void registerJavaMethod(String alias, Class<?> klass,
            String methodName, Class<?>[] argumentTypes) {
        methodRegistry.registerJavaMethod(alias, klass, methodName,
                argumentTypes);

    }

    private Stack<Command> commandStack = new Stack<>();
    private Stack<Command> nextCommandStack = new Stack<>();
    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#push(com.scriptbasic.
     * interfaces.Command)
     */
    @Override
    public void push(Command command) {
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
    public void setReturnValue(RightValue returnValue) {
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
