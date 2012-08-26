package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.leftvalues.ArrayElementAccessLeftValueModifier;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.leftvalues.LeftValueModifier;
import com.scriptbasic.executors.leftvalues.ObjectFieldAccessLeftValueModifier;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExpressionList;
import com.scriptbasic.interfaces.ExpressionListAnalyzer;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LeftValue;
import com.scriptbasic.interfaces.LeftValueAnalyzer;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.utility.FactoryUtility;

/**
 * Left value is defined as
 * 
 * <pre>
 * LEFTVALUE ::= identifier modifier*
 * modifier  ::= '[' expression_list '] | '.' id
 * </pre>
 * 
 * @author Peter Verhas
 * date June 12, 2012
 */
public abstract class AbstractLeftValueAnalyzer implements LeftValueAnalyzer {
    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    private LeftValueModifier analyzeArrayAccess(LexicalAnalyzer lexicalAnalyzer)
            throws AnalysisException {
        lexicalAnalyzer.get();
        ArrayElementAccessLeftValueModifier lvm = new ArrayElementAccessLeftValueModifier();
        ExpressionListAnalyzer expressionListAnalyzer = FactoryUtility
                .getExpressionListAnalyzer(getFactory());

        ExpressionList indexList = expressionListAnalyzer.analyze();
        lvm.setIndexList(indexList);
        LexicalElement lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol("]")) {
            lexicalAnalyzer.get();
            return lvm;
        }
        throw new GenericSyntaxException(
                "Left value array access does not have ]", lexicalElement, null);
    }

    private static LeftValueModifier analyzeFieldAccess(
            LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        lexicalAnalyzer.get();
        ObjectFieldAccessLeftValueModifier lvm = new ObjectFieldAccessLeftValueModifier();
        LexicalElement lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isIdentifier()) {
            lexicalAnalyzer.get();
            lvm.setFieldName(lexicalElement.getLexeme());
            return lvm;
        }
        throw new GenericSyntaxException(
                "Left value . is not followed by a field name", lexicalElement,
                null);
    }

    private static boolean isModifierStart(LexicalElement lexicalElement) {
        return lexicalElement != null
                && (lexicalElement.isSymbol(".") || lexicalElement
                        .isSymbol("["));
    }

    private static boolean isArrayAccessStart(LexicalElement lexicalElement) {
        return lexicalElement != null && lexicalElement.isSymbol("[");
    }

    private static boolean isFieldAccessStart(LexicalElement lexicalElement) {
        return lexicalElement != null && lexicalElement.isSymbol(".");
    }

    @Override
    public LeftValue analyze() throws AnalysisException {
        BasicLeftValue leftValue = null;
        LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                .getLexicalAnalyzer(getFactory());
        LexicalElement lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement.isIdentifier()) {
            lexicalAnalyzer.get();
            leftValue = new BasicLeftValue();
            leftValue.setIdentifier(lexicalElement.getLexeme());
            lexicalElement = lexicalAnalyzer.peek();
            while (isModifierStart(lexicalElement)) {
                LeftValueModifier lvm = null;
                if (isArrayAccessStart(lexicalElement)) {
                    lvm = analyzeArrayAccess(lexicalAnalyzer);
                } else if (isFieldAccessStart(lexicalElement)) {
                    lvm = analyzeFieldAccess(lexicalAnalyzer);
                } else {
                    throw new BasicInterpreterInternalError(
                            "left value parsing internal error, there is a modifier with unknown type");
                }
                leftValue.addModifier(lvm);
                lexicalElement = lexicalAnalyzer.peek();
            }
        } else {
            throw new GenericSyntaxException(
                    "left value should start with an identifier",
                    lexicalElement, null);
        }
        return leftValue;
    }
}
