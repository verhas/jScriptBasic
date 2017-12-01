package com.scriptbasic.interfaces;

/**
 * When a BASIC extension function implemented in Java returns an object for the sole purpose that it can be passed
 * as argument to other extension functions implemented in Java (and probably in the same class), but it does not
 * want the BASIC program to get or set any field or invoke any method from this object then the return value
 * can be embedded in a {@code NoAccessProxy}. Since this proxy implements the NoAccess interface the BASIC program
 * will not be able to access the field 'target'.
 *
 * @param <T>
 */
public class NoAccessProxy<T> implements NoAccess {
    public T target;
}
