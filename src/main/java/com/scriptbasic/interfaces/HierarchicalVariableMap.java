package com.scriptbasic.interfaces;


/**
 * A variable map interface that handles the global and local variables. The map
 * should be aware of the stack of the local variables of several levels and
 * also the global variables should be handled.
 * 
 * @author Peter Verhas
 * date July 12, 2012
 * 
 */
public interface HierarchicalVariableMap extends LocalVariableMap {
	VariableMap getGlobalMap();
}
