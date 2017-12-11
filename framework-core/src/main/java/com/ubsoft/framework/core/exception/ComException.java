package com.ubsoft.framework.core.exception;

import java.util.HashMap;
import java.util.Map;
	/**
	 * <p>
	 * ComException是Server端所有Exception的Base Class.	 * 
	 * 
	 */
	public class ComException extends RuntimeException { 
		
		private static final long serialVersionUID = 1L;

		/**
		 * 默认的Error Code
		 */
		public static final int ERROR_CODE_GENERAL = 0;

		/**
		 * 默认的Error Source
		 */
		public static final String ERROR_SOURCE_UNDEFINED = "undefined";

		// ERROR CODE SCOPE 

		/**
		 * 模块无关的异常取值范围从0开始
		 */
		public static final int MIN_ERROR_CODE_GLOBAL = 0;

		

		/**
		 * 暴漏给用户的异常，在客户端用MessageBox提示，比如密码错误等
		 */
		public static final int MIN_ERROR_CODE_USER = 1000;
	
		/**
		 * DAL ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_DAL = 3000;

		/**
		 * Security ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_SECURITY = 4000;

		/**
		 * Session ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_SESSION = 5000;

		/**
		 * log ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_BUSINESS_LOG = 6000;

		/**
		 * Config ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_CONFIGURATION = 7000;

		/**
		 * Gateway ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_GATEWAY = 8000;

		/**
		 * LockManager ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_LOCK_MANAGER = 9000;

		/**
		 * Report ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_REPORT = 10000;

		/**
		 * SecurityManager ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_SECURITY_MANAGER = 11000;

		/**
		 * Entity ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_FDM = 12000;
		/**
		 * Entity ERROR CODE起始值
		 */
		public static final int MIN_ERROR_CODE_Entity = 13000;

	

		public static final int MIN_ERROR_CODE_WX = 20000;
		
		private int code;

		private String source;

		private String originalMessage;

		private Map<String, Object> parameters=new HashMap<String, Object>();

		private static boolean returnStackTrace = true; // 标记是否在getMessage中返回StackTrace

		/**
		 * 设置是否返回 StackTrace 信息给客户端
		 * @param flag
		 */
		public static void setReturnStackTrace(boolean flag) {
			returnStackTrace = flag;
		}

		public static boolean isReturnStackTrace(){
			
			return returnStackTrace;
		}

		private void copyProperties(ComException cause) {
			this.code = cause.getCode();
			this.source = cause.getSource();
			this.originalMessage = cause.getOriginalMessage();
			this.parameters = cause.getParameters();
		}
		
		private final static ComException getFirstComException(Throwable exception){
			Throwable ex = exception;
			while (ex != null) {
				if (ex instanceof ComException){
					return (ComException)ex;
				}
				ex = ex.getCause();
			}
			return null;
		}	

		/**
		 * 从已有的 Exception 创建一个 ComException.<br/>
		 * 如果 cause 是 ComException 的子类，或者其中包含有 ComException 的子类, 则 code、source 等信息从这
		 * 个 ComException 复制, 否则 code、source 等属性使用默认值, 同时 <b>orginialMessage 取 cause.getMessage()</b>.<br/>
		 * @param cause	来源 Exception 
		 */
		public ComException(Throwable cause) {
			super(cause);
			
			ComException hex = getFirstComException(cause);
			if (null!=hex) {
				copyProperties(hex);
			} else {
				this.code = ERROR_CODE_GENERAL;
				this.source = parseSource(cause);
				while (cause.getCause() != null) {
					cause = cause.getCause();
				}				
				this.originalMessage = cause.getMessage();
			}
		}

		/**
		 * 从已有的 Exception 创建一个 ComException.<br/>
		 * 如果 cause 是 ComException 的子类，或者其中包含有 ComException 的子类, 则 code、source 等信息从这
		 * 个 ComException 复制, 否则 code、source 等属性使用默认值, 同时 <b>orginialMessage 取传入的 message 参数值</b>.<br/>
		 * @param message	Exception message
		 * @param cause		来源 Exception
		 */
		public ComException(String message, Throwable cause) {
			super(message, cause);

			ComException hex = getFirstComException(cause);
			if (null!=hex) {
				copyProperties(hex);
			} else {
				this.code = ERROR_CODE_GENERAL;
				this.source = parseSource(cause);
				this.originalMessage = message;
			}
		}

		/**
		 * 创建一个 ComException对象，这个构造函数通常在最底层截获一个非 ComException 时（包括系统级 Exception 
		 * 和第三方 Exception）调用。
		 * @param code				Exception Code	
		 * @param originalMessage	Exception OriginalMessage
		 */
		public ComException(int code, String originalMessage) {
			super(originalMessage);
			this.code = code;
			this.source = parseSource(null);
			this.originalMessage = originalMessage;
		}

		/**
		 * 创建一个 ComException 对象，这个构造函数通常在最底层截获一个非 ComException 时（包括系统级 Exception 
		 * 和第三方 Exception）调用。<br/>
		 * 如果 cause 是 ComException 的子类，或者其中包含有 ComException 的子类, 则 code, source 等信息从这
		 * 个 ComException 复制, 否则 code 使用传入参数值, source 等属性使用默认值, 同时 <b>orginialMessage 取
		 * cause.getMessage()</b>.<br/>
		 * @param code				Exception Code	
		 * @param cause				来源Exception
		 */
		public ComException(int code, Throwable cause) {
			super(cause);
			ComException hex = getFirstComException(cause);
			if (null!=hex) {
				copyProperties(hex);	//这里复制过来的 code 将覆盖参数中的 code
			} else {
				this.code = code;
				this.source = parseSource(cause);
				while (cause.getCause() != null) {
					cause = cause.getCause();
				}		
				this.originalMessage = cause.getMessage();
			}
		}

		private String parseSource(Throwable cause) {

			if (cause != null) {
				if (cause.getStackTrace() != null && cause.getStackTrace().length > 0) {
					return cause.getStackTrace()[0].toString();
				}
				else {
					return ComException.ERROR_SOURCE_UNDEFINED;
				}
			}else {
				if (this.getStackTrace() != null && this.getStackTrace().length > 0) {
					return this.getStackTrace()[0].toString();
				}else {
					return ComException.ERROR_SOURCE_UNDEFINED;
				}
			}
		}

		/**
		 * 返回Exception的Code
		 * @return
		 */
		public int getCode() {
			return this.code;
		}

		/**
		 * 返回Exception的source
		 * @return
		 */
		public String getSource() {
			return this.source;
		}

		/**
		 * 返回Exception的原始信息
		 * @return
		 */
		public String getOriginalMessage() {
			return this.originalMessage;
		}
		
		public ComException addParameter(String name, Object value) {
			if(value==null){
				return this;
			}
			this.parameters.put(name, value);
			return this;
		}

		/**
		 * 返回Exception的参数列表
		 * @return
		 */
		public Map<String, Object> getParameters() {
			return this.parameters; 
		}
		
		public String getStackTraceString(){
			return parameters.get("StackTrace")+"";
		}
		
}
