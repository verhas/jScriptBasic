package com.scriptbasic.syntax.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scriptbasic.executors.operators.AbstractBinaryOperator;
import com.scriptbasic.executors.operators.AddOperator;
import com.scriptbasic.executors.operators.DivideOperator;
import com.scriptbasic.executors.operators.EqualsOperator;
import com.scriptbasic.executors.operators.GreaterOrEqualOperator;
import com.scriptbasic.executors.operators.GreaterThanOperator;
import com.scriptbasic.executors.operators.IntegerDivideOperator;
import com.scriptbasic.executors.operators.LessOrEqualOperator;
import com.scriptbasic.executors.operators.LessThanOperator;
import com.scriptbasic.executors.operators.LogicalAndOperator;
import com.scriptbasic.executors.operators.LogicalOrOperator;
import com.scriptbasic.executors.operators.MinusOperator;
import com.scriptbasic.executors.operators.ModuloOperator;
import com.scriptbasic.executors.operators.MultiplyOperator;
import com.scriptbasic.executors.operators.NotEqualOperator;
import com.scriptbasic.executors.operators.ObjectFieldAccessOperator;
import com.scriptbasic.executors.operators.PowerOperator;

public final class BasicExpressionAnalyzer extends AbstractExpressionAnalyzer {

    private BasicExpressionAnalyzer() {
    }

    private static final Integer MAXIMUM_PRIORITY = 6;

    private static final List<Map<String, Class<? extends AbstractBinaryOperator>>> BASIC_OPERATOR_MAPS = new ArrayList<Map<String, Class<? extends AbstractBinaryOperator>>>(
            MAXIMUM_PRIORITY + 1);
    static {
        for (Integer i = 0; i <= MAXIMUM_PRIORITY; i++) {
            BASIC_OPERATOR_MAPS.add(null);
        }
    }

    private static Map<String, Class<? extends AbstractBinaryOperator>> getStaticOperatorMap(
            final Integer priority) {
        Map<String, Class<? extends AbstractBinaryOperator>> operatorMap = BASIC_OPERATOR_MAPS
                .get(priority);
        if (operatorMap == null) {
            operatorMap = new HashMap<String, Class<? extends AbstractBinaryOperator>>();
            BASIC_OPERATOR_MAPS.set(priority, operatorMap);
        }
        return operatorMap;
    }

    static {
        Map<String, Class<? extends AbstractBinaryOperator>> opMap = null;
        int priority = 0;
        opMap = getStaticOperatorMap(++priority);
        opMap.put(".", ObjectFieldAccessOperator.class);
        opMap = getStaticOperatorMap(++priority);
        opMap.put("^", PowerOperator.class);
        opMap = getStaticOperatorMap(++priority);
        opMap.put("*", MultiplyOperator.class);
        opMap.put("/", DivideOperator.class);
        opMap.put("%", ModuloOperator.class);
        opMap.put("div", IntegerDivideOperator.class);
        opMap = getStaticOperatorMap(++priority);
        opMap.put("+", AddOperator.class); // numeric and also concatenation of
                                           // strings
        opMap.put("-", MinusOperator.class);
        opMap = getStaticOperatorMap(++priority);
        // LIKE operator is NOT implemented in jScriptBasic, use Java methods as
        // it was in ScriptBasic
        opMap.put("=", EqualsOperator.class);
        opMap.put("<", LessThanOperator.class);
        opMap.put(">", GreaterThanOperator.class);
        opMap.put(">=", GreaterOrEqualOperator.class);
        opMap.put("<=", LessOrEqualOperator.class);
        opMap.put("<>", NotEqualOperator.class);
        opMap = getStaticOperatorMap(++priority);
        opMap.put("and", LogicalAndOperator.class);
        opMap.put("or", LogicalOrOperator.class);
        // XOR is not implemented in jScriptBasic by design
    }

    @Override
    protected Integer getMaximumPriority() {
        return MAXIMUM_PRIORITY;
    }

    @Override
    protected Map<String, Class<? extends AbstractBinaryOperator>> getOperatorMap(
            final Integer priority) {
        return getStaticOperatorMap(priority);
    }

}
