package com.scriptbasic.utility;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Magic;
import org.junit.Assert;
import org.junit.Test;

public class KlassUtilityTest {

    @Test
    public void canGetFieldInMagicBean() throws BasicRuntimeException {
        class TestObject implements Magic.Getter {

            @Override
            public String get(String name) {
                return name;
            }
        }
        TestObject testObject = new TestObject();
        Assert.assertEquals("obj", KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canSetFieldInMagicBean() throws BasicRuntimeException {
        class TestObject implements Magic.Setter {
            Object value;
            String name;

            @Override
            public void set(String name, Object value) {
                this.name = name;
                this.value = value;
            }
        }
        TestObject testObject = new TestObject();
        Object objToSet = new Object();
        KlassUtility.setField(testObject, "obj", objToSet);
        Assert.assertSame(objToSet, testObject.value);
        Assert.assertEquals("obj", testObject.name);
    }
    @Test
    public void canSetFieldUsingSetter() throws BasicRuntimeException {
        class TestObject {
            private Object obj;

            public void setObj(Object obj) {
                this.obj = obj;
            }
        }
        TestObject testObject = new TestObject();
        Object objToSet = new Object();
        KlassUtility.setField(testObject, "obj", objToSet);
        Assert.assertSame(objToSet, testObject.obj);
    }

    @Test
    public void canSetFieldDirect() throws BasicRuntimeException {
        class TestObject {
            Object obj;
        }
        TestObject testObject = new TestObject();
        Object objToSet = new Object();
        KlassUtility.setField(testObject, "obj", objToSet);
        Assert.assertSame(objToSet, testObject.obj);
    }

    @Test(expected = BasicRuntimeException.class)
    public void doesNotSetPrivateFields() throws BasicRuntimeException {
        class TestObject {
            private Object obj;
        }
        TestObject testObject = new TestObject();
        Object objToSet = new Object();
        KlassUtility.setField(testObject, "obj", objToSet);
    }

    @Test
    public void canGetFieldUsingGetter() throws BasicRuntimeException {
        final Object objToSet = new Object();
        class TestObject {
            private Object obj = objToSet;

            public Object getObj() {
                return obj;
            }
        }
        TestObject testObject = new TestObject();
        Assert.assertSame(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canGetFieldDirect() throws BasicRuntimeException {
        final Object objToSet = new Object();
        class TestObject {
            Object obj = objToSet;
        }
        TestObject testObject = new TestObject();
        Assert.assertSame(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test(expected = BasicRuntimeException.class)
    public void doesNotGetPrivateField() throws BasicRuntimeException {
        final Object objToSet = new Object();
        class TestObject {
            private Object obj = objToSet;
        }
        TestObject testObject = new TestObject();
        Assert.assertSame(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canGetFieldUsingIsGetter_boolean() throws BasicRuntimeException {
        final boolean objToSet = true;
        class TestObject {
            private boolean obj = objToSet;

            public boolean isObj() {
                return obj;
            }
        }
        TestObject testObject = new TestObject();
        Assert.assertEquals(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canGetFieldUsingIsGetter_Boolean() throws BasicRuntimeException {
        final boolean objToSet = true;
        class TestObject {
            private Boolean obj = objToSet;

            public boolean isObj() {
                return obj;
            }
        }
        TestObject testObject = new TestObject();
        Assert.assertEquals(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canGetFieldUsingIsGetter_boolean_ignoresGetXXX() throws BasicRuntimeException {
        final boolean objToSet = true;
        class TestObject {
            private boolean obj = objToSet;

            public boolean getObj() {
                return obj;
            }

            public boolean isObj() {
                return obj;
            }
        }
        TestObject testObject = new TestObject();
        Assert.assertEquals(objToSet, KlassUtility.getField(testObject, "obj"));
    }

    @Test
    public void canGetFieldUsingIsGetter_Boolean_ignoresGetXXX() throws BasicRuntimeException {
        final boolean objToSet = true;
        class TestObject {
            private Boolean obj = objToSet;

            public boolean getObj() {
                return obj;
            }

            public boolean isObj() {
                return obj;
            }
        }
        TestObject testObject = new TestObject();
        Assert.assertEquals(objToSet, KlassUtility.getField(testObject, "obj"));
    }
}
