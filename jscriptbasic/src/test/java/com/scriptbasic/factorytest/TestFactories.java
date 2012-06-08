package com.scriptbasic.factorytest;

import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.factories.ThreadLocalFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.FactoryManaged;
import com.scriptbasic.syntax.BasicSyntaxAnalyzer;

public class TestFactories extends TestCase {

    private static class classWithPublicConstructor implements FactoryManaged {
        @SuppressWarnings("unused")
        public classWithPublicConstructor() {
        }
    }

    private static class classWithManyConstructor implements FactoryManaged {
        private classWithManyConstructor() {
        }

        private classWithManyConstructor(int a) {
        }
    }

    public void testFactories() throws Exception {
        Factory factory = new ThreadLocalFactory();
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

    private static class TestThread implements Runnable {
        private static AtomicInteger count = new AtomicInteger(0);

        private static boolean success = true;

        public static boolean isSuccess() {
            return success;
        }

        private static Factory factory = new ThreadLocalFactory();

        private void syncPoint(int x) {
            Integer j = count.addAndGet(1);
            while (j < x) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new Error("Test thread was interrupted");
                }
                j = count.get();
            }
        }

        private Integer threadIndex;

        public TestThread(Integer threadIndex) {
            this.threadIndex = threadIndex;
        }

        @Override
        public void run() {
            syncPoint(2);
            if (threadIndex == 1) {
                factory.create(FactoryManaged.class, BasicSyntaxAnalyzer.class);
                syncPoint(3);
                syncPoint(5);
                FactoryManaged object = factory.get(FactoryManaged.class);
                if (object == null) {
                    success = false;
                }
            } else {
                syncPoint(4);
                FactoryManaged object = factory.get(FactoryManaged.class);
                if (object != null) {
                    success = false;
                }
            }
        }

    }

    public void testThreads() throws Exception {
        Thread t1 = new Thread(new TestThread(1));
        Thread t2 = new Thread(new TestThread(2));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        assertTrue(TestThread.isSuccess());
    }

}
