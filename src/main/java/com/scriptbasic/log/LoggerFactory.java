/**
 *
 */
package com.scriptbasic.log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Verhas date Aug 7, 2012
 */
public class LoggerFactory {
    private final static Map<Class<?>, Logger> loggers = new HashMap<>();

    public static synchronized Logger getLogger() {
        final Class<?> klass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        final Logger logger;
        if (loggers.containsKey(klass)) {
            logger = loggers.get(klass);
        } else {
            logger = new Logger(
                    java.lang.System.LoggerFinder.getLoggerFinder()
                            .getLogger(klass.getName(), klass.getModule()));
            loggers.put(klass, logger);
        }
        return logger;
    }
}
