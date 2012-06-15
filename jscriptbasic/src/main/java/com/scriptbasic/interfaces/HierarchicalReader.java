package com.scriptbasic.interfaces;

/**
 * A hierarchical reader can include sources into the string of characters read
 * returned by another reader. Implementation usually do not directly read any
 * source, but rather use a non hierarchical reader to read the source.
 * <p>
 * To give the hierarchical reader to a Lexical Analyzer is done usually
 * following the pattern:
 * <p>
 * 
 * <pre>
 * Reader reader = someSourceProvider.get(&quot;some source name&quot;);
 * 
 * HierarchicalReader hierarchicalReader = new SomeHierarchicalReader();
 * hierarchicalReader.include(reader);
 * 
 * LexicalAnalyzer lexicalAnalyzer = new ScriptBasicLexicalAnalyzer();
 * la.set(hierarchicalReader);
 * </pre>
 * 
 * @author Peter Verhas
 * 
 */
public interface HierarchicalReader extends Reader {
    void include(Reader reader);
}
