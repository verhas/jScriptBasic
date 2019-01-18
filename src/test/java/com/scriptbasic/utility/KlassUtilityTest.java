package com.scriptbasic.utility;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Magic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KlassUtilityTest {

    @Test
    public void canGetFieldInMagicBean() throws BasicRuntimeException {
        class TestObject implements Magic.Getter {

            @Override
            public String get(final String name) {
                return name;
            }
        }
        final var testObject = new TestObject();
        Assertions.assertEquals("obj", KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canSetFieldInMagicBean() throws BasicRuntimeException {
        class TestObject implements Magic.Setter {
            Object value;
            String name;

            @Override
            public void set(final String name, final Object value) {
                this.name = name;
                this.value = value;
            }
        }
        final var testObject = new TestObject();
        final var objToSet = new Object();
        KlassUtility.setField(testObject, "obj", objToSet);
        Assertions.assertSame(objToSet, testObject.value);
        Assertions.assertEquals("obj", testObject.name);
    }

    @Test
    public void canSetFieldUsingSetter() throws BasicRuntimeException {
        class TestObject {
            private Object obj;

            public void setObj(final Object obj) {
                this.obj = obj;
            }
        }
        final var testObject = new TestObject();
        final var objToSet = new Object();
        KlassUtility.setField(testObject, "obj", objToSet);
        Assertions.assertSame(objToSet, testObject.obj);
    }

    @Test
    public void canSetFieldDirect() throws BasicRuntimeException {
        class TestObject {
            Object obj;
        }
        final var testObject = new TestObject();
        final var objToSet = new Object();
        KlassUtility.setField(testObject, "obj", objToSet);
        Assertions.assertSame(objToSet, testObject.obj);
    }

    @Test()
    public void doesNotSetPrivateFields() {
        class TestObject {
            private Object obj;
        }
        final var testObject = new TestObject();
        final var objToSet = new Object();
        Assertions.assertThrows(BasicRuntimeException.class, () ->
                KlassUtility.setField(testObject, "obj", objToSet));
    }

    @Test
    public void canGetFieldUsingGetter() throws BasicRuntimeException {
        final var objToSet = new Object();
        class TestObject {
            private final Object obj = objToSet;

            public Object getObj() {
                return obj;
            }
        }
        final var testObject = new TestObject();
        Assertions.assertSame(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canGetFieldDirect() throws BasicRuntimeException {
        final var objToSet = new Object();
        class TestObject {
            Object obj = objToSet;
        }
        final var testObject = new TestObject();
        Assertions.assertSame(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test()
    public void doesNotGetPrivateField() {
        final var objToSet = new Object();
        class TestObject {
            private Object obj = objToSet;
        }
        final var testObject = new TestObject();
        Assertions.assertThrows(BasicRuntimeException.class, () ->
                Assertions.assertSame(objToSet, KlassUtility.getField(testObject, "obj")));
    }

    @Test
    public void canGetFieldUsingIsGetter_boolean() throws BasicRuntimeException {
        final var objToSet = true;
        class TestObject {
            private final boolean obj = objToSet;

            public boolean isObj() {
                return obj;
            }
        }
        final var testObject = new TestObject();
        Assertions.assertEquals(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canGetFieldUsingIsGetter_Boolean() throws BasicRuntimeException {
        final var objToSet = true;
        class TestObject {
            private final Boolean obj = objToSet;

            public boolean isObj() {
                return obj;
            }
        }
        final var testObject = new TestObject();
        Assertions.assertEquals(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canGetFieldUsingIsGetter_boolean_ignoresGetXXX() throws BasicRuntimeException {
        final var objToSet = true;
        class TestObject {
            private final boolean obj = objToSet;

            public boolean getObj() {
                return obj;
            }

            public boolean isObj() {
                return obj;
            }
        }
        final var testObject = new TestObject();
        Assertions.assertEquals(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canGetFieldUsingIsGetter_Boolean_ignoresGetXXX() throws BasicRuntimeException {
        final var objToSet = true;
        class TestObject {
            private final Boolean obj = objToSet;

            public boolean getObj() {
                return obj;
            }

            public boolean isObj() {
                return obj;
            }
        }
        final var testObject = new TestObject();
        Assertions.assertEquals(objToSet, KlassUtility.getField(testObject, "obj"));
    }
}
