package com.scriptbasic.sourceproviders;

import java.io.IOException;

import com.scriptbasic.interfaces.Reader;

import junit.framework.TestCase;

public class TestSingleIncludeSourcePathSourceProvider extends TestCase {
    public TestSingleIncludeSourcePathSourceProvider(String testName) {
        super(testName);
    }

    private class TestedSingleIncludeSourcePathSourceProvider extends
            SingleIncludeSourcePathSourceProvider {

        @Override
        protected Reader getSource(String sourceName) throws IOException {
            return null;
        }

        @Override
        protected Reader getSource(String sourceName, String referencingSource)
                throws IOException {
            return null;
        }

        @Override
        protected String getKeyName(String sourceName) {
            return null;
        }

        @Override
        protected String getKeyName(String sourceName, String referencingSource) {
            return null;
        }

    }

    public void testFSPFileNotFound() throws IOException {
        TestedSingleIncludeSourcePathSourceProvider tsispsp = new TestedSingleIncludeSourcePathSourceProvider();
        tsispsp.setSingleInclude(null);
        tsispsp.getSingleInclude();
        tsispsp.get("habakukk");
        tsispsp.get("habakukk","kakukk");
        tsispsp.setSingleInclude(new BasicSingleIncludeChecker());
        tsispsp.get("habakukk","kakukk");
    }
}
