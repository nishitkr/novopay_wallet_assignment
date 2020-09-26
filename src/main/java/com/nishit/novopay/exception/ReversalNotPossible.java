package com.nishit.novopay.exception;

public class ReversalNotPossible extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReversalNotPossible() {
		super();
	}
	
	public ReversalNotPossible(String message) {
		super(message);
	}
	
	public ReversalNotPossible(String message, Throwable cause) {
		super(message, cause);
	}
}
