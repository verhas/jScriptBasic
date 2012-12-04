package com.scriptbasic.executors.rightvalues;

import java.util.Arrays;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;

public class BasicArrayValue extends AbstractRightValue {
	private static final Integer INCREMENT_GAP = 100;
	private Object[] array = new Object[INCREMENT_GAP];
	private int maxIndex = -1;
	// TODO implement a function in the interpreter that can limit the
	// allocation of arrays
	// perhaps only the size of the individual arrays
	private ExtendedInterpreter interpreter;

	public void setInterpreter(ExtendedInterpreter interpreter) {
		this.interpreter = interpreter;
	}

	public BasicArrayValue() {
		this.interpreter = null;
	}

	public BasicArrayValue(ExtendedInterpreter interpreter) {
		this.interpreter = interpreter;
	}

	private void assertArraySize(Integer index) throws ExecutionException {
		if (index < 0) {
			throw new BasicRuntimeException("Array index can not be negative");
		}
		if (array == null || array.length <= index) {
			array = Arrays.copyOf(array, index + INCREMENT_GAP);
		}
	}

	public Long getLength() {
		return new Long(maxIndex + 1);
	}

	/**
	 * Set the index-th element of the array
	 * 
	 * @param index
	 * @param object
	 *            the new value for the array
	 * @throws ExecutionException
	 */
	public void set(Integer index, Object object) throws ExecutionException {
		assertArraySize(index);
		array[index] = object;
		if (maxIndex < index) {
			maxIndex = index;
		}
	}

	public Object get(Integer index) throws ExecutionException {
		assertArraySize(index);
		return array[index];
	}
}
