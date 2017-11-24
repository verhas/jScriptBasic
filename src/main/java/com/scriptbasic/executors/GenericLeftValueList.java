package com.scriptbasic.executors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.scriptbasic.interfaces.LeftValue;
import com.scriptbasic.interfaces.LeftValueList;

public class GenericLeftValueList implements LeftValueList {

	private final List<LeftValue> leftValueList = new LinkedList<>();

	@Override
	public void add(final LeftValue expression) {
		this.leftValueList.add(expression);
	}

	@Override
	public Iterator<LeftValue> iterator() {
		return this.leftValueList.iterator();
	}

    /* (non-Javadoc)
     * @see com.scriptbasic.interfaces.GenericList#length()
     */
    @Override
    public int size() {
        return leftValueList.size();
    }

}
