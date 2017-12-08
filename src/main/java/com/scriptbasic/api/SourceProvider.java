package com.scriptbasic.api;

import java.io.IOException;

/**
 * A source provider gives a reader for a source based upon the name of the source.
 * <p>
 * When source files reference each other the source provider may alter its
 * behavior finding the source based on which source is referencing the other
 * source.
 * <p>
 * For example you want to get a reader to the source named {@code myProg.sb}
 * This program may include using the {@code include} statement {@code inc/incProg.sb}.
 * The second file is in the directory {@code inc}. If that file includes
 * {@code myProg.sb} then source provider may provide a reader to the file
 * {@code inc/myProg.sb}.
 * <p>
 * Source providers may provide readers reading the source from files,
 * database, svn and other locations.
 *
 * @author Peter Verhas
 */
public interface SourceProvider {
    /**
     * Get a reader to a source when there is no referencing source. This is the
     * main source.
     *
     * @param sourceName the name of the source
     * @return reader reading the source file.
     */
    SourceReader get(String sourceName) throws IOException;

    /**
     * Get a reader to a source specifying the source that references this
     * source.
     *
     * @param sourceName        the name of the source to get the reader to.
     * @param referencingSource the name of the source that is referencing the source to read.
     * @return
     */
    SourceReader get(String sourceName, String referencingSource) throws IOException;
}
