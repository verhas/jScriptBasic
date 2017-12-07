package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.api.BasicArray;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.interfaces.RightValue;

import java.util.Arrays;

public class BasicArrayValue implements RightValue, BasicArray {
    private static final Integer INCREMENT_GAP = 100;
    private Object[] array = new Object[INCREMENT_GAP];

    private int maxIndex = -1;
    // TODO implement a function in the interpreter that can limit the
    // allocation of arrays
    // perhaps only the size of the individual arrays
    private Interpreter interpreter;

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
    public BasicArrayValue(final Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public void setArray(final Object[] array) throws ScriptBasicException {
        if (array == null) {
            throw new ScriptBasicException("BasicArrayValue embedded array cannot be null");
        }
        this.array = objectArrayOf(array);
        maxIndex = array.length - 1;
    }

    private Object[] objectArrayOf(Object[] array) {
        final Object[] objectArray;
        if (array.getClass() == Object[].class) {
            objectArray = array;
        } else {
            objectArray = Arrays.copyOf(array, array.length, Object[].class);
        }
        return objectArray;
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
    public void setInterpreter(final Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    private void assertArraySize(final Integer index) throws ScriptBasicException {
        if (index < 0) {
            throw new BasicRuntimeException("Array index can not be negative");
        }
        if (array.length <= index) {
            array = Arrays.copyOf(array, index + INCREMENT_GAP);
        }
    }

    @Override
    public long getLength() {
        return (long) maxIndex + 1;
    }

    @Override
    public void set(final Integer index, final Object object) throws ScriptBasicException {
        assertArraySize(index);
        array[index] = object;
        if (maxIndex < index) {
            maxIndex = index;
        }
    }

    @Override
    public Object get(final Integer index) throws ScriptBasicException {
        assertArraySize(index);
        return array[index];
    }
}
