package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.Analyzer;
import com.scriptbasic.interfaces.SyntaxAnalyzer;

public abstract class AbstractAnalyzer implements Analyzer {

    private SyntaxAnalyzer syntaxAnalyzer;

    public SyntaxAnalyzer getSyntaxAnalyzer() {
        return syntaxAnalyzer;
    }

    public void setSyntaxAnalyzer(SyntaxAnalyzer sa) {
        syntaxAnalyzer = sa;
    }

}
