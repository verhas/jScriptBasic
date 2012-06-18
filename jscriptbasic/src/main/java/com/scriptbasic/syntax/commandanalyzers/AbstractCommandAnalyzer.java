package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.NestedStructure;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;
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

    abstract protected String getName();

    protected Expression analyzeExpression() throws AnalysisException {
        return FactoryUtilities.getExpressionAnalyzer(getFactory()).analyze();
    }

    protected void pushNodeOnTheAnalysisStack(NestedStructure node) {
        NestedStructureHouseKeeper nshk = FactoryUtilities
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
        LexicalElement lexicalElement = FactoryUtilities.getLexicalAnalyzer(
                getFactory()).get();
        if (lexicalElement == null || !lexicalElement.isSymbol(keyword)) {
            throw new GenericSyntaxException("There is no '" + keyword
                    + "' after the '" + getName() + "'", lexicalElement);
        }
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
        LexicalElement le = FactoryUtilities.getLexicalAnalyzer(factory).get();
        if (le != null && !le.isLineTerminator()) {
            SyntaxExceptionUtility.throwSyntaxException(
                    "There are extra characters following the expression after the '"
                            + getName() + "' keyword", le);
        }
    }
}
