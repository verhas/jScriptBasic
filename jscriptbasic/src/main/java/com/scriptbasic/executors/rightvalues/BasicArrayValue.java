package com.scriptbasic.executors.rightvalues;
import java.util.Arrays;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
public class BasicArrayValue extends AbstractRightValue {
    private static final Integer INCREMENT_GAP = 100;
    private Object[] array = new Object[INCREMENT_GAP];
    private void assertArraySize(Integer index) throws ExecutionException {
        if (index < 0) {
            throw new BasicRuntimeException("Array index can not be negative");
        }
        if (array == null || array.length <= index) {
            array = Arrays.copyOf(array, index + INCREMENT_GAP);
        }
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
    }
    public Object get(Integer index) throws ExecutionException {
        assertArraySize(index);
        return array[index];
    }
}