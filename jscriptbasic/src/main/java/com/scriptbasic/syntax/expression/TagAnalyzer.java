package com.scriptbasic.syntax.expression;

import static com.scriptbasic.syntax.expression.LexFacade.get;
import static com.scriptbasic.syntax.expression.LexFacade.peek;

import java.util.HashMap;
import java.util.Map;

import com.scriptbasic.executors.AbstractPrimitiveRightValue;
import com.scriptbasic.executors.ArrayElementAccess;
import com.scriptbasic.executors.BasicDoubleValue;
import com.scriptbasic.executors.BasicLongValue;
import com.scriptbasic.executors.BasicStringValue;
import com.scriptbasic.executors.FunctionCall;
import com.scriptbasic.executors.VariableAccess;
import com.scriptbasic.executors.operators.AbstractUnaryOperator;
import com.scriptbasic.executors.operators.UnaryOperatorMinus;
import com.scriptbasic.executors.operators.UnaryOperatorNot;
import com.scriptbasic.executors.operators.UnaryOperatorPlus;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.SyntaxAnalyzer;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.syntax.GenericSyntaxException;

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
public class TagAnalyzer extends AbstractAnalyzer {
    private BasicExpressionAnalyzer expressionAnalyzer = null;

    @Override
    public SyntaxAnalyzer getSyntaxAnalyzer() {
        return this.expressionAnalyzer.getSyntaxAnalyzer();
    }

    public TagAnalyzer(final BasicExpressionAnalyzer expressionAnalyzer) {
        this.expressionAnalyzer = expressionAnalyzer;
    }

    private final ExpresionListAnalyzer expressionListAnalyzer = new ExpresionListAnalyzer(
            this);

    public BasicExpressionAnalyzer getExpressionAnalyzer() {
        return expressionAnalyzer;
    }

    private static Map<String, Class<? extends AbstractUnaryOperator>> unaryOperatorMap = new HashMap<String, Class<? extends AbstractUnaryOperator>>();
    static {
        unaryOperatorMap.put("+", UnaryOperatorPlus.class);
        unaryOperatorMap.put("-", UnaryOperatorMinus.class);
        unaryOperatorMap.put("not", UnaryOperatorNot.class);
    }

    @Override
    public Expression analyze() throws SyntaxException {
        final LexicalAnalyzer lexicalAnalyzer = getSyntaxAnalyzer()
                .getLexicalAnalyzer();
        LexicalElement lexicalElement = peek(lexicalAnalyzer);
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
            final LexicalAnalyzer lexicalAnalyzer) throws SyntaxException {
        final LexicalElement identifierElement = get(lexicalAnalyzer);
        final LexicalElement lexicalElement = peek(lexicalAnalyzer);
        if (isOpeningBracket(lexicalElement)) {
            return newArray(lexicalAnalyzer, identifierElement);
        } else if (isOpeningParenthese(lexicalElement)) {
            return newFunctionCall(lexicalAnalyzer, identifierElement);
        } else {
            return newVariableAccess(identifierElement);
        }
    }

    private Expression newVariableAccess(final LexicalElement identifierElement) {
        final VariableAccess variableAccessNode = new VariableAccess();
        variableAccessNode.setVariableName(identifierElement.get());
        return variableAccessNode;
    }

    private Expression newFunctionCall(final LexicalAnalyzer lexicalAnalyzer,
            final LexicalElement identifierElement) throws SyntaxException {
        final FunctionCall functionCall = new FunctionCall();
        functionCall.setVariableName(identifierElement.get());
        functionCall.setExpressionList(expressionListAnalyzer.analyze());
        final LexicalElement lexicalElement = peek(lexicalAnalyzer);
        if (isClosingParenthese(lexicalElement)) {
            return functionCall;
        } else {
            throw new GenericSyntaxException(
                    "There is no closing ')' in function call.");
        }
    }

    private Expression newArray(final LexicalAnalyzer lexicalAnalyzer,
            final LexicalElement identifierElement) throws SyntaxException {
        final ArrayElementAccess arrayElementAccess = new ArrayElementAccess();
        arrayElementAccess.setVariableName(identifierElement.get());
        arrayElementAccess.setExpressionList(expressionListAnalyzer.analyze());
        final LexicalElement lexicalElement = peek(lexicalAnalyzer);
        if (isClosingBracket(lexicalElement)) {
            return arrayElementAccess;
        } else {
            throw new GenericSyntaxException(
                    "There is no closing ']' in array element access.");
        }

    }

    private Expression newSubExpression(final LexicalAnalyzer lexicalAnalyzer)
            throws SyntaxException {
        get(lexicalAnalyzer);
        final Expression expression = expressionAnalyzer.analyze();
        final LexicalElement lexicalElement = peek(lexicalAnalyzer);
        if (isClosingParenthese(lexicalElement)) {
            get(lexicalAnalyzer);
            return expression;
        } else {
            throw new GenericSyntaxException(
                    "There is no matching closing ')' for an opening '('");
        }
    }

    private AbstractUnaryOperator newUnaryOperator(
            final LexicalAnalyzer lexicalAnalyzer) throws SyntaxException {
        final LexicalElement lexicalElement = get(lexicalAnalyzer);
        AbstractUnaryOperator operator;
        try {
            operator = unaryOperatorMap.get(lexicalElement.get()).newInstance();
        } catch (final Exception e) {
            throw new GenericSyntaxException(e);
        }
        operator.setOperand(analyze());
        return operator;
    }

    private AbstractPrimitiveRightValue<?> newLiteralConstant(
            final LexicalAnalyzer lexicalAnalyzer) throws SyntaxException {
        final LexicalElement lexicalElement = get(lexicalAnalyzer);
        if (lexicalElement.isDouble()) {
            get(lexicalAnalyzer);
            return new BasicDoubleValue(lexicalElement.doubleValue());
        } else if (lexicalElement.isLong()) {
            get(lexicalAnalyzer);
            return new BasicLongValue(lexicalElement.longValue());
        } else if (lexicalElement.isString()) {
            get(lexicalAnalyzer);
            return new BasicStringValue(lexicalElement.stringValue());
        }
        throw new InternalError("Lexical element type=" + lexicalElement.type()
                + " lexeme=\"" + lexicalElement.get() + "\"");
    }

    private boolean isParenthese(final LexicalElement lexicalElement,
            final String ch) {
        if (lexicalElement != null && lexicalElement.isSymbol()) {
            return ch.equals(lexicalElement.get());
        } else {
            return false;
        }
    }

    private boolean isClosingBracket(final LexicalElement lexicalElement) {
        return isParenthese(lexicalElement, "]");
    }

    private boolean isOpeningBracket(final LexicalElement lexicalElement) {
        return isParenthese(lexicalElement, "[");
    }

    private boolean isClosingParenthese(final LexicalElement lexicalElement) {
        return isParenthese(lexicalElement, ")");
    }

    private boolean isOpeningParenthese(final LexicalElement lexicalElement) {
        return isParenthese(lexicalElement, "(");
    }

    private boolean isUnaryOperator(final LexicalElement lexicalElement) {
        return lexicalElement.isSymbol()
                && unaryOperatorMap.containsKey(lexicalElement.get());
    }

}
