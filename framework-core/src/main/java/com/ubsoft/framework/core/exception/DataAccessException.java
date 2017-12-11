package com.ubsoft.framework.core.exception;

import java.sql.SQLException;



public class DataAccessException extends ComException {

	private static final long serialVersionUID = 1L;

	public static final int DAL_ERROR_NOT_SUPPORT_DATABASE = 0;

	public static final int DAL_ERROR_POINTER_NO_RECORD_TO_POINT = 1;

	public static final int DAL_ERROR_FIELD_INDEX_OUT_OF_BOUND = 2;

	public static final int DAL_ERROR_UNDEFINED_FIELD = 3;

	public static final int DAL_ERROR_TYPE_ERROR = 4;

	public static final int DAL_ERROR_METHOD_NOT_SUPPORT_IN_CLASS = 5;

	public static final int DAL_ERROR_IN_SETTING_FIELD_TO_NULL = 6;

	public static final int DAL_ERROR_CAN_NOT_DELETE_A_NOT_NULL_FIELD = 7;

	public static final int DAL_ERROR_GET_Entity_ERROR = 8;

	public static final int DAL_ERROR_NO_DIMENSION_VALUE_FOUND = 9;

	public static final int DAL_ERROR_Entity_HAS_BEEN_UPDATED = 10;

	public static final int DAL_ERROR_FIELD_NOT_ALLOWED_HERE = 11;

	public static final int DAL_ERROR_JDBC_CONNECTION_UNAVAILABLE = 12;

	public static final int DAL_ERROR_FIELD_CAN_NOT_BE_UPDATED = 13;

	public static final int DAL_ERROR_Entity_DIMENSION_NAME_NOT_EQUAL_BALANCETABLE_DIMENSION_NAME = 14;

	public static final int DAL_ERROR_NEED_Entity_METADATA_MANAGER_TO_OPEN_SESSION = 15;

	public static final int DAL_ERROR_GENERATE_SEQUENCE_ERROR = 16;

	public static final int DAL_ERROR_OUT_OF_BOUND = 17;

	public static final int DAL_ERROR_SQL_EXCEPTION = 18;

	public static final int DAL_ERROR_IN_DELETING = 19;

	public static final int DAL_ERROR_IN_FORM_OPERATION = 20;

	public static final int DAL_ERROR_IO_EXCEPTION = 21;

	public static final int DAL_ERROR_GET_ROLE_ERROR = 22;

	public static final int DAL_ERROR_NOT_ENOUGH_RIGHT = 23;

	public static final int DAL_ERROR_GET_DIMENSION_LEVEL = 24;

	public static final int DAL_ERROR_GET_DIMENSION = 25;

	public static final int DAL_ERROR_NO_PARENT_DIMENSION = 26;

	public static final int DAL_ERROR_NO_VALUE_FOUND = 27;

	public static final int DAL_ERROR_DIFFER_WITH_ROWSETS = 28;

	public static final int DAL_ERROR_UNDEFINED_METHOD = 29;

	public static final int DAL_ERROR_SQL_EXCEPTION_PK_VIOLATED = 30;

	public static final int DAL_ERROR_SQL_EXCEPTION_UNIQUE_CONSTRAINT_VIOLATED = 31;

	public static final int DAL_ERROR_SQL_EXCEPTION_INTEGRITY_CONSTRAINT_VIOLATED_PARENT_KEY_NOT_FOUND = 32;

	public static final int DAL_ERROR_SQL_EXCEPTION_INTEGRITY_CONSTRAINT_VIOLATED_CHILD_RECORD_FOUND = 33;

	public static final int DAL_ERROR_SQL_EXCEPTION_INTEGRITY_CONSTRAINT_VIOLATED = 34;
	
	public static final int DAL_ERROR_SQL_EXCEPTION_ALIAS_NOT_DEFINED = 35;

	public static final int DAL_ERROR_XML_DATASET = 50;

	public static final int DAL_ERROR_USERSESSION_ISNULL = 60;

	public static final int DAL_ERROR_OPERATION_CANCELLED = 70;

	public static final int DAL_ERROR_RECORD_LIMIT = 200;

	public static final int DAL_ERROR_DYNAMIC_SQL = 210;

	public static final int DAL_ERROR_BASE_FORM_LOCKED = 400;

	public static final int DAL_ERROR_SQL_SYNTAX = 500;

	/****************************************************************
	 *
	 * Constructors
	 * 
	 ****************************************************************/

	/**
	 * Constructor with error code and originalMessage
	 * @param code
	 * @param originalMessage
	 */
	public DataAccessException(int code, String originalMessage) {

		super(ComException.MIN_ERROR_CODE_DAL + code, originalMessage);
		if(code == 500){
			addParameter("reason", originalMessage);
		}
	}

	/**
	 * Constructor with error code and original exception
	 * @param code
	 * @param cause
	 */
	public DataAccessException(int code, Throwable cause) {

		super(ComException.MIN_ERROR_CODE_DAL + getSQLExceptionCode(code, cause), cause);
		if (cause instanceof SQLException) {
			addParameter("dialect", "");
			addParameter("errorCode", "" + extractErrorCode((SQLException) cause));
			addParameter("sqlState", extractSqlState((SQLException) cause));
		}
	}

	/**
	 * Constructor with message and original exception
	 * @param message
	 * @param cause
	 */
	public DataAccessException(String message, Throwable cause) {

		super(message, cause);
	}

	/**
	 * Constructor with original exception
	 * @param cause
	 */
	public DataAccessException(Throwable cause) {

		super(cause);
	}

	private static int getSQLExceptionCode(int code, Throwable cause) {

		if (cause instanceof SQLException) {
			int errorCode = extractErrorCode((SQLException) cause);
			String ComCode = "";
			if (null != ComCode && ComCode.length() > 0) {
				return Integer.parseInt(ComCode);
			}
			else
				return DAL_ERROR_SQL_EXCEPTION;
		}
		return code;
	}

	

	/**
	 * For the given SQLException, locates the vendor-specific error code.
	 *
	 * @param sqlException The exception from which to extract the SQLState
	 * @return The error code.
	 */
	public static int extractErrorCode(SQLException sqlException) {

		int errorCode = sqlException.getErrorCode();
		SQLException nested = sqlException.getNextException();
		while (errorCode == 0 && nested != null) {
			errorCode = nested.getErrorCode();
			nested = nested.getNextException();
		}
		return errorCode;
	}

	/**
	 * For the given SQLException, locates the X/Open-compliant SQLState.
	 *
	 * @param sqlException The exception from which to extract the SQLState
	 * @return The SQLState code, or null.
	 */
	public static String extractSqlState(SQLException sqlException) {

		String sqlState = sqlException.getSQLState();
		SQLException nested = sqlException.getNextException();
		while (sqlState == null && nested != null) {
			sqlState = nested.getSQLState();
			nested = nested.getNextException();
		}
		return sqlState;
	}

	public DataAccessException parameter(String name, Object value) {

		return (DataAccessException) super.addParameter(name, value);
	}
	
}
