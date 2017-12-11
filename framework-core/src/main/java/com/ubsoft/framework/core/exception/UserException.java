package com.ubsoft.framework.core.exception;



public class UserException extends ComException {

	public UserException(int code, String originalMessage) {
		super(ComException.MIN_ERROR_CODE_USER + code, originalMessage);		
	}

	/**
	 * Constructor with error code and original exception
	 * @param code
	 * @param cause
	 */
	public UserException(int code, Throwable cause) {

		super(ComException.MIN_ERROR_CODE_USER , cause);
		
	}

	/**
	 * Constructor with message and original exception
	 * @param message
	 * @param cause
	 */
	public UserException(String message, Throwable cause) {

		super(message, cause);
	}
}
