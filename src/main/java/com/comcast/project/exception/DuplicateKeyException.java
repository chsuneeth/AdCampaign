package com.comcast.project.exception;


public class DuplicateKeyException extends Exception {
	
	/**
	 * @author suneeth
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateKeyException(String message) {
		super(message);
	}

}
