package com.scriptbasic.log;

import java.lang.System.Logger.Level;

/**
 * @author Peter Verhas
 * date Aug 7, 2012
 */
public class Logger {
    private final java.lang.System.Logger javaLogger;

    protected Logger(final java.lang.System.Logger javaLogger) {
        this.javaLogger = javaLogger;
    }

    protected static String format(final String s, final Object[] args) {
        String msg = s;
        if (args != null) {
            int i = 0;
            int pos;
            while ((pos = msg.indexOf("{}")) != -1 && i < args.length) {
                msg = msg.substring(0, pos) + args[i] + msg.substring(pos + 2);
                i++;
            }
        }
        return msg;
    }

    public void error(final String s, final Throwable e) {
        javaLogger.log(Level.ERROR, s, e);
    }

    public void error(final String s) {
        javaLogger.log(Level.ERROR, s);
    }

    public void debug(final String s, final Object... args) {
        javaLogger.log(Level.DEBUG, format(s, args));
    }

    public void info(final String s, final Object... args) {
        javaLogger.log(Level.INFO, format(s, args));
    }

    public void error(final String s, final Object... args) {
        javaLogger.log(Level.ERROR, format(s, args));
    }
}
