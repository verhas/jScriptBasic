package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.spi.Command;
import com.scriptbasic.spi.LeftValue;
import com.scriptbasic.spi.LeftValueList;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.utility.SyntaxExceptionUtility;

public abstract class AbstractCommandAnalyzer extends AbstractAnalyzer<Command>
        implements CommandAnalyzer {
    protected final Context ctx;

    protected AbstractCommandAnalyzer(final Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Check that the left values are simple (no modifiers, a.k.a. simply
     * variables) and are the same variables (have the same name).
     *
     * @param a variable one
     * @param b variable two
     * @return {@code true} if the variables have the same name and none of them
     * has modifiers (array access or field access)
     */
    protected static boolean equal(final LeftValue a, final LeftValue b) {
        if (a == b || (a != null && a.equals(b))) {
            return true;
        }
        if (a instanceof BasicLeftValue && b instanceof BasicLeftValue) {
            final var aBasic = (BasicLeftValue) a;
            final var bBasic = (BasicLeftValue) b;
            if (aBasic.hasModifiers() || bBasic.hasModifiers()) {
                return false;
            }
            return aBasic.getIdentifier() != null
                    && aBasic.getIdentifier().equals(bBasic.getIdentifier());

        } else {
            return false;
        }
    }


    protected String getName() {
        return this.getClass().getSimpleName().substring(15).toUpperCase();
    }

    protected LeftValueList analyzeSimpleLeftValueList()
            throws AnalysisException {
        return ctx.simpleLeftValueListAnalyzer.analyze();
    }

    protected LeftValue analyzeSimpleLeftValue() throws AnalysisException {
        return ctx.simpleLeftValueAnalyzer.analyze();
    }

    protected Expression analyzeExpression() throws AnalysisException {
        return ctx.expressionAnalyzer.analyze();
    }

    protected ExpressionList analyzeExpressionList() throws AnalysisException {
        return ctx.expressionListAnalyzer.analyze();
    }

    protected void pushNode(final NestedStructure node) {
        ctx.nestedStructureHouseKeeper.push(node);
    }

    /**
     * Ensures that the appropriate keyword is on the line. Also it eats up that
     * keyword.
     *
     * @param keyword the keyword that has to be present on the line
     * @throws AnalysisException when the next lexeme is NOT the expected keyword.
     */
    protected void assertKeyWord(final String keyword) throws AnalysisException {
        if (!isKeyWord(keyword)) {
            final var lexicalElement = ctx.lexicalAnalyzer.peek();
            throw new BasicSyntaxException("There is no '" + keyword
                    + "' after the '" + getName() + "'", lexicalElement, null);
        } else {
            ctx.lexicalAnalyzer.get();
        }
    }

    protected boolean isKeyWord(final String keyword) throws AnalysisException {
        final var lexicalElement = ctx.lexicalAnalyzer.peek();
        return lexicalElement != null && lexicalElement.isSymbol(keyword);
    }

    /**
     * Checks that there are no extra characters when the line analyzer 
     * expects it has finished analyzing the statement. If there are
     * some extra characters on the line then throws syntax error exception.
     * Otherwise it simply steps the lexical analyzer iterator over the symbol.
     *
     * @throws AnalysisException when there are extra character on the actual line
     */
    protected void consumeEndOfStatement() throws AnalysisException {
        final var le = ctx.lexicalAnalyzer.get();
        if (le != null && !(le.isLineTerminator() || le.isStatementSeparator())) {
            SyntaxExceptionUtility.throwSyntaxException(
                    "There are extra characters following the expression after the '"
                            + getName() + "' keyword", le);
        }
    }
}
