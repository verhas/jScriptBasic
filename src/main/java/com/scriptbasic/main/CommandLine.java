package com.scriptbasic.main;

import com.scriptbasic.context.Context;
import com.scriptbasic.context.ContextBuilder;
import com.scriptbasic.utility.functions.file.FileHandlingFunctions;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * @author Peter Verhas date Aug 6, 2012
 */
public class CommandLine {
    public static void main(final String[] args) throws Exception {
        if (args.length != 1) {
            System.err
                    .println("usage: java [-Dsb4j.extensionclasses=comma separated list of extension classes] "
                            + "[-cp classpath for extensions] -jar jscriptbasic-x.y.z basicprogram.sb");
            System.exit(1);
        }
        final String basicProgramFileName = args[0];

        // START SNIPPET: x
        final PrintWriter output = new PrintWriter(System.out);
        final PrintWriter error = new PrintWriter(System.err);
        try {
            final InputStreamReader input = new InputStreamReader(System.in);
            final Context ctx = ContextBuilder.from(new FileReader(basicProgramFileName), input, output, error);
            ctx.interpreter.registerFunctions(FileHandlingFunctions.class);
            registerSystemPropertyDefinedClasses(ctx);
            ctx.interpreter.setProgram(ctx.syntaxAnalyzer.analyze());
            ctx.configuration.set("insecure","true");
            ctx.interpreter.execute();
        } catch (final Exception exception) {
            final Throwable cause = Optional.ofNullable(exception.getCause()).orElse(exception);
            if (cause.getMessage() != null) {
                System.err.println("ERROR: " + cause.getMessage());
            } else {
                throw exception;
            }
        } finally {
            output.flush();
            error.flush();
        }
        // END SNIPPET: x
    }

    private static void registerSystemPropertyDefinedClasses(final Context ctx) throws ClassNotFoundException {
        final String classes = System.getProperty("sb4j.extensionclasses");
        if (classes != null && classes.length() > 0) {
            final String[] classNames = classes.split(",");
            for (final String className : classNames) {
                final Class<?> klass = Class.forName(className);
                ctx.interpreter.registerFunctions(klass);
            }
        }
    }
}
