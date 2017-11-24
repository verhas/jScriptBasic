package com.scriptbasic.log;

import java.lang.System.Logger.Level;

/**
 * @author Peter Verhas
 * date Aug 7, 2012
 * 
 */
public class Logger {
    private java.lang.System.Logger javaLogger;

    protected Logger(java.lang.System.Logger javaLogger) {
        this.javaLogger = javaLogger;
    }

    public void error(String s, Throwable e) {
        javaLogger.log(Level.ERROR, s, e);
    }

    public void error(String s) {
        javaLogger.log(Level.ERROR, s);
    }

    public void debug(String s, Object... args) {
        javaLogger.log(Level.DEBUG, format(s, args));
    }

    public void info(String s, Object... args) {
        javaLogger.log(Level.INFO, format(s, args));
    }

    public void error(String s, Object... args) {
        javaLogger.log(Level.ERROR, format(s, args));
    }

    protected static String format(String s, Object[] args) {
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
}
