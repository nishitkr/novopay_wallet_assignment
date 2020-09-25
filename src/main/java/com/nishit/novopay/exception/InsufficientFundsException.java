package com.nishit.novopay.exception;

public class InsufficientFundsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientFundsException() {
		super();
	}
	
	public InsufficientFundsException(String message) {
		super(message);
	}
	
	public InsufficientFundsException(String message, Throwable cause) {
		super(message, cause);
	}
}
