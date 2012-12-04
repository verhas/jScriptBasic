package com.scriptbasic.utility.functions;

import com.scriptbasic.utility.UtilityUtility;

/**
 * This class implements static methods, which can be used from BASIC programs
 * to access the file system.
 * <p>
 * Note that these functions are NOT registered into the BASIC interpreter by default. The
 * embedding application has to ask the interpreter to register the methods of this class if
 * it wants BASIC programs access the file system.
 * @author Peter Verhas
 * 
 */
public class FileHandlingFunctions {
	private FileHandlingFunctions() {
		UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
	}
}
