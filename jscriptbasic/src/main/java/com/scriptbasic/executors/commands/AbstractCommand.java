package com.scriptbasic.executors.commands;
import java.security.Permission;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Executor;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.NestedStructure;
import com.scriptbasic.security.CommandPermission;
public abstract class AbstractCommand implements Executor, Command,
        NestedStructure {
    
    private String fileName;
    private int lineNumber;
    private int position;
    
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * @return the lineNumber
     */
    public int getLineNumber() {
        return lineNumber;
    }
    /**
     * @param lineNumber the lineNumber to set
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }
    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }
    @Override
    public abstract void execute(ExtendedInterpreter interpreter)
            throws ExecutionException;

    private Permission permission = new CommandPermission(this
            .getClass());
    private void assertCommandSecurity() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(permission);
        }
    }
    public void checkedExecute(ExtendedInterpreter interpreter)
            throws ExecutionException {
        assertCommandSecurity();
        execute(interpreter);
    }
    private Command nextCommand;
    /**
     * Get the next command that has to be executed unless some condition alters
     * this, like in case of If, While and similar.
     *
     * @return
     */
    @Override
    public Command getNextCommand() {
        return nextCommand;
    }
    public void setNextCommand(final Command nextCommand) {
        this.nextCommand = nextCommand;
    }
}