package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.AnalysisException;

/**
 * 
 * Classes that analyze something implement this interface.
 * 
 * @author Peter Verhas
 * @date June 15, 2012
 * 
 * @param <T>
 */
public interface Analyzer<T extends AnalysisResult> {
    
    /**
     * 
     * @return the analysis result.
     * 
     * @throws AnalysisException
     */
	T analyze() throws AnalysisException;
}
