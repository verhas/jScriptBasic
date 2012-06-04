package com.scriptbasic.syntax.expression;

import java.util.ArrayList;
import java.util.HashMap;
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

public class BasicExpressionAnalyzer extends AbstractExpressionAnalyzer {

    private final static Integer maximumPriority = 6;

    private static ArrayList<Map<String, Class<? extends AbstractBinaryOperator>>> operatorMapArray = new ArrayList<Map<String, Class<? extends AbstractBinaryOperator>>>(
            maximumPriority + 1);
    static {
        for (Integer i = 0; i <= maximumPriority; i++) {
            operatorMapArray.add(null);
        }
    }

    private static Map<String, Class<? extends AbstractBinaryOperator>> getStaticOperatorMap(
            final Integer priority) {
        Map<String, Class<? extends AbstractBinaryOperator>> operatorMap = operatorMapArray
                .get(priority);
        if (operatorMap == null) {
            operatorMap = new HashMap<String, Class<? extends AbstractBinaryOperator>>();
            operatorMapArray.set(priority, operatorMap);
        }
        return operatorMap;
    }

    static {
        // TODO create all the executor classes and replace the nulls
        Map<String, Class<? extends AbstractBinaryOperator>> opMap = null;
        opMap = getStaticOperatorMap(1);
        opMap.put(".", ObjectFieldAccessOperator.class);
        opMap = getStaticOperatorMap(2);
        opMap.put("^", PowerOperator.class);
        opMap = getStaticOperatorMap(3);
        opMap.put("*", MultiplyOperator.class);
        opMap.put("/", DivideOperator.class);
        opMap.put("%", ModuloOperator.class);
        opMap.put("div", IntegerDivideOperator.class);
        opMap = getStaticOperatorMap(4);
        opMap.put("+", AddOperator.class); // numeric and also concatenation of strings
        opMap.put("-", MinusOperator.class);
        opMap = getStaticOperatorMap(5);
        // LIKE operator is NOT implemented in jScriptBasic, use Java methods
        opMap.put("=", EqualsOperator.class);
        opMap.put("<", LessThanOperator.class);
        opMap.put(">", GreaterThanOperator.class);
        opMap.put(">=", GreaterOrEqualOperator.class);
        opMap.put("<=", LessOrEqualOperator.class);
        opMap.put("<>", NotEqualOperator.class);
        opMap = getStaticOperatorMap(6);
        opMap.put("and", LogicalAndOperator.class);
        opMap.put("or", LogicalOrOperator.class);
        // XOR is not implemented in jScriptBasic by design
    }

    @Override
    protected Integer getMaximumPriority() {
        return maximumPriority;
    }

    private final TagAnalyzer tagAnalyzer = new TagAnalyzer(this);

    @Override
    protected TagAnalyzer getTagAnalyzer() {
        return tagAnalyzer;
    }

    @Override
    protected Map<String, Class<? extends AbstractBinaryOperator>> getOperatorMap(
            final Integer priority) {
        return getStaticOperatorMap(priority);
    }
}
