package com.scriptbasic.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.ExpressionAnalyzer;
import com.scriptbasic.interfaces.ExpressionListAnalyzer;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.FactoryManaged;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.Program;
import com.scriptbasic.interfaces.SyntaxAnalyzer;
import com.scriptbasic.interfaces.TagAnalyzer;
import com.scriptbasic.syntax.expression.BasicExpressionListAnalyzer;

public class FactoryUtilities {
    private static Logger log = LoggerFactory.getLogger(FactoryUtilities.class);

    private static <T extends FactoryManaged> T get(Factory factory,
            Class<T> klass) {
        T object = factory.get(klass);
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

    public static <T extends FactoryManaged> void create(Factory factory,
            Class<T> interf4ce, Class<? extends T> klass) {
        factory.create(interf4ce, klass);
    }

    public static LexicalAnalyzer getLexicalAnalyzer(Factory factory) {
        log.debug("getting lexical analyzer from " + factory);
        return (LexicalAnalyzer) get(factory, LexicalAnalyzer.class);
    }

    public static Program getProgram(Factory factory) {
        return (Program) get(factory, Program.class);
    }

    public static SyntaxAnalyzer getSyntaxAnalyzer(Factory factory) {
        return (SyntaxAnalyzer) get(factory, SyntaxAnalyzer.class);
    }

    public static ExpressionAnalyzer getExpressionAnalyzer(Factory factory) {
        return (ExpressionAnalyzer) get(factory, ExpressionAnalyzer.class);
    }

    public static BasicExpressionListAnalyzer getExpressionListAnalyzer(
            Factory factory) {
        return (BasicExpressionListAnalyzer) get(factory,
                ExpressionListAnalyzer.class);
    }

    public static TagAnalyzer getTagAnalyzer(Factory factory) {
        return (TagAnalyzer) get(factory, TagAnalyzer.class);
    }
}
