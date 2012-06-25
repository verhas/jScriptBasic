/**
 * 
 */
package com.scriptbasic.executors;

import java.util.HashMap;
import java.util.Map;

import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.interfaces.BuildableProgram;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.memory.MixedBasicVariableMap;

/**
 * @author Peter Verhas
 * @date June 22, 2012
 * 
 */
public class BasicExtendedInterpreter implements ExtendedInterpreter {
    private BasicExtendedInterpreter() {
    }

    private BuildableProgram program;

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

    final private MixedBasicVariableMap variables = new MixedBasicVariableMap();

    @Override
    public MixedBasicVariableMap getVariables() {
        return variables;
    }

    private Command nextCommand;

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Interpreter#execute()
     */
    @Override
    public void execute() throws ExecutionException {
        Command command = program.getStartCommand();
        while (command != null) {
            nextCommand = command.getNextCommand();
            command.execute(this);
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
    public void setVariable(String name, Object value) {
        // TODO Auto-generated method stub
        throw new RuntimeException("not yet implemented");
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
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.ExtendedInterpreter#getProgramCounter()
     */
    @Override
    public Integer getProgramCounter() {
        // TODO Auto-generated method stub
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

}
