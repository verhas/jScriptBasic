/**
 * 
 */
package com.scriptbasic.executors.leftvalues;

import com.scriptbasic.interfaces.ExpressionList;

/**
 * @author Peter Verhas
 * @date June 13, 2012
 * 
 */
public class ArrayElementAccessLeftValueModifier extends LeftValueModifier {
	private ExpressionList indexList;

	public ExpressionList getIndexList() {
		return indexList;
	}

	public void setIndexList(ExpressionList indexList) {
		this.indexList = indexList;
	}
}
