package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.lexer.BasicLexicalAnalyzer;
import com.scriptbasic.lexer.elements.ScriptBasicLexicalAnalyzer;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.readers.GenericHierarchicalSourceReader;

import java.io.IOException;

public final class FactoryUtility {

    private FactoryUtility() {
        NoInstance.isPossible();
    }

    private static final Logger LOG = LoggerFactory
            .getLogger();

    private static <T> T get(final Factory factory,
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

    public static LexicalAnalyzer getLexicalAnalyzer(SourceProvider provider, String fileName) throws IOException {
        final SourceReader reader = provider.get(fileName);
        final GenericHierarchicalSourceReader hreader = new GenericHierarchicalSourceReader(reader);
        return new ScriptBasicLexicalAnalyzer(hreader);
    }

    public static LexicalAnalyzer getLexicalAnalyzer(Factory factory) {
        LOG.debug("getting lexical analyzer from {}", factory);
        return get(factory, LexicalAnalyzer.class);
    }


    public static SyntaxAnalyzer getSyntaxAnalyzer(Factory factory) {
        LOG.debug("getting syntax analyzer from {}", factory);
        return get(factory, SyntaxAnalyzer.class);
    }

    public static ExpressionAnalyzer getExpressionAnalyzer(Factory factory) {
        LOG.debug("getting expression analyzer from {}", factory);
        return get(factory, ExpressionAnalyzer.class);
    }

    public static ExpressionListAnalyzer getExpressionListAnalyzer(
            Factory factory) {
        LOG.debug("getting expression list analyzer from {}", factory);
        return get(factory, ExpressionListAnalyzer.class);
    }

    public static TagAnalyzer getTagAnalyzer(Factory factory) {
        LOG.debug("getting tag analyzer from {}", factory);
        return get(factory, TagAnalyzer.class);
    }

    public static NestedStructureHouseKeeper getNestedStructureHouseKeeper(
            Factory factory) {
        LOG.debug("getting nested structure house keeper from {}", factory);
        return get(factory, NestedStructureHouseKeeper.class);
    }

    public static LeftValueAnalyzer getLeftValueAnalyzer(Factory factory) {
        LOG.debug("getting left value analyzer from {}", factory);
        return get(factory, LeftValueAnalyzer.class);
    }

    public static LeftValueListAnalyzer getLeftValueListAnalyzer(Factory factory) {
        LOG.debug("getting left value list analyzer from {}", factory);
        return get(factory, LeftValueListAnalyzer.class);
    }

    public static ExtendedInterpreter getExtendedInterpreter(Factory factory) {
        LOG.debug("getting extended interpreter analyzer from {}", factory);
        return get(factory, ExtendedInterpreter.class);
    }

    public static SimpleLeftValueAnalyzer getSimpleLeftValueAnalyzer(
            Factory factory) {
        LOG.debug("getting simple leftvalue analyzer from {}", factory);
        return get(factory, SimpleLeftValueAnalyzer.class);
    }

    public static Configuration getConfiguration(Factory factory) {
        LOG.debug("getting configuration from {}", factory);
        return get(factory, Configuration.class);
    }

    /**
     * @param factory
     * @return
     */
    public static SimpleLeftValueListAnalyzer getSimpleLeftValueListAnalyzer(
            Factory factory) {
        LOG.debug("getting simple leftvalue listanalyzer from {}", factory);
        return get(factory, SimpleLeftValueListAnalyzer.class);
    }
}
