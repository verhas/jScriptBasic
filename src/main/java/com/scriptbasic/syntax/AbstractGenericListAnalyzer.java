package com.scriptbasic.syntax;

import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.utility.LexUtility;

/**
 * An abstract class to analyze a list of something. A list is several something
 * separated by commas.
 *
 * @param <T> is the interface that defines the list of something
 * @param <K> is the implementation of T actually used to manage the list of the
 *            something
 * @param <Z> is the analyzed something that the analyzer {@code A} returns
 * @param <A> is the analyzer to analyze the something.
 * @author Peter Verhas
 * date June 14, 2012
 */
public abstract class AbstractGenericListAnalyzer<T extends GenericList<Z>, K extends T, Z extends AnalysisResult, A extends Analyzer<Z>>
        implements ListAnalyzer<T> {

    protected final Context ctx;

    protected AbstractGenericListAnalyzer(Context ctx) {
        this.ctx = ctx;
    }

    private static boolean isComma(final LexicalElement lexicalElement) {
        return lexicalElement != null && lexicalElement.isSymbol(",");
    }

    protected T analyze(K list, A analyzer) throws AnalysisException {
        list.add(analyzer.analyze());
        LexicalElement lexicalElement = LexUtility.peek(ctx.lexicalAnalyzer);
        while (isComma(lexicalElement)) {
            LexUtility.get(ctx.lexicalAnalyzer);
            list.add(analyzer.analyze());
            lexicalElement = LexUtility.peek(ctx.lexicalAnalyzer);
        }
        return list;
    }
}
