package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.executors.GenericExpressionList;
import com.scriptbasic.executors.leftvalues.ArrayElementAccessLeftValueModifier;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.leftvalues.ObjectFieldAccessLeftValueModifier;
import com.scriptbasic.interfaces.Expression;

public class LeftValue {
    private BasicLeftValue lv = new BasicLeftValue();

    private LeftValue(String id) {
        lv.setIdentifier(id);
    }

    public static LeftValue of(String id) {
        return new LeftValue(id);
    }

    public LeftValue field(String id) {
        ObjectFieldAccessLeftValueModifier modifier = new ObjectFieldAccessLeftValueModifier();
        modifier.setFieldName(id);
        lv.addModifier(modifier);
        return this;
    }

    public LeftValue array(Expression... indexArray) {
        ArrayElementAccessLeftValueModifier modifier = new ArrayElementAccessLeftValueModifier();
        GenericExpressionList expressionList = new GenericExpressionList();
        for (Expression e : indexArray) {
            expressionList.add(e);
        }
        modifier.setIndexList(expressionList);
        lv.addModifier(modifier);
        return this;
    }

    public BasicLeftValue build() {
        return lv;
    }
}
