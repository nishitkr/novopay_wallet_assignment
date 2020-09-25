package com.nishit.novopay.exception;

public class UserDetailNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserDetailNotFoundException() {
		super();
	}
	
	public UserDetailNotFoundException(String message) {
		super(message);
	}
	
	public UserDetailNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
