package com.scriptbasic.interfaces;

import com.scriptbasic.api.BasicRuntimeException;

/**
 * When a BASIC program has a reference to an object that is an instance of a class that implements Magic.Bean
 * setting a field and getting the value of a field is done through the "magic" methods defined in this interface
 * instead of setting the field or getting the value of the field through the getters/setters or accessing the
 * fields directly.
 */
public interface Magic {

    interface Getter {
        Object get(String fieldName) throws BasicRuntimeException;
    }

    interface Setter {
        void set(String fieldName, Object value) throws BasicRuntimeException;
    }

    interface Bean extends Getter, Setter {
    }
}
