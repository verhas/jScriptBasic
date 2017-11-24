package com.scriptbasic.memory;

import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.*;

/**
 * @author Peter Verhas
 * date June 23, 2012
 */
public class TestVariableMaps {

    private static Logger log = LoggerFactory.getLogger();
    MixedBasicVariableMap mixedMap = null;

    @Test
    public void testBasicLocalVariableMap() throws ExecutionException {
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

    private void VE(String name) throws ExecutionException {
        assertTrue(mixedMap.variableExists(name));
    }

    private void VNE(String name) throws ExecutionException {
        assertFalse(mixedMap.variableExists(name));
    }

    private void VD(String name) throws ExecutionException {
        assertTrue(mixedMap.variableDefined(name));
    }

    private void VND(String name) throws ExecutionException {
        assertFalse(mixedMap.variableDefined(name));
    }

    private void LET(String name, Object v) throws ExecutionException {
        if (v == null) {
            mixedMap.setVariable(name, null);
        } else if (v instanceof RightValue) {
            mixedMap.setVariable(name, (RightValue) v);
        } else if (v instanceof Double) {
            mixedMap.setVariable(name, new BasicDoubleValue((Double) v));
        } else if (v instanceof Long) {
            mixedMap.setVariable(name, new BasicLongValue((Long) v));
        } else if (v instanceof Integer) {
            mixedMap.setVariable(name,
                    new BasicLongValue(((Integer) v).longValue()));
        } else if (v instanceof String) {
            mixedMap.setVariable(name, new BasicStringValue((String) v));
        }
    }

    private void PUSH() {
        mixedMap.newFrame();
    }

    private void POP() {
        mixedMap.dropFrame();
    }

    private void RLV(String name) throws ExecutionException {
        mixedMap.registerLocalVariable(name);
    }

    private void RGV(String name) throws ExecutionException {
        mixedMap.registerGlobalVariable(name);
    }

    private Object VALUE(String name) throws ExecutionException {
        return ((AbstractPrimitiveRightValue<?>) mixedMap.getVariableValue(name))
                .getValue();
    }

    public void testMixedBasicVariableMap() throws ExecutionException {
        mixedMap = new MixedBasicVariableMap();

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
        mixedMap.isGlobal("alga");
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
        mixedMap = new MixedBasicVariableMap();
        mixedMap.setCaseSensitive();
        LET("var1", 1);
        LET("VAR1", 2);
        assertEquals((Long) 1L, VALUE("var1"));
        assertEquals((Long) 2L, VALUE("VAR1"));

        mixedMap = new MixedBasicVariableMap();
        mixedMap.setCaseIgnorant();
        LET("var1", 1);
        LET("VAR1", 2);
        assertEquals((Long) 2L, VALUE("VaR1"));

        mixedMap = new MixedBasicVariableMap();
        mixedMap.setCaseFreak();
        LET("var1", 1);
        try {
            LET("VAR1", 2);
            assertTrue(false);
        } catch (BasicRuntimeException bre) {
            log.debug("this exception has to be thrown " + bre);
        }
        assertEquals((Long) 1L, VALUE("var1"));

        mixedMap = new MixedBasicVariableMap();
        mixedMap.setCaseSensitive();
        mixedMap.defaultVariableScopeShallBeGlobal();
        PUSH();
        LET("var1", 1);
        LET("VAR1", 2);
        POP();
        assertEquals((Long) 1L, VALUE("var1"));
        assertEquals((Long) 2L, VALUE("VAR1"));

        mixedMap = new MixedBasicVariableMap();
        mixedMap.setCaseFreak();
        mixedMap.defaultVariableScopeShallBeLocal();
        PUSH();
        LET("var1", 1);
        try {
            LET("VAR1", 2);
            assertTrue(false);
        } catch (BasicRuntimeException bre) {
            log.debug("this exception has to be thrown " + bre);
        }
        assertEquals((Long) 1L, VALUE("var1"));
        POP();
        VNE("var1");

        mixedMap = new MixedBasicVariableMap();
        mixedMap.setCaseFreak();
        mixedMap.defaultVariableScopeShallBeGlobal();
        mixedMap.createVariablesOnTheFly();// just for the coverage
        PUSH();
        LET("var1", 1);
        try {
            LET("VAR1", 2);
            assertTrue(false);
        } catch (BasicRuntimeException bre) {
            log.debug("this exception has to be thrown " + bre);
        }
        assertEquals((Long) 1L, VALUE("var1"));
        POP();
        VE("var1");
        assertEquals((Long) 1L, VALUE("var1"));

        mixedMap = new MixedBasicVariableMap();
        mixedMap.setCaseFreak();
        mixedMap.requireVariableDeclaration();
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
        assertEquals((Long) 1L, VALUE("var1"));
        POP();
        VE("var1");
        assertEquals((Long) 1L, VALUE("var1"));

    }

}
