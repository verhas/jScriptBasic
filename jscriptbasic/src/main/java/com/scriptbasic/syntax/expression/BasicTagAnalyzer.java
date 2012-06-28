package com.scriptbasic.syntax.expression;

import java.util.HashMap;
import java.util.Map;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.operators.AbstractUnaryOperator;
import com.scriptbasic.executors.operators.UnaryOperatorMinus;
import com.scriptbasic.executors.operators.UnaryOperatorNot;
import com.scriptbasic.executors.operators.UnaryOperatorPlus;
import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.executors.rightvalues.ArrayElementAccess;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.executors.rightvalues.VariableAccess;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.TagAnalyzer;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;
import com.scriptbasic.utility.LexUtility;

/**
 * Analyze a tag. A tag is the most primitive part of an expression that does
 * not contain any further operators (unless the operators are enclosed between
 * brackets some way).
 * <p>
 * A tag is defined as the following:
 * 
 * <pre>
 *  tag ::= UNOP tag
 *          TRUE | FALSE
 *          NUMBER
 *          STRING
 *          '(' expression ')'
 *          VARIABLE { '[' expression_list ']' }
 *          FUNC '(' expression_list ')'
 * </pre>
 * 
 * @author Peter Verhas
 * 
 */
public final class BasicTagAnalyzer extends AbstractAnalyzer<Expression>
        implements TagAnalyzer {
    // private static Logger log =
    // LoggerFactory.getLogger(BasicTagAnalyzer.class);

    private BasicTagAnalyzer() {
    }

    private Factory factory;

    @Override
    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    private static Map<String, Class<? extends AbstractUnaryOperator>> unaryOperatorMap = new HashMap<String, Class<? extends AbstractUnaryOperator>>();
    static {
        unaryOperatorMap.put("+", UnaryOperatorPlus.class);
        unaryOperatorMap.put("-", UnaryOperatorMinus.class);
        unaryOperatorMap.put("not", UnaryOperatorNot.class);
    }

    @Override
    public Expression analyze() throws AnalysisException {
        final LexicalAnalyzer lexicalAnalyzer = FactoryUtilities
                .getLexicalAnalyzer(factory);
        final LexicalElement lexicalElement = LexUtility.peek(lexicalAnalyzer);
        if (lexicalElement != null) {
            if (isUnaryOperator(lexicalElement)) {
                return newUnaryOperator(lexicalAnalyzer);
            } else if (lexicalElement.isLiteralConstant()) {
                return newLiteralConstant(lexicalAnalyzer);
            } else if (isOpeningParenthese(lexicalElement)) {
                return newSubExpression(lexicalAnalyzer);
            } else if (lexicalElement.isIdentifier()) {
                return newArrayOrVariableOrFunctionCall(lexicalAnalyzer);
            }
        }
        throw new GenericSyntaxException();
    }

    private Expression newArrayOrVariableOrFunctionCall(
            final LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        final LexicalElement identifierElement = LexUtility
                .get(lexicalAnalyzer);
        final LexicalElement lexicalElement = LexUtility.peek(lexicalAnalyzer);
        if (isOpeningBracket(lexicalElement)) {
            return newArray(lexicalAnalyzer, identifierElement);
        } else if (isOpeningParenthese(lexicalElement)) {
            return newFunctionCall(lexicalAnalyzer, identifierElement);
        } else {
            return newVariableAccess(identifierElement);
        }
    }

    private static Expression newVariableAccess(
            final LexicalElement identifierElement) {
        final VariableAccess variableAccessNode = new VariableAccess();
        variableAccessNode.setVariableName(identifierElement.getLexeme());
        return variableAccessNode;
    }

    private Expression newFunctionCall(final LexicalAnalyzer lexicalAnalyzer,
            final LexicalElement identifierElement) throws AnalysisException {
        final FunctionCall functionCall = new FunctionCall();
        LexUtility.get(lexicalAnalyzer);
        functionCall.setVariableName(identifierElement.getLexeme());
        LexicalElement lexicalElement = LexUtility.peek(lexicalAnalyzer);
        if (!isClosingParenthese(lexicalElement)) {
            functionCall.setExpressionList(FactoryUtilities
                    .getExpressionListAnalyzer(factory).analyze());
            lexicalElement = LexUtility.peek(lexicalAnalyzer);
        }
        if (isClosingParenthese(lexicalElement)) {
            LexUtility.get(lexicalAnalyzer);
            return functionCall;
        } else {
            throw new GenericSyntaxException(
                    "There is no closing ')' in function call.");
        }
    }

    private Expression newArray(final LexicalAnalyzer lexicalAnalyzer,
            final LexicalElement identifierElement) throws AnalysisException {
        LexUtility.get(lexicalAnalyzer);
        final ArrayElementAccess arrayElementAccess = new ArrayElementAccess();
        arrayElementAccess.setVariableName(identifierElement.getLexeme());
        arrayElementAccess.setExpressionList(FactoryUtilities
                .getExpressionListAnalyzer(factory).analyze());
        final LexicalElement lexicalElement = LexUtility.peek(lexicalAnalyzer);
        if (isClosingBracket(lexicalElement)) {
            LexUtility.get(lexicalAnalyzer);
            return arrayElementAccess;
        } else {
            throw new GenericSyntaxException(
                    "There is no closing ']' in array element access.");
        }

    }

    private Expression newSubExpression(final LexicalAnalyzer lexicalAnalyzer)
            throws AnalysisException {
        LexUtility.get(lexicalAnalyzer);
        final Expression expression = FactoryUtilities.getExpressionAnalyzer(
                factory).analyze();
        final LexicalElement lexicalElement = LexUtility.peek(lexicalAnalyzer);
        if (isClosingParenthese(lexicalElement)) {
            LexUtility.get(lexicalAnalyzer);
            return expression;
        } else {
            throw new GenericSyntaxException(
                    "There is no matching closing ')' for an opening '('");
        }
    }

    private AbstractUnaryOperator newUnaryOperator(
            final LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        final LexicalElement lexicalElement = LexUtility.get(lexicalAnalyzer);
        AbstractUnaryOperator operator;
        try {
            operator = unaryOperatorMap.get(lexicalElement.getLexeme())
                    .newInstance();
        } catch (final Exception e) {
            throw new GenericSyntaxException(e);
        }
        operator.setOperand(analyze());
        return operator;
    }

    private static AbstractPrimitiveRightValue<?> newLiteralConstant(
            final LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        final LexicalElement lexicalElement = LexUtility.get(lexicalAnalyzer);
        if (lexicalElement.isDouble()) {
            return new BasicDoubleValue(lexicalElement.doubleValue());
        } else if (lexicalElement.isLong()) {
            return new BasicLongValue(lexicalElement.longValue());
        } else if (lexicalElement.isString()) {
            return new BasicStringValue(lexicalElement.stringValue());
        } else if (lexicalElement.isBoolean()) {
            return new BasicBooleanValue(lexicalElement.booleanValue());
        }
        throw new BasicInterpreterInternalError("Lexical element type="
                + lexicalElement.getType() + " lexeme=\""
                + lexicalElement.getLexeme() + "\"");
    }

    private static boolean isParenthese(final LexicalElement lexicalElement,
            final String ch) {
        if (lexicalElement != null && lexicalElement.isSymbol()) {
            return ch.equals(lexicalElement.getLexeme());
        } else {
            return false;
        }
    }

    private static boolean isClosingBracket(final LexicalElement lexicalElement) {
        return isParenthese(lexicalElement, "]");
    }

    private static boolean isOpeningBracket(final LexicalElement lexicalElement) {
        return isParenthese(lexicalElement, "[");
    }

    private static boolean isClosingParenthese(
            final LexicalElement lexicalElement) {
        return isParenthese(lexicalElement, ")");
    }

    private static boolean isOpeningParenthese(
            final LexicalElement lexicalElement) {
        return isParenthese(lexicalElement, "(");
    }

    private static boolean isUnaryOperator(final LexicalElement lexicalElement) {
        return lexicalElement.isSymbol()
                && unaryOperatorMap.containsKey(lexicalElement.getLexeme());
    }
}
