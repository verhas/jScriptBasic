package com.scriptbasic.interfaces;


/**
 * Classes that analyze something implement this interface.
 *
 * @param <T>
 * @author Peter Verhas
 * date June 15, 2012
 */
public interface Analyzer<T extends AnalysisResult> {

    /**
     * @return the analysis result.
     * @throws AnalysisException
     */
    T analyze() throws AnalysisException;
}
