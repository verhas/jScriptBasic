package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.executors.GenericLeftValueList;
import com.scriptbasic.executors.leftvalues.ArrayElementAccessLeftValueModifier;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.leftvalues.LeftValueModifier;
import com.scriptbasic.executors.leftvalues.ObjectFieldAccessLeftValueModifier;
import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.syntax.expression.ExpressionComparator;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.LexUtility;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import static com.scriptbasic.syntax.expression.ExpressionBuilder.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBasicLeftValueAnalyzer {

    private static Factory factory = new BasicFactory();

    private static BasicLeftValue compile(final String s)
            throws AnalysisException {
        factory.clean();
        final LexicalAnalyzer la = createStringReading(factory, s);
        final LeftValueAnalyzer lva = FactoryUtility
                .getLeftValueAnalyzer(factory);
        final BasicLeftValue e = (BasicLeftValue) lva.analyze();
        if (LexUtility.peek(la) != null) {
            throw new GenericSyntaxException(
                    "There are extra lexemes after the expression: "
                            + LexUtility.peek(la).getLexeme());
        }
        return e;
    }

    private static void compare(BasicLeftValue gv, BasicLeftValue lv) {
        assertEquals(gv.getIdentifier(), lv.getIdentifier());
        Iterator<LeftValueModifier> gvms = gv.getModifiers().iterator();
        Iterator<LeftValueModifier> lvms = lv.getModifiers().iterator();
        while (gvms.hasNext() && lvms.hasNext()) {
            LeftValueModifier gvm = gvms.next();
            LeftValueModifier lvm = lvms.next();
            assertEquals(gvm.getClass(), lvm.getClass());
            if (gvm instanceof ArrayElementAccessLeftValueModifier) {
                ExpressionComparator.assertEqual(
                        ((ArrayElementAccessLeftValueModifier) gvm)
                                .getIndexList(),
                        ((ArrayElementAccessLeftValueModifier) lvm)
                                .getIndexList());

            } else if (gvm instanceof ObjectFieldAccessLeftValueModifier) {
                assertEquals(
                        ((ObjectFieldAccessLeftValueModifier) gvm)
                                .getFieldName(),
                        ((ObjectFieldAccessLeftValueModifier) lvm)
                                .getFieldName());
            } else {
                assertTrue(
                        "Unknown left value modifier class " + gvm.getClass(),
                        false);
            }
        }
    }

    private static void extressionHasSyntaxError(String s)
            throws AnalysisException {
        try {
            compile(s);
            Assert.fail("Syntax exception was not trown for '" + s + "'");
        } catch (SyntaxException e) {
        }

    }

    private static GenericLeftValueList compileList(final String s)
            throws AnalysisException {
        factory.clean();
        final LexicalAnalyzer la = createStringReading(factory, s);
        final LeftValueListAnalyzer lva = FactoryUtility
                .getLeftValueListAnalyzer(factory);
        final GenericLeftValueList e = (GenericLeftValueList) lva.analyze();
        if (LexUtility.peek(la) != null) {
            throw new GenericSyntaxException(
                    "There are extra lexemes after the expression: "
                            + LexUtility.peek(la).getLexeme());
        }
        return e;
    }

    private static void expressionCompilesTo(String expression, BasicLeftValue gv) throws AnalysisException {
        BasicLeftValue lv = compile(expression);
        compare(gv, lv);
    }

    @Test
    public void testLeftValues() throws Exception {
        BasicLeftValue lv;
        BasicLeftValue gv;

        expressionCompilesTo("apple[3].apple",
                LeftValue.of("apple").array(LONG(3L)).field("apple").build());

        expressionCompilesTo("apple",
                LeftValue.of("apple").build());

        expressionCompilesTo("apple.sheep",
                LeftValue.of("apple").field("sheep").build());

        expressionCompilesTo("com.scriptbasic.syntax.leftvalue",
                LeftValue.of("com").field("scriptbasic").field("syntax")
                        .field("leftvalue").build());

        expressionCompilesTo("apple[3]",
                LeftValue.of("apple").array(LONG(3L)).build());

        expressionCompilesTo("apple[3,5]",
                LeftValue.of("apple").array(LONG(3L), LONG(5L)).build());

        expressionCompilesTo("apple[3,apple]",
                LeftValue.of("apple").array(LONG(3L), ID("apple")).build());

        expressionCompilesTo("apple[3][apple]",
                LeftValue.of("apple").array(LONG(3L)).array(ID("apple"))
                        .build());

        expressionCompilesTo("apple[3].apple.bbb[cc,dd.qq[4]*3]",
                LeftValue.of("apple")
                        .array(LONG(3L))
                        .field("apple")
                        .field("bbb")
                        .array(ID("cc"),
                                multiply(OBJECT_FIELD(ID("dd"), array("qq", LONG(4L))),
                                        LONG(3L))).build());

        extressionHasSyntaxError("apple.");
        extressionHasSyntaxError("apple[]");
        extressionHasSyntaxError("apple..");
        extressionHasSyntaxError("apple[");
        extressionHasSyntaxError("apple]");
        extressionHasSyntaxError("apple+3");
        extressionHasSyntaxError("apple[3");
        extressionHasSyntaxError("apple[3)");
        extressionHasSyntaxError("apple[3aaa");
        extressionHasSyntaxError("[");
        extressionHasSyntaxError("apple[hat.het+\"kaka\".z");

    }

    @Test
    public void variableNamesSeparatedByCommaCompilesToLeftValueList() throws Exception {
        compileList("a,b,c,d");
    }

}
