package com.scriptbasic.api;

import com.scriptbasic.executors.rightvalues.BasicArrayValue;
import com.scriptbasic.interfaces.ExecutionException;

public interface BasicArray {

    static BasicArray create() {
        return new BasicArrayValue();
    }

    /**
     * Set the array object. This method is available as a convenience method
     * for extension methods and is not used by the interpreter. This method can
     * be used when the array is available from some calculation and it would be
     * waste of resource to copy the elements of the array one by one calling
     * {@link #set(Integer, Object)}.
     *
     * @param array the array
     * @throws NullPointerException when the array is null
     */
    void setArray(final Object[] array) throws ScriptBasicException;

    /**
     * Set the index-th element of the array
     *
     * @param index
     * @param object the new value for the array
     * @throws ExecutionException
     */
    public void set(final Integer index, final Object object) throws ExecutionException;

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
    Object get(final Integer index) throws ExecutionException;

    /**
     * Get the length of the array. This is not the length of the underlying
     * object array but the size that the BASIC program should feel.
     *
     * @return the length of the array, which is n+1, where n is the maximal
     * index of the array the BASIC program ever used.
     */
    long getLength();
}
