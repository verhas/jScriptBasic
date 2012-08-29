/**
 * 
 */
package com.scriptbasic.log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Verhas date Aug 7, 2012
 * 
 */
public class LoggerFactory {
	private final static Map<Class<?>, Logger> loggers = new HashMap<Class<?>, Logger>();

	public static synchronized Logger getLogger(Class<?> klass) {
		Logger logger = null;
		if (loggers.containsKey(klass)) {
			logger = loggers.get(klass);
		} else {
			logger = new Logger(java.util.logging.Logger.getLogger(klass
					.getName()));
			loggers.put(klass, logger);
		}
		return logger;
	}
}
