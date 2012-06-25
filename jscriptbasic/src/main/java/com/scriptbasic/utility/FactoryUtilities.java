package com.scriptbasic.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.ExpressionAnalyzer;
import com.scriptbasic.interfaces.ExpressionListAnalyzer;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.FactoryManaged;
import com.scriptbasic.interfaces.LeftValueAnalyzer;
import com.scriptbasic.interfaces.LeftValueListAnalyzer;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.interfaces.BuildableProgram;
import com.scriptbasic.interfaces.SyntaxAnalyzer;
import com.scriptbasic.interfaces.TagAnalyzer;

public class FactoryUtilities {

    private FactoryUtilities() {
    }

    private static final Logger log = LoggerFactory
            .getLogger(FactoryUtilities.class);

    private static <T extends FactoryManaged> T get(final Factory factory,
            final Class<T> klass) {
        final T object = factory.get(klass);
        if (object == null) {
            throw new BasicInterpreterInternalError(
                    "There was no object set for the " + klass);
        }
        if (!(klass.isInstance(object))) {
            throw new BasicInterpreterInternalError(
                    "Class mismatch in analyzer registration. "
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
        return get(factory, LexicalAnalyzer.class);
    }

    public static BuildableProgram getProgram(Factory factory) {
        return get(factory, BuildableProgram.class);
    }

    public static SyntaxAnalyzer getSyntaxAnalyzer(Factory factory) {
        return get(factory, SyntaxAnalyzer.class);
    }

    public static ExpressionAnalyzer getExpressionAnalyzer(Factory factory) {
        return get(factory, ExpressionAnalyzer.class);
    }

    public static ExpressionListAnalyzer getExpressionListAnalyzer(
            Factory factory) {
        return get(factory, ExpressionListAnalyzer.class);
    }

    public static TagAnalyzer getTagAnalyzer(Factory factory) {
        return get(factory, TagAnalyzer.class);
    }

    public static NestedStructureHouseKeeper getNestedStructureHouseKeeper(
            Factory factory) {
        return get(factory, NestedStructureHouseKeeper.class);
    }

    public static CommandFactory getCommandFactory(Factory factory) {
        return get(factory, CommandFactory.class);
    }

    public static LeftValueAnalyzer getLeftValueAnalyzer(Factory factory) {
        return get(factory, LeftValueAnalyzer.class);
    }

    public static LeftValueListAnalyzer getLeftValueListAnalyzer(Factory factory) {
        return get(factory, LeftValueListAnalyzer.class);
    }

    public static ExtendedInterpreter getExtendedInterpreter(Factory factory) {
        return get(factory, ExtendedInterpreter.class);
    }
}
