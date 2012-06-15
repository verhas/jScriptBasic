package com.scriptbasic.executors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.scriptbasic.interfaces.LeftValue;
import com.scriptbasic.interfaces.LeftValueList;

public class GenericLeftValueList implements LeftValueList {

	List<LeftValue> expressionList = new LinkedList<LeftValue>();

	@Override
	public void add(final LeftValue expression) {
		this.expressionList.add(expression);
	}

	@Override
	public Iterator<LeftValue> iterator() {
		return this.expressionList.iterator();
	}

}
