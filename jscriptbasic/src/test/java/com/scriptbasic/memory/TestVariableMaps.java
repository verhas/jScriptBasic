/**
 * 
 */
package com.scriptbasic.memory;

import java.util.EmptyStackException;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.RightValue;

/**
 * @author Peter Verhas
 * @date June 23, 2012
 * 
 */
public class TestVariableMaps extends TestCase {

    private static Logger log = LoggerFactory.getLogger(TestVariableMaps.class);

    public static void testBasicLocalVariableMap() throws ExecutionException {
        BasicLocalVariableMap blvm = new BasicLocalVariableMap();

        // global scope
        assertNull(blvm.getVariableValue("noVariablesAre"));
        assertFalse(blvm.variableDefined("noVariablesAre"));
        assertFalse(blvm.variableExists("noVariablesAre"));

        // local scope 1
        blvm.newFrame();
        blvm.setVariable("var1", new BasicDoubleValue(5.5));
        assertTrue(blvm.variableExists("var1"));
        assertTrue(blvm.variableDefined("var1"));
        assertFalse(blvm.variableExists("var2"));
        assertFalse(blvm.variableDefined("var2"));
        assertEquals(5.5,
                (Double) ((AbstractPrimitiveRightValue<?>) blvm
                        .getVariableValue("var1")).getValue(), 0.0);
        blvm.setVariable("var3", null);
        assertTrue(blvm.variableExists("var3"));
        assertFalse(blvm.variableDefined("var3"));
        assertFalse(blvm.variableExists("var4"));
        assertNull(blvm.getVariableValue("var4"));

        // local scope 1-1
        blvm.newFrame();
        blvm.setVariable("var2", new BasicStringValue("avestruc"));
        assertTrue(blvm.variableExists("var2"));
        assertTrue(blvm.variableDefined("var2"));
        assertFalse(blvm.variableExists("var1"));
        assertFalse(blvm.variableDefined("var1"));
        blvm.dropFrame();

        // local scope 1
        assertTrue(blvm.variableExists("var1"));
        assertTrue(blvm.variableDefined("var1"));
        assertFalse(blvm.variableExists("var2"));
        assertFalse(blvm.variableDefined("var2"));
        assertEquals(5.5,
                (Double) ((AbstractPrimitiveRightValue<?>) blvm
                        .getVariableValue("var1")).getValue(), 0.0);
        assertTrue(blvm.variableExists("var3"));
        assertFalse(blvm.variableDefined("var3"));
        blvm.dropFrame();

        // global scope
        try {
            // can not set local variable since we are in global scope
            // will throw IllegalArgumentException
            blvm.setVariable("var1", new BasicLongValue(5L));
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
        }
        try {
            blvm.dropFrame();
            assertFalse(true);
        } catch (EmptyStackException e) {
        }
    }

    MixedBasicVariableMap blvm = null;

    private void VE(String name) throws ExecutionException {
        assertTrue(blvm.variableExists(name));
    }

    private void VNE(String name) throws ExecutionException {
        assertFalse(blvm.variableExists(name));
    }

    private void VD(String name) throws ExecutionException {
        assertTrue(blvm.variableDefined(name));
    }

    private void VND(String name) throws ExecutionException {
        assertFalse(blvm.variableDefined(name));
    }

    private void LET(String name, Object v) throws ExecutionException {
        SET: {
            if (v == null) {
                blvm.setVariable(name, null);
                break SET;
            }
            if (v instanceof RightValue) {
                blvm.setVariable(name, (RightValue) v);
                break SET;
            }
            if (v instanceof Double) {
                blvm.setVariable(name, new BasicDoubleValue((Double) v));
                break SET;
            }
            if (v instanceof Long) {
                blvm.setVariable(name, new BasicLongValue((Long) v));
                break SET;
            }
            if (v instanceof Integer) {
                blvm.setVariable(name,
                        new BasicLongValue(((Integer) v).longValue()));
                break SET;
            }
            if (v instanceof String) {
                blvm.setVariable(name, new BasicStringValue((String) v));
                break SET;
            }
        }
    }

    private void PUSH() {
        blvm.newFrame();
    }

    private void POP() {
        blvm.dropFrame();
    }

    private void RLV(String name) throws ExecutionException {
        blvm.registerLocalVariable(name);
    }

    private void RGV(String name) throws ExecutionException {
        blvm.registerGlobalVariable(name);
    }

    private Object VALUE(String name) throws ExecutionException {
        return ((AbstractPrimitiveRightValue<?>) blvm.getVariableValue(name))
                .getValue();
    }

