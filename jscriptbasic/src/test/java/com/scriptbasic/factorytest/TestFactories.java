package com.scriptbasic.factorytest;

import junit.framework.TestCase;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.factories.GenericFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.FactoryManaged;
import com.scriptbasic.readers.GenericHierarchicalReader;
import com.scriptbasic.syntax.BasicSyntaxAnalyzer;
import com.scriptbasic.utility.FactoryUtility;

public class TestFactories extends TestCase {

    private static class classWithPublicConstructor implements FactoryManaged {

        @Override
        public void setFactory(Factory factory) {
        }

        @SuppressWarnings("unused")
        public classWithPublicConstructor() {
        }
    }

    private static class classWithManyConstructor implements FactoryManaged {

        @Override
        public void setFactory(Factory factory) {

        }

        private classWithManyConstructor() {
        }

        private classWithManyConstructor(int a) {
        }
    }

    private static class NullFactory implements Factory {

        @Override
        public <T extends FactoryManaged> void create(Class<T> interf4ce,
                Class<? extends T> cl4ss) {
        }

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

    @SuppressWarnings("static-method")
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

        try {
            BasicFactory bf = new BasicFactory();
            bf.get(NothingImplementsThis.class);
            assertTrue("Could instantiate " + NothingImplementsThis.class,
                    false);
        } catch (BasicInterpreterInternalError e) {
            // OK
        }
        try {
            BasicFactory bf = new BasicFactory();
            bf.create(ThrowErrorConstructorInterface.class,
                    ThrowErrorConstructorClass.class);
            bf.get(ThrowErrorConstructorInterface.class);
            assertTrue("Could instantiate " + ThrowErrorConstructorClass.class,
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
