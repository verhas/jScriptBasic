package com.scriptbasic.syntax.expression;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import junit.framework.TestCase;

import com.scriptbasic.executors.VariableAccess;
import com.scriptbasic.executors.operators.MultiplyOperator;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.BasicProgram;
import com.scriptbasic.syntax.BasicSyntaxAnalyzer;

public class TestBasicExpressionAnalyzer extends TestCase {

    private void testAlmaMultiplyByKorte(String s) throws SyntaxException {
        LexicalAnalyzer la = createStringReading(s);
        BasicSyntaxAnalyzer sa = new BasicSyntaxAnalyzer();
        sa.setLexicalAnalyzer(la);
        BasicExpressionAnalyzer bea = new BasicExpressionAnalyzer();
        BasicProgram program = new BasicProgram();
        sa.setProgram(program);
        bea.setSyntaxAnalyzer(sa);
        Expression e = bea.analyze();
        assertTrue(e instanceof MultiplyOperator);
        Expression leftOperand = ((MultiplyOperator) e).getLeftOperand();
        Expression rightOperand = ((MultiplyOperator) e).getRightOperand();
        assertTrue(leftOperand instanceof VariableAccess);
        assertTrue(rightOperand instanceof VariableAccess);
        assertTrue(((VariableAccess) leftOperand).getVariableName().equals(
                "alma"));
        assertTrue(((VariableAccess) rightOperand).getVariableName().equals(
                "korte"));
    }

    public void testAlmaTimesKorte() throws SyntaxException, LexicalException {
        testAlmaMultiplyByKorte("alma * korte");
        testAlmaMultiplyByKorte("(alma * korte)");
        testAlmaMultiplyByKorte("alma*korte  ");
        testAlmaMultiplyByKorte("(alma)*korte");
        testAlmaMultiplyByKorte("alma *( korte)");
    }

}
