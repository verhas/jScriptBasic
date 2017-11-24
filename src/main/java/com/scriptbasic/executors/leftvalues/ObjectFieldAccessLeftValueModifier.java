package com.scriptbasic.executors.leftvalues;

/**
 * @author Peter Verhas
 * date June 13, 2012
 * 
 */
public class ObjectFieldAccessLeftValueModifier extends LeftValueModifier {
	private String fieldName;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
