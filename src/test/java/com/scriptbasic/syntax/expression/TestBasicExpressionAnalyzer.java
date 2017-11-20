package com.scriptbasic.syntax.expression;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.LexUtility;
import org.junit.Test;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import static com.scriptbasic.syntax.expression.ExpressionBuilder.*;
import static org.junit.Assert.assertTrue;


public class TestBasicExpressionAnalyzer {

    private static final Expression[] nullExpression = null;
    private static Factory factory = new BasicFactory();

    private static Expression compile(final String s) throws AnalysisException {
        factory.clean();
        final LexicalAnalyzer la = createStringReading(factory, s);
        final BasicExpressionAnalyzer bea = (BasicExpressionAnalyzer) FactoryUtility
                .getExpressionAnalyzer(factory);
        final Expression e = bea.analyze();
        if (LexUtility.peek(la) != null) {
            throw new GenericSyntaxException(
                    "There are extra lexemes after the expression: "
                            + LexUtility.peek(la).getLexeme());
        }
        return e;
    }

    private static void testAppleMultiplyByPeach(final String s)
            throws Exception {
        ExpressionComparator.assertEqual(compile(s),
                multiply(variable("apple"), variable("peach")));
    }

    @Test
    public void testAppleTimesPeach() throws Exception {
        testAppleMultiplyByPeach("apple * peach");
        testAppleMultiplyByPeach("(apple * peach)");
        testAppleMultiplyByPeach("apple*peach  ");
        testAppleMultiplyByPeach("(apple)*peach");
        testAppleMultiplyByPeach("apple *( peach)");
    }

    public void testCorrectExpressions() throws Exception {
        new ExpressionBuilder(); // just for the coverage

        ExpressionComparator.assertEqual(compile("-not 3"),
                unaryMinus(not(LONG(3L))));

        ExpressionComparator.assertEqual(compile("1+2+3"),
                add(add(LONG(1L), LONG(2L)), LONG(3L)));

        ExpressionComparator.assertEqual(compile("apple[23]"),
                array("apple", LONG(23L)));

        ExpressionComparator.assertEqual(
                compile("apple[23,\"string\",44.2,identif]"),
                array("apple", LONG(23L), STRING("string"), DOUBLE(44.2),
                        ID("identif")));

        ExpressionComparator.assertEqual(compile("apple.pie"),
                OBJECT_FIELD("apple", "pie"));

        ExpressionComparator.assertEqual(compile("apple.pie()"),
                OBJECT_FIELD("apple", func("pie", nullExpression)));

        ExpressionComparator.assertEqual(compile("apple.pie(12)"),
                OBJECT_FIELD("apple", func("pie", LONG(12L))));

        ExpressionComparator.assertEqual(compile("id(apple).(pie)"),
                OBJECT_FIELD(func("id", ID("apple")), variable("pie")));

        ExpressionComparator.assertEqual(
                compile("id(apple).pie()"),
                OBJECT_FIELD(func("id", ID("apple")),
                        func("pie", nullExpression)));

        ExpressionComparator.assertEqual(compile("id(apple).(pie)"),
                OBJECT_FIELD(func("id", ID("apple")), ID("pie")));

        ExpressionComparator
                .assertEqual(
                        compile("id(apple).(pie+1)"),
                        OBJECT_FIELD(func("id", ID("apple")),
                                add(ID("pie"), LONG(1L))));

        ExpressionComparator.assertEqual(
                compile("apple.pie(23,\"string\",44.2,identif)"),
                OBJECT_FIELD(
                        "apple",
                        func("pie", LONG(23L), STRING("string"), DOUBLE(44.2),
                                ID("identif"))));
        ExpressionComparator.assertEqual(compile("33+12*\"abraka dabra\""),
                add(LONG(33L), multiply(LONG(12L), STRING("abraka dabra"))));
        ExpressionComparator.assertEqual(compile("12"), LONG(12L));
        ExpressionComparator
                .assertEqual(compile("not false"), not(BOOL(false)));

    }

    public void testFaultyExpressions() throws Exception {
        final String[] expressions = new String[]{"abraka dabra", "1+\"",
                "andara{12}", "No+hay+mas+de(weinte)'", "apple(pie",
                "apple[pie", "not", "alma+(3+2", "not )"};

        for (final String s : expressions) {
            try {
                compile(s);
                assertTrue(false);
            } catch (final SyntaxException e) {
            }
        }
    }
}
