package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;

import java.util.Arrays;

public class BasicArrayValue extends AbstractRightValue {
	private static final Integer INCREMENT_GAP = 100;
	private Object[] array = new Object[INCREMENT_GAP];

	private int maxIndex = -1;
	// TODO implement a function in the interpreter that can limit the
	// allocation of arrays
	// perhaps only the size of the individual arrays
	@SuppressWarnings("unused")
	private ExtendedInterpreter interpreter;

	/**
	 * Set the array object. This method is available as a convenience method
	 * for extension methods and is not used by the interpreter. This method can
	 * be used when the array is available from some calculation and it would be
	 * waste of resource to copy the elements of the array one by one calling
	 * {@link #set(Integer, Object)}.
	 * 
	 * @param array
	 *            the array
	 * 
	 * @throws NullPointerException
	 *             when the array is null
	 */
	public void setArray(Object[] array) {
		if (array == null) {
			throw new NullPointerException(
					"BasicArrayValue embedded array cann ot be null");
		}
		this.array = array;
	}

	/**
	 * Set the interpreter that this array belongs to.
	 * <p>
	 * Note that this method is used only from the code where the interpreter
	 * calls an extension method that returns a BasicArrayValue. In that case
	 * the parameter less constructor of this class is called by the extension
	 * method and thus the BasicArrayValue does not know the interpreter and can
	 * not request suggestion from the interpreter to perform resizing or throw
	 * exception.
	 * <p>
	 * When the parameterless version of the constructor becomes deprecated this
	 * setter will also become deprecated.
	 * 
	 * @param interpreter
	 */
	public void setInterpreter(ExtendedInterpreter interpreter) {
		this.interpreter = interpreter;
	}

	/**
	 * This constructor can be used by extension classes to instantiate a new
	 * BasicArrayValue when the extension function does not have access to the
	 * interpreter.
	 * <p>
	 * Note that in later versions this constructor will be deprecated as soon
	 * as the interface of the extensions will make it possible to pass the
	 * interpreter along to the extension methods.
	 */
	public BasicArrayValue() {
		this.interpreter = null;
	}

	/**
	 * Create a new BasicArrayValue and remember the interpreter.
	 * <p>
	 * The interpreter can determine the maximum size allowed for arrays and
	 * therefore may suggest for a BasicArrayValue not to extend its size, but
	 * rather throw exception. This is to prevent allocating extraordinary large
	 * arrays in an interpreter by mistake.
	 * 
	 * @param interpreter
	 */
	public BasicArrayValue(ExtendedInterpreter interpreter) {
		this.interpreter = interpreter;
	}

	private void assertArraySize(Integer index) throws ExecutionException {
		if (index < 0) {
			throw new BasicRuntimeException("Array index can not be negative");
		}
		if (array.length <= index) {
			array = Arrays.copyOf(array, index + INCREMENT_GAP);
		}
	}

	/**
	 * Get the length of the array. This is not the length of the underlying
	 * object array but the size that the BASIC program should feel.
	 * 
	 * @return the length of the array, which is n+1, where n is the maximal
	 *         index of the array the BASIC program ever used.
	 */
	public Long getLength() {
		return (long)maxIndex + 1;
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

	/**
	 * Get the {@code index}-th element of the array. Note that this method does
	 * NOT convert the value to an ordinary Java object. Thus when calling this
	 * method from an extension method be prepared to convert the value to
	 * ordinary Java object yourself.
	 * 
	 * @param index
	 * @return the array element.
	 * @throws ExecutionException
	 */
	public Object get(Integer index) throws ExecutionException {
		assertArraySize(index);
		return array[index];
	}
}
