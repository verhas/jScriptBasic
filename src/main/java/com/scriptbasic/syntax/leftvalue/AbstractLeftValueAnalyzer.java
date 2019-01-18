package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.context.Context;
import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.executors.leftvalues.ArrayElementAccessLeftValueModifier;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.leftvalues.LeftValueModifier;
import com.scriptbasic.executors.leftvalues.ObjectFieldAccessLeftValueModifier;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.spi.LeftValue;

/**
 * Left value is defined as
 * <pre>
 * LEFTVALUE ::= identifier modifier*
 * modifier  ::= '[' expression_list '] | '.' id
 * </pre>
 *
 * @author Peter Verhas
 * date June 12, 2012
 */
public abstract class AbstractLeftValueAnalyzer implements LeftValueAnalyzer {
    protected final Context ctx;

    protected AbstractLeftValueAnalyzer(final Context ctx) {
        this.ctx = ctx;
    }

    private static LeftValueModifier analyzeFieldAccess(
            final LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        lexicalAnalyzer.get();
        final var lvm = new ObjectFieldAccessLeftValueModifier();
        final var lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isIdentifier()) {
            lexicalAnalyzer.get();
            lvm.setFieldName(lexicalElement.getLexeme());
            return lvm;
        }
        throw new BasicSyntaxException(
                "Left value . is not followed by a field name", lexicalElement,
                null);
    }

    private static boolean isModifierStart(final LexicalElement lexicalElement) {
        return lexicalElement != null
                && (lexicalElement.isSymbol(".") || lexicalElement
                .isSymbol("["));
    }

    private static boolean isArrayAccessStart(final LexicalElement lexicalElement) {
        return lexicalElement != null && lexicalElement.isSymbol("[");
    }

    private static boolean isFieldAccessStart(final LexicalElement lexicalElement) {
        return lexicalElement != null && lexicalElement.isSymbol(".");
    }

    private LeftValueModifier analyzeArrayAccess(final LexicalAnalyzer lexicalAnalyzer)
            throws AnalysisException {
        lexicalAnalyzer.get();
        final var lvm = new ArrayElementAccessLeftValueModifier();

        final var indexList = ctx.expressionListAnalyzer.analyze();
        lvm.setIndexList(indexList);
        final var lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol("]")) {
            lexicalAnalyzer.get();
            return lvm;
        }
        throw new BasicSyntaxException(
                "Left value array access does not have ]", lexicalElement, null);
    }

    @Override
    public LeftValue analyze() throws AnalysisException {
        final BasicLeftValue leftValue;
        LexicalElement lexicalElement = ctx.lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isIdentifier()) {
            ctx.lexicalAnalyzer.get();
            leftValue = new BasicLeftValue();
            leftValue.setIdentifier(lexicalElement.getLexeme());
            lexicalElement = ctx.lexicalAnalyzer.peek();
            while (isModifierStart(lexicalElement)) {
                final LeftValueModifier modifier;
                if (isArrayAccessStart(lexicalElement)) {
                    modifier = analyzeArrayAccess(ctx.lexicalAnalyzer);
                } else if (isFieldAccessStart(lexicalElement)) {
                    modifier = analyzeFieldAccess(ctx.lexicalAnalyzer);
                } else {
                    throw new BasicInterpreterInternalError(
                            "left value parsing internal error, there is a modifier with unknown type");
                }
                leftValue.addModifier(modifier);
                lexicalElement = ctx.lexicalAnalyzer.peek();
            }
        } else {
            throw new BasicSyntaxException(
                    "left value should start with an identifier",
                    lexicalElement, null);
        }
        return leftValue;
    }
}
