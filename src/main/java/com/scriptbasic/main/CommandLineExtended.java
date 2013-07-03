/**
 * 
 */
package com.scriptbasic.main;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.readers.GenericReader;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.functions.file.FileHandlingFunctions;

/**
 * @author Peter Verhas date Aug 6, 2012
 * 
 */
public class CommandLineExtended {
	public static void main(final String[] args) throws Exception {
		if (args.length != 1) {
			System.err
					.println("usage: java [-Dsb4j.extensionclasses=comma separated list of extension classes] "
							+ "[-cp classpath for extensions] -jar jscriptbasic-x.y.z basicprogram.sb");
			System.exit(1);
		}
		Logger.getLogger("").setLevel(Level.SEVERE);
		final String basicProgramFileName = args[0];

		// START SNIPPET: x
		final Factory factory = new BasicFactory();
		final java.io.Reader r = new FileReader(basicProgramFileName);
		final GenericReader reader = new GenericReader();
		reader.set(r);
		reader.setSourceProvider(null);
		reader.set((String) basicProgramFileName);
		final LexicalAnalyzer lexicalAnalyzer = FactoryUtility
				.getLexicalAnalyzer(factory);
		lexicalAnalyzer.set(reader);
		final ExtendedInterpreter interpreter = FactoryUtility
				.getExtendedInterpreter(factory);
		interpreter.registerFunctions(FileHandlingFunctions.class);
		final String classes = System.getProperty("sb4j.extensionclasses");
		if (classes != null && classes.length() > 0) {
			String[] classNames = classes.split(",");
			for (String className : classNames) {
				Class<?> klass = Class.forName(className); 
				interpreter.registerFunctions(klass);
			}
		}
		final PrintWriter outWriter = new PrintWriter(System.out);
		final PrintWriter errorWriter = new PrintWriter(System.err);
		try {
			interpreter.setProgram(FactoryUtility.getSyntaxAnalyzer(factory)
					.analyze());
			interpreter.setWriter(outWriter);
			interpreter.setErrorWriter(errorWriter);
			interpreter.setReader(new InputStreamReader(System.in));
			interpreter.execute();
		} catch (final Exception sce) {
			Exception cause = (Exception) sce.getCause();
			if (cause == null) {
				cause = sce;
			}
			if (cause.getMessage() != null) {
				System.err.println("ERROR: " + cause.getMessage());
			} else {
				throw sce;
			}
		} finally {
			outWriter.flush();
			errorWriter.flush();
		}
		// END SNIPPET: x
	}
}
