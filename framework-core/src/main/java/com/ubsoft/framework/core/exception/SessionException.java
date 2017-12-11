package com.ubsoft.framework.core.exception;

public class SessionException extends UserException {
	private static final long serialVersionUID = 1L;
    
    /**General session exception*/
    //public static final int ERR_CODE_SESSION_GENERAL = 10;
    
	/**General session-security exception*/
    public static final int ERR_CODE_SESSION_SECURITY = 20;
    
    /**General login exception*/
    public static final int ERR_CODE_SESSION_LOGIN = 100;
    /**login exception : user not exist*/
    public static final int ERR_CODE_SESSION_LOGIN_USER_NOT_EXIST = 110;
    /**login exception : user is disabled*/
    public static final int ERR_CODE_SESSION_LOGIN_USER_DISABLED = 115;
    /**login exception : username and password mismatch*/
    public static final int ERR_CODE_SESSION_LOGIN_PWD_MISMATCH = 120;
    /**login exception : System setting: multi-roles login is denied*/
    public static final int ERR_CODE_SESSION_LOGIN_DENY_MULTI_ROLES = 130;
    /**login exception : ${userName} login with invalid Role ${roleName}*/
    public static final int ERR_CODE_SESSION_LOGIN_ROLE_INVALID = 140;
    /**login exception : ${userName} can't login with no role assigned*/
    public static final int ERR_CODE_SESSION_LOGIN_NO_ROLE_ASSIGNED = 150;
    /**login exception : ${userName} can't login with session ${sessionId}*/
    public static final int ERR_CODE_SESSION_LOGIN_SESSION_ID_MISMATCH = 160;
    /**login exception : ${userName} can't login with Password Expiration*/
    public static final int ERR_CODE_PASSWORD_EXPIRATION = 170;

    /**Can't find a session*/
    public static final int ERR_CODE_SESSION_NOT_EXIST = 210;
    /**The role ${roleName} is invalid for current session (id=${sessionId})*/
    public static final int ERR_CODE_SESSION_ROLE_INVALID = 220; 

    /**Session header invalid: ${reason}*/
    public static final int ERR_CODE_SESSION_HEADER_INVALID = 300;
    
    /**Session handler error code. */
    public static final String SESSION_HANDLER_ERR_CODE = "Server.Session";
	/**
     * @param errCode
     * @param message
	 */
	public SessionException(int errCode, String message) {
		super(MIN_ERROR_CODE_SESSION + errCode, message);
	}
	/**
     * @param errCode
	 * @param cause
	 */
	public SessionException(int errCode, Throwable cause) {
		super(MIN_ERROR_CODE_SESSION + errCode, cause);
	}
    
    /**
     * Add a message as parameter, used in it's subclass
     * @param message
     */
    protected void addMessage(String message){
        this.addParameter("message", message);
    }
    
}
