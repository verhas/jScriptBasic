/**
 * 
 */
package com.scriptbasic.syntax.commandanalyzers;

import java.util.ArrayList;

import com.scriptbasic.executors.commands.CommandMethod;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
import com.scriptbasic.utility.ExpressionUtility;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.KlassUtility;
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

        ExpressionList argExpressions = FactoryUtility
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
            argClasses.add(KlassUtility.forNameEx(argClassName));
        }
        CommandMethod node = new CommandMethod();
        node.setArgumentTypes(argClasses.toArray(new Class<?>[0]));
        node.setKlass(KlassUtility.forNameEx(className));
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
