package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandUse;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.spi.Command;
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
        final var className = ExpressionUtility
                .convertToString(analyzeExpression());
        LexUtility.checkLexeme(ctx.lexicalAnalyzer, ScriptBasicKeyWords.KEYWORD_FROM,
                "Keyword 'FROM' is missing in command 'USE'");
        final var packageName = ExpressionUtility
                .convertToString(analyzeExpression());
        final String aliasName;
        if (LexUtility.isLexeme(ctx.lexicalAnalyzer, ScriptBasicKeyWords.KEYWORD_AS)) {
            aliasName = ExpressionUtility.convertToString(analyzeExpression());
        } else {
            aliasName = className;
        }
        consumeEndOfStatement();
        if (className.indexOf('.') != -1 || aliasName.indexOf('.') != -1) {
            throw new BasicSyntaxException(
                    "class name and alias name should not contain dot in command USE");
        }
        final var fullClassName = packageName + "." + className;
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
