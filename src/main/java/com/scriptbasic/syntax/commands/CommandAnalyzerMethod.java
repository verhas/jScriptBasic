package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandMethod;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
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
        final String methodName = getMethodName();

        LexUtility.checkLexeme(ctx.lexicalAnalyzer, ScriptBasicKeyWords.KEYWORD_FROM,
                "Keyword 'FROM' is missing in command 'METHOD'");

        final String className = getClassName();

        LexUtility.checkLexeme(ctx.lexicalAnalyzer, ScriptBasicKeyWords.KEYWORD_IS,
                "Keyword 'IS' is missing in command 'METHOD'");
        LexUtility.checkLexeme(ctx.lexicalAnalyzer, "(",
                "'(' is missing in command 'METHOD' after the keyword 'IS'");

        final var argClasses = getArgClasses();

        LexUtility.checkLexeme(ctx.lexicalAnalyzer, ")",
                "')' is missing in command 'METHOD'");

        final String alias = getAlias(methodName);

        final var node = new CommandMethod();
        node.setArgumentTypes(argClasses.toArray(new Class<?>[0]));
        node.setKlass(KlassUtility.forNameEx(className));
        node.setMethodName(methodName);
        node.setAlias(alias);
        consumeEndOfStatement();

        return node;
    }

    private String getAlias(String methodName) throws AnalysisException {
        final String alias;
        if (LexUtility.isLexeme(ctx.lexicalAnalyzer, ScriptBasicKeyWords.KEYWORD_USE)) {
            LexUtility.checkLexeme(ctx.lexicalAnalyzer, ScriptBasicKeyWords.KEYWORD_AS,
                    "Keyword 'AS' is missung after 'USE in command 'METHOD'");
            alias = getString();
        } else {
            alias = methodName;
        }
        return alias;
    }

    private String getClassName() throws AnalysisException {
        return getString();
    }

    private String getString() throws AnalysisException {
        return ExpressionUtility
                    .convertToString(analyzeExpression());
    }

    private String getMethodName() throws AnalysisException {
        return getString();
    }

    private ArrayList<Class<?>> getArgClasses() throws AnalysisException {
        final var argExpressions = ctx.expressionListAnalyzer.analyze();
        final ArrayList<Class<?>> argClasses = new ArrayList<>();
        for (final Expression expression : argExpressions) {
            final var argClassName = ExpressionUtility.convertToString(expression);
            argClasses.add(KlassUtility.forNameEx(argClassName));
        }
        return argClasses;
    }

}