    public void testMixedBasicVariableMap() throws ExecutionException {
        blvm = new MixedBasicVariableMap();

        try {
            RLV("algo");
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
        }
        RGV("alga");

        LET("var1", 5.5);
        VE("var1");
        VD("var1");
        VNE("var2");
        VND("var2");
        assertEquals(5.5, (Double) VALUE("var1"), 0.0);
        LET("var3", null);
        VE("var3");
        VND("var3");

        //
        // ENTERING LOCAL SCOPE
        //
        PUSH();
        blvm.isGlobal("alga");
        RGV("olga");
        RGV("olga");// must be lenient
        try {
            RLV("olga");
            assertTrue(false);
        } catch (BasicRuntimeException bre) {

        }
        RLV("var2");
        LET("var2", "avestruc");
        RGV("var4");
        LET("var4", "berenjena");
        VE("var2");
        VD("var2");
        // var1 is global variable
        VE("var1");
        VD("var1");
        // var4 was declared global
        VD("var4");
        POP();
        VE("var1");
        VD("var1");
        VNE("var2");
        VND("var2");
        assertEquals(5.5, (Double) VALUE("var1"), 0.0);
        VE("var3");
        VND("var3");
        // var4 was declared global
        VD("var4");

        try {
            POP();
            assertFalse(true);
        } catch (EmptyStackException e) {
        }
    }

    public void testCasing() throws ExecutionException {
        blvm = new MixedBasicVariableMap();
        blvm.setCaseSensitive();
        LET("var1", 1);
        LET("VAR1", 2);
        assertEquals((Long) 1L, (Long) VALUE("var1"));
        assertEquals((Long) 2L, (Long) VALUE("VAR1"));

        blvm = new MixedBasicVariableMap();
        blvm.setCaseIgnorant();
        LET("var1", 1);
        LET("VAR1", 2);
        assertEquals((Long) 2L, (Long) VALUE("VaR1"));

        blvm = new MixedBasicVariableMap();
        blvm.setCaseFreak();
        LET("var1", 1);
        try {
            LET("VAR1", 2);
            assertTrue(false);
        } catch (BasicRuntimeException bre) {
            log.debug("this exception has to be thrown " + bre);
        }
        assertEquals((Long) 1L, (Long) VALUE("var1"));

        blvm = new MixedBasicVariableMap();
        blvm.setCaseSensitive();
        blvm.defaultVariableScopeShallBeGlobal();
        PUSH();
        LET("var1", 1);
        LET("VAR1", 2);
        POP();
        assertEquals((Long) 1L, (Long) VALUE("var1"));
        assertEquals((Long) 2L, (Long) VALUE("VAR1"));

        blvm = new MixedBasicVariableMap();
        blvm.setCaseFreak();
        blvm.defaultVariableScopeShallBeLocal();
        PUSH();
        LET("var1", 1);
        try {
            LET("VAR1", 2);
            assertTrue(false);
        } catch (BasicRuntimeException bre) {
            log.debug("this exception has to be thrown " + bre);
        }
        assertEquals((Long) 1L, (Long) VALUE("var1"));
        POP();
        VNE("var1");

        blvm = new MixedBasicVariableMap();
        blvm.setCaseFreak();
        blvm.defaultVariableScopeShallBeGlobal();
        blvm.createVariablesOnTheFly();// just for the coverage
        PUSH();
        LET("var1", 1);
        try {
            LET("VAR1", 2);
            assertTrue(false);
        } catch (BasicRuntimeException bre) {
            log.debug("this exception has to be thrown " + bre);
        }
        assertEquals((Long) 1L, (Long) VALUE("var1"));
        POP();
        VE("var1");
        assertEquals((Long) 1L, (Long) VALUE("var1"));

        blvm = new MixedBasicVariableMap();
        blvm.setCaseFreak();
        blvm.requireVariableDeclaration();
        PUSH();
        try {
            LET("var1", 1);
            assertTrue(false);
        } catch (BasicRuntimeException bre) {
            log.debug("this exception has to be thrown " + bre);
        }
        RGV("var1");
        LET("var1", 1);
        try {
            LET("VAR1", 2);
            assertTrue(false);
        } catch (BasicRuntimeException bre) {
            log.debug("this exception has to be thrown " + bre);
        }
        try {
            RGV("VAR1");
            assertTrue(false);
        } catch (BasicRuntimeException bre) {
            log.debug("this exception has to be thrown " + bre);
        }
        assertEquals((Long) 1L, (Long) VALUE("var1"));
        POP();
        VE("var1");
        assertEquals((Long) 1L, (Long) VALUE("var1"));

    }

}
