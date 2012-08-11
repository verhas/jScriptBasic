package com.scriptbasic.syntax.leftvalue;
import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import static com.scriptbasic.syntax.expression.ExpressionBuilder.ID;
import static com.scriptbasic.syntax.expression.ExpressionBuilder.LONG;
import static com.scriptbasic.syntax.expression.ExpressionBuilder.OBJECT_FIELD;
import static com.scriptbasic.syntax.expression.ExpressionBuilder.array;
import static com.scriptbasic.syntax.expression.ExpressionBuilder.multiply;
import java.util.Iterator;
import junit.framework.TestCase;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.executors.GenericLeftValueList;
import com.scriptbasic.executors.leftvalues.ArrayElementAccessLeftValueModifier;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.leftvalues.LeftValueModifier;
import com.scriptbasic.executors.leftvalues.ObjectFieldAccessLeftValueModifier;
import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LeftValueAnalyzer;
import com.scriptbasic.interfaces.LeftValueListAnalyzer;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.syntax.expression.ExpressionComparator;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.LexUtility;
public class TestBasicLeftValueAnalyzer extends TestCase {
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
    private static void testSyntaxExceptionLeftValue(String s)
            throws AnalysisException {
        try {
            compile(s);
            assertTrue("Syntax exception was not trown for '" + s + "'", false);
        } catch (SyntaxException e) {
        }
    }
    @SuppressWarnings("static-method")
    public void testLeftValues() throws Exception {
        BasicLeftValue lv;
        BasicLeftValue gv;
        lv = compile("apple[3].apple");
        gv = new LeftValueBuilder("apple").array(LONG(3L)).field("apple").x();
        compare(gv, lv);
        lv = compile("apple");
        gv = new LeftValueBuilder("apple").x();
        compare(gv, lv);
        lv = compile("apple.sheep");
        gv = new LeftValueBuilder("apple").field("sheep").x();
        compare(gv, lv);
        lv = compile("com.scriptbasic.syntax.leftvalue");
        gv = new LeftValueBuilder("com").field("scriptbasic").field("syntax")
                .field("leftvalue").x();
        compare(gv, lv);
        lv = compile("apple[3]");
        gv = new LeftValueBuilder("apple").array(LONG(3L)).x();
        compare(gv, lv);
        lv = compile("apple[3,5]");
        gv = new LeftValueBuilder("apple").array(LONG(3L), LONG(5L)).x();
        compare(gv, lv);
        lv = compile("apple[3,apple]");
        gv = new LeftValueBuilder("apple").array(LONG(3L), ID("apple")).x();
        compare(gv, lv);
        lv = compile("apple[3][apple]");
        gv = new LeftValueBuilder("apple").array(LONG(3L)).array(ID("apple"))
                .x();
        compare(gv, lv);
        lv = compile("apple[3].apple.bbb[cc,dd.qq[4]*3]");
        gv = new LeftValueBuilder("apple")
                .array(LONG(3L))
                .field("apple")
                .field("bbb")
                .array(ID("cc"),
                        multiply(OBJECT_FIELD(ID("DD"), array("qq", LONG(4L))),
                                LONG(3L))).x();
        testSyntaxExceptionLeftValue("apple.");
        testSyntaxExceptionLeftValue("apple[]");
        testSyntaxExceptionLeftValue("apple..");
        testSyntaxExceptionLeftValue("apple[");
        testSyntaxExceptionLeftValue("apple]");
        testSyntaxExceptionLeftValue("apple+3");
        testSyntaxExceptionLeftValue("apple[3");
        testSyntaxExceptionLeftValue("apple[3)");
        testSyntaxExceptionLeftValue("apple[3aaa");
        testSyntaxExceptionLeftValue("[");
        testSyntaxExceptionLeftValue("apple[hat.het+\"kaka\".z");
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
    @SuppressWarnings("static-method")
    public void testLeftValueLists() throws Exception {
        compileList("a,b,c,d");
    }
}