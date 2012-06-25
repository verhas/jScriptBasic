package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.executors.GenericExpressionList;
import com.scriptbasic.executors.leftvalues.ArrayElementAccessLeftValueModifier;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.leftvalues.ObjectFieldAccessLeftValueModifier;
import com.scriptbasic.interfaces.Expression;

public class LeftValueBuilder {
	private BasicLeftValue lv = new BasicLeftValue();

	public LeftValueBuilder(String id) {
		lv.setIdentifier(id);
	}

	public LeftValueBuilder field(String id){
		ObjectFieldAccessLeftValueModifier modifier = new ObjectFieldAccessLeftValueModifier();
		modifier.setFieldName(id);
		lv.addModifier(modifier);
		return this;
	}
	
	public LeftValueBuilder array(Expression ... indexArray){
		ArrayElementAccessLeftValueModifier modifier = new ArrayElementAccessLeftValueModifier();
		GenericExpressionList expressionList = new GenericExpressionList();
		for( Expression e : indexArray){
			expressionList.add(e);
		}
		modifier.setIndexList(expressionList);
		lv.addModifier(modifier);
		return this;
	}
		
	public BasicLeftValue x(){
		return lv;
	}
}
