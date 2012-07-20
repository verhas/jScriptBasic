package com.scriptbasic.syntax.commands;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LeftValue;
import com.scriptbasic.interfaces.LeftValueList;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.NestedStructure;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.SyntaxExceptionUtility;

public abstract class AbstractCommandAnalyzer extends AbstractAnalyzer<Command>
        implements CommandAnalyzer {
    private Factory factory;

    @Override
    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    protected abstract String getName();

    /**
     * Check that the left values are simple (no modifiers, aka simply
     * variables) and are teh same variables (have the same name).
     * 
     * @param a
     *            variable one
     * @param b
     *            variable two
     * @return {@code true} if the variables have the same name and none of them
     *         has modifiers (array access or field access)
     */
    protected static boolean equal(LeftValue a, LeftValue b) {
        if (a == b || (a != null && a.equals(b))) {
            return true;
        }
        if (a instanceof BasicLeftValue && b instanceof BasicLeftValue) {
            BasicLeftValue aBasic = (BasicLeftValue) a;
            BasicLeftValue bBasic = (BasicLeftValue) b;
            if (aBasic.hasModifiers() || bBasic.hasModifiers()) {
                return false;
            }
            return aBasic.getIdentifier() != null
                    && aBasic.getIdentifier().equals(bBasic.getIdentifier());

        } else {
            return false;
        }
    }

    protected LeftValueList analyzeLeftValueList() throws AnalysisException {
        return FactoryUtility.getLeftValueListAnalyzer(getFactory()).analyze();
    }

    protected LeftValueList analyzeSimpleLeftValueList()
            throws AnalysisException {
        return FactoryUtility.getSimpleLeftValueListAnalyzer(getFactory())
                .analyze();
    }

    protected LeftValue analyzeSimpleLeftValue() throws AnalysisException {
        return FactoryUtility.getSimpleLeftValueAnalyzer(getFactory())
                .analyze();
    }

    protected Expression analyzeExpression() throws AnalysisException {
        return FactoryUtility.getExpressionAnalyzer(getFactory()).analyze();
    }

    protected ExpressionList analyzeExpressionList() throws AnalysisException {
        return FactoryUtility.getExpressionListAnalyzer(getFactory()).analyze();
    }

    protected void pushNode(NestedStructure node) {
        NestedStructureHouseKeeper nshk = FactoryUtility
                .getNestedStructureHouseKeeper(getFactory());
        nshk.push(node);
    }

    /**
     * Ensures that the appropriate keyword is on the line. Also it eats up that
     * keyword.
     * 
     * @param keyword
     *            the keyword that has to be present on the line
     * @throws AnalysisException
     *             when the next lexeme is NOT the expected keyword.
     */
    protected void assertKeyWord(String keyword) throws AnalysisException {
        if (!isKeyWord(keyword)) {
            LexicalElement lexicalElement = FactoryUtility.getLexicalAnalyzer(
                    getFactory()).peek();
            throw new GenericSyntaxException("There is no '" + keyword
                    + "' after the '" + getName() + "'", lexicalElement, null);
        } else {
            FactoryUtility.getLexicalAnalyzer(getFactory()).get();
        }
    }

    protected boolean isKeyWord(String keyword) throws AnalysisException {
        LexicalElement lexicalElement = FactoryUtility.getLexicalAnalyzer(
                getFactory()).peek();
        return lexicalElement != null && lexicalElement.isSymbol(keyword);
    }

    /**
     * Checks that there are no extra characters on a program line when the line
     * analyzer thinks that it has finished analyzing the line. If there are
     * some extra characters on the line then throws syntax error exception.
     * Otherwise it simply steps the lexical analyzer iterator over the EOL
     * symbol.
     * 
     * @throws AnalysisException
     *             when there are extra character on the actual line
     */
    protected void consumeEndOfLine() throws AnalysisException {
        LexicalElement le = FactoryUtility.getLexicalAnalyzer(factory).get();
        if (le != null && !le.isLineTerminator()) {
            SyntaxExceptionUtility.throwSyntaxException(
                    "There are extra characters following the expression after the '"
                            + getName() + "' keyword", le);
        }
    }
}
