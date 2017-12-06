package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandUse;
import com.scriptbasic.context.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.utility.ExpressionUtility;
import com.scriptbasic.utility.KlassUtility;
import com.scriptbasic.utility.LexUtility;

/**
 * @author Peter Verhas
 * date Jun 27, 2012
 */
public class CommandAnalyzerUse extends AbstractCommandAnalyzer {

    public CommandAnalyzerUse(final Context ctx) {
        super(ctx);
    }

    /*
         * (non-Javadoc)
         *
         * @see com.scriptbasic.interfaces.Analyzer#analyze()
         */
    @Override
    public Command analyze() throws AnalysisException {
        final String className = ExpressionUtility
                .convertToString(analyzeExpression());
        LexUtility.checkLexeme(ctx.lexicalAnalyzer, "from",
                "Keyword 'FROM' is missing in command 'USE'");
        final String packageName = ExpressionUtility
                .convertToString(analyzeExpression());
        final String aliasName;
        if (LexUtility.isLexeme(ctx.lexicalAnalyzer, "as")) {
            aliasName = ExpressionUtility.convertToString(analyzeExpression());
        } else {
            aliasName = className;
        }
        consumeEndOfLine();
        if (className.indexOf('.') != -1 || aliasName.indexOf('.') != -1) {
            throw new BasicSyntaxException(
                    "class name and alias name should not contain dot in command USE");
        }
        final String fullClassName = packageName + "." + className;
        final Class<?> klass;
        try {
            klass = KlassUtility.forNameEx(fullClassName);
        } catch (final BasicSyntaxException e) {
            throw new BasicSyntaxException(
                    "The class in the USE statement is not found.", e);
        }

        return new CommandUse(klass, aliasName);
    }
}
