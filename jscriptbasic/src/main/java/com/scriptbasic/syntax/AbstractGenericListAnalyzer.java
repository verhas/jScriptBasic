/**
 * 
 */
package com.scriptbasic.syntax;

import static com.scriptbasic.syntax.LexFacade.get;
import static com.scriptbasic.syntax.LexFacade.peek;

import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.AnalysisResult;
import com.scriptbasic.interfaces.Analyzer;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.GenericList;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.ListAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;

/**
 * An abstract class to analyze a list of something. A list is several something
 * separated by commas.
 * 
 * @author Peter Verhas
 * @date June 14, 2012
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

    abstract public Factory getFactory();

    private K list;

    protected void setList(K list) {
        this.list = list;
    }

    private A analyzer;

    protected void setAnalyzer(A analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public T analyze() throws AnalysisException {
        list.add(analyzer.analyze());
        LexicalElement lexicalElement = peek(FactoryUtilities
                .getLexicalAnalyzer(getFactory()));
        while (isComma(lexicalElement)) {
            get(FactoryUtilities.getLexicalAnalyzer(getFactory()));
            list.add(analyzer.analyze());
            lexicalElement = peek(FactoryUtilities
                    .getLexicalAnalyzer(getFactory()));
        }
        return list;
    }

    private static boolean isComma(final LexicalElement lexicalElement) {
        return lexicalElement != null && lexicalElement.isSymbol(",");
    }
}
