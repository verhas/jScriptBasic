package com.scriptbasic.syntax.expression;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.operators.*;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public final class BasicExpressionAnalyzer extends AbstractExpressionAnalyzer {

    private static final Integer MAXIMUM_PRIORITY = 6;

    private static final List<Map<String, Class<? extends AbstractBinaryOperator>>> BASIC_OPERATOR_MAPS
            = new ArrayList<>(MAXIMUM_PRIORITY + 1);

    static {
        BASIC_OPERATOR_MAPS.add(Map.of(
                ".", JavaObjectFieldAccessOperator.class));
        BASIC_OPERATOR_MAPS.add(Map.of(
                ScriptBasicKeyWords.OPERATOR_POWER, PowerOperator.class));
        BASIC_OPERATOR_MAPS.add(Map.of(
                ScriptBasicKeyWords.OPERATOR_MULTIPLY, MultiplyOperator.class,
                ScriptBasicKeyWords.OPERATOR_DIVIDE, DivideOperator.class,
                ScriptBasicKeyWords.OPERATOR_MODULO, ModuloOperator.class,
                ScriptBasicKeyWords.KEYWORD_DIV, IntegerDivideOperator.class));
        BASIC_OPERATOR_MAPS.add(Map.of(
                ScriptBasicKeyWords.OPERATOR_PLUS, AddOperator.class, // numeric and also concatenation of strings
                ScriptBasicKeyWords.OPERATOR_MINUS, MinusOperator.class,
                ScriptBasicKeyWords.OPERATOR_AMPERSAND, AmpersandOperator.class));
        BASIC_OPERATOR_MAPS.add(Map.of(
                ScriptBasicKeyWords.OPERATOR_EQUALS, EqualsOperator.class,
                ScriptBasicKeyWords.OPERATOR_LESS, LessThanOperator.class,
                ScriptBasicKeyWords.OPERATOR_GREATER, GreaterThanOperator.class,
                ScriptBasicKeyWords.OPERATOR_GREATER_OR_EQUAL, GreaterOrEqualOperator.class,
                ScriptBasicKeyWords.OPERATOR_LESS_OR_EQUAL, LessOrEqualOperator.class,
                ScriptBasicKeyWords.OPERATOR_NOT_EQUALS, NotEqualOperator.class));
        BASIC_OPERATOR_MAPS.add(Map.of(
                ScriptBasicKeyWords.KEYWORD_AND, LogicalAndOperator.class,
                ScriptBasicKeyWords.KEYWORD_OR, LogicalOrOperator.class));
        // XOR is not implemented in jScriptBasic by design
    }

    public BasicExpressionAnalyzer(final Context ctx) {
        super(ctx);
    }

    @Override
    protected Integer getMaximumPriority() {
        return MAXIMUM_PRIORITY;
    }

    @Override
    protected Map<String, Class<? extends AbstractBinaryOperator>> getOperatorMap(
            final Integer priority) {
        return BASIC_OPERATOR_MAPS.get(priority - 1);
    }
}
