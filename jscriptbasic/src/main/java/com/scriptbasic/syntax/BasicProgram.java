package com.scriptbasic.syntax;

import java.util.ArrayList;
import java.util.Collection;

import com.scriptbasic.interfaces.AnalysisResult;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.Program;

public class BasicProgram implements Program {
    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
	public void setFactory(Factory factory) {
        this.factory = factory;
    }

    private BasicProgram() {
    }

    private final ArrayList<AnalysisResult> commandAnalyzers = new ArrayList<AnalysisResult>();

    public void addCommand(final AnalysisResult analysisResult) {
        this.commandAnalyzers.add(analysisResult);
    }

    @Override
    public AnalysisResult getStartCommand() {

        return null;
    }

    @Override
    public Collection<AnalysisResult> getCommands() {

        return null;
    }

    @Override
    public AnalysisResult getCommand(final Integer programCounter) {

        return null;
    }

}
