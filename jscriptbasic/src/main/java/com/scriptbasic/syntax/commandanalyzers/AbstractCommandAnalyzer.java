package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.syntax.AbstractAnalyzer;

public abstract class AbstractCommandAnalyzer extends AbstractAnalyzer implements CommandAnalyzer {

    @Override
    public abstract Command getCommand();

}
