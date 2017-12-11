package com.ubsoft.framework.core.exception;


public class TypeCastException  extends ComException{
	public TypeCastException(int code, String originalMessage) {
		super(ComException.MIN_ERROR_CODE_Entity + code, originalMessage);		
	}

	/**
	 * Constructor with error code and original exception
	 * @param code
	 * @param cause
	 */
	public TypeCastException(int code, Throwable cause) {

		super(ComException.MIN_ERROR_CODE_Entity , cause);
		
	}

	/**
	 * Constructor with message and original exception
	 * @param message
	 * @param cause
	 */
	public TypeCastException(String message, Throwable cause) {

		super(message, cause);
	}
}
