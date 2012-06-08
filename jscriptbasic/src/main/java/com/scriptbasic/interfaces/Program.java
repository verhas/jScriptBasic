package com.scriptbasic.interfaces;

import java.util.Collection;

/**
 * The program that was created by the syntax analyzer. This object is
 * state less, the actual runtime should contain all state information, global
 * variables, program counter, whatever it wants. The implementing class has to
 * be thread safe and immutable after it was created by the syntax analysis.
 * 
 * @author Peter Verhas
 * 
 */
public interface Program extends FactoryManaged {
    public AnalysisResult getStartCommand();

    public Collection<AnalysisResult> getCommands();

    public AnalysisResult getCommand(Integer programCounter);
}
