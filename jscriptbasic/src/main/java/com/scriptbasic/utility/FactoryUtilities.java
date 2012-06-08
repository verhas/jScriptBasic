package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.factories.FactoryFactory;
import com.scriptbasic.interfaces.ExpressionAnalyzer;
import com.scriptbasic.interfaces.ExpressionListAnalyzer;
import com.scriptbasic.interfaces.FactoryManaged;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.Program;
import com.scriptbasic.interfaces.SyntaxAnalyzer;
import com.scriptbasic.interfaces.TagAnalyzer;
import com.scriptbasic.syntax.expression.BasicExpressionListAnalyzer;

public class FactoryUtilities {

    private static <T extends FactoryManaged> T get(Class<T> klass) {
        T object = FactoryFactory.getFactory().get(klass);
        if (object == null) {
            throw new BasicInterpreterInternalError(
                    "There was no object set for the " + klass);
        }
        if (!(klass.isInstance(object))) {
            throw new BasicInterpreterInternalError(
                    "Class mismatch in thread analyzer registration. "
                            + object.getClass()
                            + " as registered is not the same as the expected "
                            + klass);
        }
        return object;
    }

    public static <T extends FactoryManaged> void create(Class<T> interf4ce,
            Class<? extends T> klass) {
        FactoryFactory.getFactory().create(interf4ce, klass);
    }

    public static LexicalAnalyzer getLexicalAnalyzer() {
        return (LexicalAnalyzer) get(LexicalAnalyzer.class);
    }

    public static Program getProgram() {
        return (Program) get(Program.class);
    }

    public static SyntaxAnalyzer getSyntaxAnalyzer() {
        return (SyntaxAnalyzer) get(SyntaxAnalyzer.class);
    }

    public static ExpressionAnalyzer getExpressionAnalyzer() {
        return (ExpressionAnalyzer) get(ExpressionAnalyzer.class);
    }

    public static BasicExpressionListAnalyzer getExpressionListAnalyzer() {
        return (BasicExpressionListAnalyzer) get(ExpressionListAnalyzer.class);
    }

    public static TagAnalyzer getTagAnalyzer() {
        return (TagAnalyzer) get(TagAnalyzer.class);
    }

}
