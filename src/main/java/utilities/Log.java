package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
	private Log() {
	}

	private static Logger log = LogManager.getLogger(Log.class);

	public static void getLogger(String loggerName) {
		log = LogManager.getLogger(loggerName);
	}

	/**
	 * This method is used to write trace messages to the log
	 * 
	 * @param message - trace message to printed
	 */
	public static void trace(String message) {
		log.trace(message);
	}

	/**
	 * This method is used to write debug messages to the log
	 * 
	 * @param message - debug message to printed
	 */
	public static void debug(String message) {
		log.debug(message);
	}

	/**
	 * This method is used to write info messages to the log
	 * 
	 * @param message - info message to printed
	 */
	public static void info(String message) {
		log.info(message);
	}

	/**
	 * This method is used to write warning messages to the log
	 * 
	 * @param message - warning message to printed
	 */
	public static void warn(String message) {
		log.warn(message);
	}

	/**
	 * This method is used to write error messages to the log
	 * 
	 * @param message - error message to printed
	 */
	public static void error(String message) {
		log.error(message);
	}

	/**
	 * This method is used to write fatal messages to the log
	 * 
	 * @param message - fatal message to printed
	 */
	public static void fatal(String message) {
		log.fatal(message);
	}
}
