package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.executors.GenericExpressionList;
import com.scriptbasic.executors.leftvalues.ArrayElementAccessLeftValueModifier;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.leftvalues.ObjectFieldAccessLeftValueModifier;
import com.scriptbasic.interfaces.Expression;

public class LeftValue {
    private final BasicLeftValue lv = new BasicLeftValue();

    private LeftValue(final String id) {
        lv.setIdentifier(id);
    }

    public static LeftValue of(final String id) {
        return new LeftValue(id);
    }

    public LeftValue field(final String id) {
        final var modifier = new ObjectFieldAccessLeftValueModifier();
        modifier.setFieldName(id);
        lv.addModifier(modifier);
        return this;
    }

    public LeftValue array(final Expression... indexArray) {
        final var modifier = new ArrayElementAccessLeftValueModifier();
        final var expressionList = new GenericExpressionList();
        for (final Expression e : indexArray) {
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
