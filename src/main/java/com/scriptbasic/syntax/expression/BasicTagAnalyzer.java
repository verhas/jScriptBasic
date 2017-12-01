package com.scriptbasic.syntax.expression;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.executors.operators.AbstractUnaryOperator;
import com.scriptbasic.executors.operators.UnaryOperatorMinus;
import com.scriptbasic.executors.operators.UnaryOperatorNot;
import com.scriptbasic.executors.operators.UnaryOperatorPlus;
import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.utility.LexUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * Analyze a tag. A tag is the most primitive part of an expression that does
 * not contain any further operators (unless the operators are enclosed between
 * brackets some way).
 * <p>
 * A tag is defined as the following:
 * <p>
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
 */
public final class BasicTagAnalyzer extends AbstractAnalyzer<Expression>
        implements TagAnalyzer {


    private static Map<String, Class<? extends AbstractUnaryOperator>> unaryOperatorMap = new HashMap<>();

    static {
        unaryOperatorMap.put("+", UnaryOperatorPlus.class);
        unaryOperatorMap.put("-", UnaryOperatorMinus.class);
        unaryOperatorMap.put("not", UnaryOperatorNot.class);
    }

    private final Context ctx;

    public BasicTagAnalyzer(Context ctx) {
        this.ctx = ctx;
    }

    private static Expression newVariableAccess(
            final LexicalElement identifierElement) {
        final VariableAccess variableAccessNode = new VariableAccess();
        variableAccessNode.setVariableName(identifierElement.getLexeme());
        return variableAccessNode;
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

    @Override
    public Expression analyze() throws AnalysisException {
        final LexicalAnalyzer lexicalAnalyzer = ctx.lexicalAnalyzer;
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
        throw new BasicSyntaxException(
                "Expression syntax is erroneous. Unexpected lexeme is "
                        + lexicalElement, lexicalElement);
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

    private Expression newFunctionCall(final LexicalAnalyzer lexicalAnalyzer,
                                       final LexicalElement identifierElement) throws AnalysisException {
        final FunctionCall functionCall = new FunctionCall();
        LexUtility.get(lexicalAnalyzer);
        functionCall.setVariableName(identifierElement.getLexeme());
        LexicalElement lexicalElement = LexUtility.peek(lexicalAnalyzer);
        if (!isClosingParenthese(lexicalElement)) {
            functionCall.setExpressionList(ctx.expressionListAnalyzer.analyze());
            lexicalElement = LexUtility.peek(lexicalAnalyzer);
        }
        if (isClosingParenthese(lexicalElement)) {
            LexUtility.get(lexicalAnalyzer);
            return functionCall;
        } else {
            throw new BasicSyntaxException("There is no closing ')' in function call.");
        }
    }

    private Expression newArray(final LexicalAnalyzer lexicalAnalyzer,
                                final LexicalElement identifierElement) throws AnalysisException {
        LexUtility.get(lexicalAnalyzer);
        final ArrayElementAccess arrayElementAccess = new ArrayElementAccess();
        arrayElementAccess.setVariableName(identifierElement.getLexeme());
        arrayElementAccess.setExpressionList(ctx.expressionListAnalyzer.analyze());
        final LexicalElement lexicalElement = LexUtility.peek(lexicalAnalyzer);
        if (isClosingBracket(lexicalElement)) {
            LexUtility.get(lexicalAnalyzer);
            return arrayElementAccess;
        } else {
            throw new BasicSyntaxException("There is no closing ']' in array element access.");
        }

    }

    private Expression newSubExpression(final LexicalAnalyzer lexicalAnalyzer)
            throws AnalysisException {
        LexUtility.get(lexicalAnalyzer);
        final Expression expression = ctx.expressionAnalyzer.analyze();
        final LexicalElement lexicalElement = LexUtility.peek(lexicalAnalyzer);
        if (!isClosingParenthese(lexicalElement)) {
            throw new BasicSyntaxException("There is no matching closing ')' for an opening '('");
        }
        LexUtility.get(lexicalAnalyzer);
        return expression;
    }

    private AbstractUnaryOperator newUnaryOperator(
            final LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        final LexicalElement lexicalElement = LexUtility.get(lexicalAnalyzer);
        final AbstractUnaryOperator operator;
        try {
            operator = unaryOperatorMap.get(lexicalElement.getLexeme())
                    .getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
            throw new BasicSyntaxException(e);
        }
        operator.setOperand(analyze());
        return operator;
    }
}
