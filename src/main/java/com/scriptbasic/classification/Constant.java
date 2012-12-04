/**
 * 
 */
package com.scriptbasic.classification;

/**
 * Functions that let the BASIC program to access some special constant values
 * should be annotated using this interface as classification parameter.
 * <p>
 * The best example of these functions is the {@see
 * RuntimeUtility#nullFunction()} that returns a {@code null} values.
 * <p>
 * Functions classified using these value should be considered exceptionally
 * harmless.
 * 
 * @author Peter Verhas date Jul 23, 2012
 * 
 */
public interface Constant {

}
