package com.nishit.novopay.exception;

public class TransactionIdNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionIdNotFoundException() {
		super();
	}
	
	public TransactionIdNotFoundException(String message) {
		super(message);
	}
	
	public TransactionIdNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
