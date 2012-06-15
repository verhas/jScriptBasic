package com.scriptbasic.interfaces;

/**
 * A {@code BuildableProgram} is a {@code Program} that also provides methods
 * needed to build up the program code during the BASIC source code analysis.
 * 
 * @author Peter Verhas
 * @date June 15, 2012
 * 
 */
public interface BuildableProgram extends FactoryManaged, Program {

    void addCommand(Command command);
}
