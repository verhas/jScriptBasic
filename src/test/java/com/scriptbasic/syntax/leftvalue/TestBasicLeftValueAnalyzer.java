package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.context.ContextBuilder;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.executors.leftvalues.ArrayElementAccessLeftValueModifier;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.leftvalues.LeftValueModifier;
import com.scriptbasic.executors.leftvalues.ObjectFieldAccessLeftValueModifier;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.syntax.expression.ExpressionComparator;
import com.scriptbasic.utility.LexUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import static com.scriptbasic.syntax.expression.ExpressionBuilder.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestBasicLeftValueAnalyzer {


    private static BasicLeftValue compile(final String s)
            throws AnalysisException {
        final var ctx = ContextBuilder.from(createStringReading(s));
        final var leftValueAnalyzer = ctx.leftValueAnalyzer;
        final var e = (BasicLeftValue) leftValueAnalyzer.analyze();
        if (LexUtility.peek(ctx.lexicalAnalyzer) != null) {
            throw new BasicSyntaxException(
                    "There are extra lexemes after the expression: "
                            + LexUtility.peek(ctx.lexicalAnalyzer).getLexeme());
        }
        return e;
    }

    private static void compare(final BasicLeftValue gv, final BasicLeftValue lv) {
        assertEquals(gv.getIdentifier(), lv.getIdentifier());
        final Iterator<LeftValueModifier> gvms = gv.getModifiers().iterator();
        final Iterator<LeftValueModifier> lvms = lv.getModifiers().iterator();
        while (gvms.hasNext() && lvms.hasNext()) {
            final var gvm = gvms.next();
            final var lvm = lvms.next();
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
                fail("Unknown left value modifier class " + gvm.getClass());
            }
        }
    }

    private static void extressionHasSyntaxError(final String s)
            throws AnalysisException {
        try {
            compile(s);
            Assertions.fail("Syntax exception was not trown for '" + s + "'");
        } catch (final SyntaxException e) {
        }

    }

    private static void expressionCompilesTo(final String expression, final BasicLeftValue gv) throws AnalysisException {
        final var lv = compile(expression);
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

}
