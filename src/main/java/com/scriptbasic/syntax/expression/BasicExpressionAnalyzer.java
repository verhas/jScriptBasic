package com.scriptbasic.syntax.expression;

import com.scriptbasic.executors.operators.*;
import com.scriptbasic.factories.Context;

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
                "^", PowerOperator.class));
        BASIC_OPERATOR_MAPS.add(Map.of(
                "*", MultiplyOperator.class,
                "/", DivideOperator.class,
                "%", ModuloOperator.class,
                "div", IntegerDivideOperator.class));
        BASIC_OPERATOR_MAPS.add(Map.of(
                "+", AddOperator.class, // numeric and also concatenation of strings
                "-", MinusOperator.class));
        BASIC_OPERATOR_MAPS.add(Map.of(
                "=", EqualsOperator.class,
                "<", LessThanOperator.class,
                ">", GreaterThanOperator.class,
                ">=", GreaterOrEqualOperator.class,
                "<=", LessOrEqualOperator.class,
                "<>", NotEqualOperator.class));
        BASIC_OPERATOR_MAPS.add(Map.of(
                "and", LogicalAndOperator.class,
                "or", LogicalOrOperator.class));
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
