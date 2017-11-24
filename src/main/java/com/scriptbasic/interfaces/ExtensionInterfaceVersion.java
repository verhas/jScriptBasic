package com.scriptbasic.interfaces;

import com.scriptbasic.api.Function;

/**
 * @author Peter Verhas date Jul 22, 2012
 * 
 */
public interface ExtensionInterfaceVersion {
	/**
	 * This constant defines the extension interface version. Whenever an
	 * interpreter tries to load an extension that requires a larger version
	 * than that the interpreter can provide the load of the extension will be
	 * refused.
	 * <p>
	 * If the version is the same it is OK.
	 * <p>
	 * If the required version is smaller then the interpreter may decide as
	 * described in the javadoc {@link Function#requiredVersion()}.
	 */
	long EXTENSION_INTERFACE_VERSION = 2L;
}
