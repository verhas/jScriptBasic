package com.scriptbasic.interfaces;


/**
 * Classes that analyze something implement this interface.
 *
 * @param <T> the type of the result of the analysis
 * @author Peter Verhas
 * date June 15, 2012
 */
public interface Analyzer<T extends AnalysisResult> {

    /**
     * @return the analysis result.
     * @throws AnalysisException in case of exception
     */
    T analyze() throws AnalysisException;
}
