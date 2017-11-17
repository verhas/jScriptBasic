package com.scriptbasic.sourceproviders;

import java.io.IOException;



import com.scriptbasic.interfaces.Reader;

public class TestSingleIncludeSourcePathSourceProvider  {

    private class TestedSingleIncludeSourcePathSourceProvider extends
            AbstractSingleIncludeSourcePathSourceProvider {

        @Override
        protected Reader getSource(final String sourceName) throws IOException {
            return null;
        }

        @Override
        protected Reader getSource(final String sourceName,
                final String referencingSource) throws IOException {
            return null;
        }

        @Override
        protected String getKeyName(final String sourceName) {
            return null;
        }

        @Override
        protected String getKeyName(final String sourceName,
                final String referencingSource) {
            return null;
        }

    }

    public void testFSPFileNotFound() throws IOException {
        final TestedSingleIncludeSourcePathSourceProvider tsispsp = new TestedSingleIncludeSourcePathSourceProvider();
        tsispsp.setSingleInclude(null);
        tsispsp.getSingleInclude();
        tsispsp.get("habakukk");
        tsispsp.get("habakukk", "kakukk");
        tsispsp.setSingleInclude(new BasicSingleIncludeChecker());
        tsispsp.get("habakukk", "kakukk");
    }
}
