package com.nopcommerce.exceptions;

/**
 * Custom exception class for handling driver-related errors. Exception is
 * Exception thrown when there are issues with WebDriver initialization or
 * operations.
 */
public class DriverException extends RuntimeException {

	public DriverException(String message) {
		super(message);
	}

	public DriverException(String message, Throwable cause) {
		super(message, cause);
	}
}
