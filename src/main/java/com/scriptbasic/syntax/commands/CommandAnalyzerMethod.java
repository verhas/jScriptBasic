package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandMethod;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.spi.Command;
import com.scriptbasic.utility.ExpressionUtility;
import com.scriptbasic.utility.KlassUtility;
import com.scriptbasic.utility.LexUtility;

import java.util.ArrayList;

/**
 * @author Peter Verhas
 * date Jun 27, 2012
 */
public class CommandAnalyzerMethod extends AbstractCommandAnalyzer {

    public CommandAnalyzerMethod(final Context ctx) {
        super(ctx);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        final var methodName = ExpressionUtility
                .convertToString(analyzeExpression());
        LexUtility.checkLexeme(ctx.lexicalAnalyzer, "from",
                "Keyword 'FROM' is missing in command 'METHOD'");
        final var className = ExpressionUtility
                .convertToString(analyzeExpression());

        LexUtility.checkLexeme(ctx.lexicalAnalyzer, ScriptBasicKeyWords.KEYWORD_IS,
                "Keyword 'IS' is missing in command 'METHOD'");
        LexUtility.checkLexeme(ctx.lexicalAnalyzer, "(",
                "'(' is missing in command 'METHOD' after the keyword 'IS'");

        final var argExpressions = ctx.expressionListAnalyzer.analyze();
        LexUtility.checkLexeme(ctx.lexicalAnalyzer, ")",
                "')' is missing in command 'METHOD'");
        final String alias;
        if (LexUtility.isLexeme(ctx.lexicalAnalyzer, "use")) {
            LexUtility.checkLexeme(ctx.lexicalAnalyzer, "as",
                    "Keyword 'AS' is missung after 'USE in command 'METHOD'");
            alias = ExpressionUtility.convertToString(analyzeExpression());
        } else {
            alias = methodName;
        }
        final ArrayList<Class<?>> argClasses = new ArrayList<>();
        for (final Expression expression : argExpressions) {
            final var argClassName = ExpressionUtility.convertToString(expression);
            argClasses.add(KlassUtility.forNameEx(argClassName));
        }
        final var node = new CommandMethod();
        node.setArgumentTypes(argClasses.toArray(new Class<?>[0]));
        node.setKlass(KlassUtility.forNameEx(className));
        node.setMethodName(methodName);
        node.setAlias(alias);
        consumeEndOfLine();

        return node;
    }

}
