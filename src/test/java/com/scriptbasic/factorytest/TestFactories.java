package com.scriptbasic.factorytest;

import junit.framework.TestCase;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.factories.ThreadLocalFactoryFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.FactoryManaged;
import com.scriptbasic.readers.GenericHierarchicalReader;
import com.scriptbasic.utility.FactoryUtility;

public class TestFactories extends TestCase {

    private static class NullFactory implements Factory {

        @Override
        public <T extends FactoryManaged> T get(Class<T> klass) {
            return null;
        }

        @Override
        public void clean() {
        }

    }

    private static class BadFactory extends NullFactory {
        @SuppressWarnings("unchecked")
        @Override
        public <T extends FactoryManaged> T get(Class<T> klass) {
            return (T) new GenericHierarchicalReader();
        }
    }

    public void testThreadLocalFactory() throws Exception {
        ThreadLocalFactoryFactory.getFactory();
    }

    @SuppressWarnings("static-method")
    public void testFactories() throws Exception {

        try {
            BasicFactory bf = new BasicFactory();
            bf.get(NothingImplementsThis.class);
            assertTrue("Could instantiate " + NothingImplementsThis.class,
                    false);
        } catch (BasicInterpreterInternalError e) {
            // OK
        }

        try {
            FactoryUtility.getExpressionAnalyzer(new NullFactory());
            assertTrue("NullFactory could load ExpressionAnalyzer instance",
                    false);
        } catch (BasicInterpreterInternalError e) {
        }
        try {
            FactoryUtility.getSyntaxAnalyzer(new BadFactory());
            assertTrue("BadFactory could load getSyntaxAnalyzer instance",
                    false);
        } catch (BasicInterpreterInternalError e) {
        }
    }
}
