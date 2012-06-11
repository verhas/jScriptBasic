package com.scriptbasic.factorytest;

import junit.framework.TestCase;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.factories.GenericFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.FactoryManaged;
import com.scriptbasic.syntax.BasicSyntaxAnalyzer;

public class TestFactories extends TestCase {

    private static class classWithPublicConstructor implements FactoryManaged {

        public void setFactory(Factory factory) {
        }

        @SuppressWarnings("unused")
        public classWithPublicConstructor() {
        }
    }

    private static class classWithManyConstructor implements FactoryManaged {

        public void setFactory(Factory factory) {

        }

        private classWithManyConstructor() {
        }

        private classWithManyConstructor(int a) {
        }
    }

    public void testFactories() throws Exception {
        Factory factory = new GenericFactory();
        factory.create(FactoryManaged.class, BasicSyntaxAnalyzer.class);
        Object object = factory.get(FactoryManaged.class);
        assertTrue(object instanceof BasicSyntaxAnalyzer);
        try {
            factory.create(BasicSyntaxAnalyzer.class, BasicSyntaxAnalyzer.class);
            assertTrue(false);
        } catch (BasicInterpreterInternalError e) {
            assertTrue(e.toString().indexOf("is not an interface") >= 0);

        }
        try {
            factory.create(FactoryManaged.class,
                    classWithPublicConstructor.class);
            assertTrue(false);
        } catch (BasicInterpreterInternalError e) {
            assertTrue(e.toString().indexOf("has public constructor") >= 0);
        }
        try {
            factory.create(FactoryManaged.class, classWithManyConstructor.class);
            assertTrue(false);
        } catch (BasicInterpreterInternalError e) {
            assertTrue(e.toString().indexOf("has too many constructors") >= 0);
        }
    }

}
