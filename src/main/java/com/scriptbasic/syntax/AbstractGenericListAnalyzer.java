package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.AnalysisResult;
import com.scriptbasic.interfaces.Analyzer;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.GenericList;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.ListAnalyzer;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.LexUtility;

/**
 * An abstract class to analyze a list of something. A list is several something
 * separated by commas.
 * 
 * @author Peter Verhas
 * date June 14, 2012
 * 
 * @param <T>
 *            is the interface that defines the list of something
 * @param <K>
 *            is the implementation of T actually used to manage the list of the
 *            something
 * @param <Z>
 *            is the analyzed something that the analyzer {@code A} returns
 * @param <A>
 *            is the analyzer to analyze the something.
 */
public abstract class AbstractGenericListAnalyzer<T extends GenericList<Z>, K extends T, Z extends AnalysisResult, A extends Analyzer<Z>>
        implements ListAnalyzer<T> {

    public abstract Factory getFactory();

    protected T analyze(K list, A analyzer) throws AnalysisException {
        list.add(analyzer.analyze());
        LexicalElement lexicalElement = LexUtility.peek(FactoryUtility
                .getLexicalAnalyzer(getFactory()));
        while (isComma(lexicalElement)) {
            LexUtility.get(FactoryUtility.getLexicalAnalyzer(getFactory()));
            list.add(analyzer.analyze());
            lexicalElement = LexUtility.peek(FactoryUtility
                    .getLexicalAnalyzer(getFactory()));
        }
        return list;
    }

    private static boolean isComma(final LexicalElement lexicalElement) {
        return lexicalElement != null && lexicalElement.isSymbol(",");
    }
}
