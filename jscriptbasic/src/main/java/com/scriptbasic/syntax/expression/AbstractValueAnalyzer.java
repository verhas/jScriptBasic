package com.scriptbasic.syntax.expression;

import com.scriptbasic.command.executors.AbstractValue;
import com.scriptbasic.interfaces.Analyzer;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.SyntaxException;

public abstract class AbstractValueAnalyzer implements Analyzer {
    public abstract AbstractValue getResult();

    @Override
    public abstract Expression analyze() throws SyntaxException;

}
