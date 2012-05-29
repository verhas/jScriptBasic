package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.Analyzer;
import com.scriptbasic.interfaces.SyntaxAnalyzer;
import com.scriptbasic.interfaces.SyntaxException;

public abstract class AbstractAnalyzer implements Analyzer {

    SyntaxAnalyzer syntaxAnalyzer;

    public void setSyntaxAnalyzer(SyntaxAnalyzer sa) {
        syntaxAnalyzer = sa;
    }

}
