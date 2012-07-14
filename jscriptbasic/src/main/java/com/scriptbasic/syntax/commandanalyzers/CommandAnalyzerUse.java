/**
 * 
 */
package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandUse;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.utility.ExpressionUtility;
import com.scriptbasic.utility.KlassUtility;
import com.scriptbasic.utility.LexUtility;

/**
 * @author Peter Verhas
 * @date Jun 27, 2012
 * 
 */
public class CommandAnalyzerUse extends AbstractCommandAnalyzer {

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        String className = ExpressionUtility
                .convertToString(analyzeExpression());
        LexUtility.checkLexeme(getFactory(), "from",
                "Keyword 'FROM' is missing in command 'USE'");
        String packageName = ExpressionUtility
                .convertToString(analyzeExpression());
        String aliasName = null;
        if (LexUtility.isLexeme(getFactory(), "as")) {
            aliasName = ExpressionUtility.convertToString(analyzeExpression());
        } else {
            aliasName = className;
        }
        consumeEndOfLine();
        if (className.indexOf('.') != -1 || aliasName.indexOf('.') != -1) {
            throw new GenericSyntaxException(
                    "class name and alias name should not contain dot in command USE");
        }
        final String fullClassName = packageName + "." + className;
        Class<?> klass = null;
        try {
            klass = KlassUtility.forNameEx(fullClassName);
        } catch (GenericSyntaxException e) {
            throw new GenericSyntaxException(
                    "The class in the USE statement is not found.", e);
        }

        CommandUse node = new CommandUse();
        node.setKlass(klass);
        node.setAlias(aliasName);
        return node;
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
