/**
 * 
 */
package com.scriptbasic.main;

import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author Peter Verhas date Aug 6, 2012
 * 
 */
public class CommandLine {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err
					.println("usage: java -jar jscriptbasic-x.y.z basicprogram.sb");
			System.exit(1);
		}
		final String basicProgramFileName = args[0];
		final int extensionDotPosition = basicProgramFileName
				.indexOf((int) '.');
		final String extension;
		if (extensionDotPosition > -1) {
			extension = basicProgramFileName
					.substring(extensionDotPosition + 1);
		} else {
			extension = "";
		}

		// START SNIPPET: x
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByExtension(extension);
		if (se != null) {
			ScriptContext context = se.getContext();
			PrintWriter outWriter = new PrintWriter(System.out);
			context.setWriter(outWriter);
			PrintWriter errorWriter = new PrintWriter(System.err);
			context.setErrorWriter(errorWriter);
			context.setReader(new InputStreamReader(System.in));
			Reader reader = new FileReader(basicProgramFileName);
			try {
				se.eval(reader, context);
			} catch (ScriptException sce) {
				Exception cause = (Exception) sce.getCause();
				if (cause == null) {
					cause = sce;
				}
				if (cause.getMessage() != null) {
					System.err.println("ERROR: " + cause.getMessage());
				} else {
					throw sce;
				}
			}
			outWriter.flush();
			errorWriter.flush();
		} else {
			System.err
					.println("ERROR: There is no suitable interpreter for that file.");
		}
		// END SNIPPET: x
	}
}
