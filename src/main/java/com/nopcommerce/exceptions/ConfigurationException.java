package com.nopcommerce.exceptions;

/**
 * Custom exception class for configuration-related errors. Exception throws
 * when configuration loading or parsing fails.
 * 
 * @subramanyam
 */
@SuppressWarnings("serial")
public class ConfigurationException extends RuntimeException {

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
