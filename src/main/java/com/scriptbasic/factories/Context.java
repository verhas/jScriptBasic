package com.scriptbasic.factories;

import com.scriptbasic.interfaces.*;

public class Context {
    public Configuration configuration;
    public TagAnalyzer tagAnalyzer;
    public SyntaxAnalyzer syntaxAnalyzer;
    public ExtendedInterpreter interpreter;
    public SimpleLeftValueListAnalyzer simpleLeftValueListAnalyzer;
    public SimpleLeftValueAnalyzer simpleLeftValueAnalyzer;
    public ExpressionAnalyzer expressionAnalyzer;
    public ExpressionListAnalyzer expressionListAnalyzer;
    public NestedStructureHouseKeeper nestedStructureHouseKeeper;
    public LineOrientedLexicalAnalyzer lexicalAnalyzer;
    public LeftValueAnalyzer leftValueAnalyzer;

}
