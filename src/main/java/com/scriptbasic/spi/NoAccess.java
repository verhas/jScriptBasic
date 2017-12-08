package com.scriptbasic.spi;

/**
 * When a BASIC extension function implemented in Java returns an object for the sole purpose that it can be passed
 * as argument to other extension functions implemented in Java (and probably in the same class), but it does not
 * want the BASIC program to get or set any field or invoke any method from this object then the return value
 * can be of a type that implements this interface. If ever any BASIC variable holds an object of a type
 * that implements this interface then the BASIC program will not be allowed to access that object other than it can
 * pass it back to some other extension function.
 * <p>
 * When the returned value is of a type that can not implement this interface then the {@link NoAccessProxy} should
 * be used as a bit more complex solution. This can happen when the returned value is of a type that is implemented
 * in some library the application uses and the modification of the type to implement this interface is not feasible.
 */
public interface NoAccess {
}
