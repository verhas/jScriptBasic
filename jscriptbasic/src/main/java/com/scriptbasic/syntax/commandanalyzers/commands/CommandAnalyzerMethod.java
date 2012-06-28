/**
 * 
 */
package com.scriptbasic.syntax.commandanalyzers.commands;

import java.util.ArrayList;

import com.scriptbasic.executors.commands.CommandMethod;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
import com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer;
import com.scriptbasic.utility.ExpressionUtility;
import com.scriptbasic.utility.FactoryUtilities;
import com.scriptbasic.utility.Klass;
import com.scriptbasic.utility.LexUtility;

/**
 * @author Peter Verhas
 * @date Jun 27, 2012
 * 
 */
public class CommandAnalyzerMethod extends AbstractCommandAnalyzer {

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        String methodName = ExpressionUtility
                .convertToString(analyzeExpression());
        LexUtility.checkLexeme(getFactory(), "from",
                "Keyword 'IS' is missing in command 'METHOD'");
        String className = ExpressionUtility
                .convertToString(analyzeExpression());

        LexUtility.checkLexeme(getFactory(), "is",
                "Keyword 'IS' is missing in command 'METHOD'");
        LexUtility.checkLexeme(getFactory(), "(",
                "'(' is missing in command 'METHOD' after the keyword 'IS'");

        ExpressionList argExpressions = FactoryUtilities
                .getExpressionListAnalyzer(getFactory()).analyze();
        LexUtility.checkLexeme(getFactory(), ")",
                "')' is missing in command 'METHOD'");
        String alias = null;
        if (LexUtility.isLexeme(getFactory(), "use")) {
            LexUtility.checkLexeme(getFactory(), "as",
                    "Keyword 'AS' is missung after 'USE in command 'METHOD'");
            alias = ExpressionUtility.convertToString(analyzeExpression());
        } else {
            alias = methodName;
        }
        ArrayList<Class<?>> argClasses = new ArrayList<>();
        for (Expression expression : argExpressions) {
            String argClassName = ExpressionUtility.convertToString(expression);
            argClasses.add(Klass.forName(argClassName));
        }
        CommandMethod node = new CommandMethod();
        node.setArgumentTypes(argClasses.toArray(new Class<?>[0]));
        node.setKlass(Klass.forName(className));
        node.setMethodName(methodName);
        node.setAlias(alias);
        consumeEndOfLine();

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
        return "METHOD";
    }

}
