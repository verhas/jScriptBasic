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

    /**
     * Calling this method signals that all the commands are added to the
     * program. This method has to perform all the polishing of the built
     * program that are to be done after the commands are analyzed. These
     * include:
     * <ul>
     * <li>optimizing expressions
     * <li>recalculating if/elseif/else/endif jumps
     * <li>building subroutine symbol table
     * <li>checking syntax analysis rules that apply to the whole program and
     * are easier to implement this way than keeping different state
     * informations during the syntax analysis, which is line oriented. For
     * example checking that all LOCAL and GLOBAL declarations in a SUB are
     * before any executable statement, subs are not nested...
     * </ul>
     * 
     * @throws AnalysisException
     *             when the postprocessing discovers syntax errors
     */
    void postprocess() throws AnalysisException;
}
