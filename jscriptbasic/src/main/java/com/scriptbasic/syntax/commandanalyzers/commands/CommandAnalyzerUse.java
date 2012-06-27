/**
 * 
 */
package com.scriptbasic.syntax.commandanalyzers.commands;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandUse;
import com.scriptbasic.executors.operators.ObjectFieldAccessOperator;
import com.scriptbasic.executors.rightvalues.VariableAccess;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;

/**
 * @author Peter Verhas
 * @date Jun 27, 2012
 * 
 */
public class CommandAnalyzerUse extends AbstractCommandAnalyzer {

    /**
     * Convert an expression of the form 'a.b.c.d' into a String.
     * 
     * @param expression
     * @return the string containing the dots and the identifiers
     * @throws AnalysisException
     *             when the expression does not match the format
     * 
     */
    private String convertToString(Expression expression)
            throws AnalysisException {
        if (expression instanceof VariableAccess) {
            return ((VariableAccess) expression).getVariableName();
        }
        if (expression instanceof ObjectFieldAccessOperator) {
            ObjectFieldAccessOperator ofao = (ObjectFieldAccessOperator) expression;
            return convertToString(ofao.getLeftOperand()) + "."
                    + convertToString(ofao.getRightOperand());
        }
        throw new GenericSyntaxException(
                "class, package or symbol name are not vaid in command USE");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        String className = convertToString(analyzeExpression());
        LexicalAnalyzer lexicalAnalyzer = FactoryUtilities
                .getLexicalAnalyzer(getFactory());
        LexicalElement lexicalElement = lexicalAnalyzer.get();
        if (lexicalElement == null || !lexicalElement.isSymbol()
                || !"from".equalsIgnoreCase(lexicalElement.getLexeme())) {
            throw new GenericSyntaxException(
                    "Keyword 'FROM' is missing in command 'USE'");
        }
        String packageName = convertToString(analyzeExpression());
        String aliasName = null;
        lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol()
                && "as".equalsIgnoreCase(lexicalElement.getLexeme())) {
            lexicalAnalyzer.get();
            aliasName = convertToString(analyzeExpression());
        } else {
            aliasName = className;
        }
        consumeEndOfLine();
        if (className.indexOf('.') != -1 || aliasName.indexOf('.') != -1) {
            throw new GenericSyntaxException(
                    "class name and alias name shoudl not contain dot in command USE");
        }
        final String fullClassName = packageName + "." + className;
        Class<?> klass = null;
        try {
            klass = Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
            throw new GenericSyntaxException(
                    "The class in the USE statement is not found.", e);
        }

        CommandUse node = new CommandUse();
        node.setKlass(klass);
        node.setAlias(aliasName);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer#getName()
     */
    @Override
    protected String getName() {
        return "USE";
    }

}
