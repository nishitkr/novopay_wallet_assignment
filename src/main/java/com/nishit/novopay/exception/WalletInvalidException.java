package com.nishit.novopay.exception;

public class WalletInvalidException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WalletInvalidException() {
		super();
	}
	
	public WalletInvalidException(String message) {
		super(message);
	}
	
	public WalletInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

}