package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.interfaces.Command;
import com.scriptbasic.syntax.AbstractAnalyzer;

public abstract class AbstractCommandAnalyzer extends AbstractAnalyzer {

    public abstract Command getCommand();

}
