package com.ubsoft.framework.core.exception;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @version 1
 * @author chenkf
 */
public class ExceptionHelper {

	/**
	 * 统一异常处理,json格式
	 * 
	 * @param e
	 * @return
	 */
	public static RuntimeException dealRuntimeException(Throwable e) {

		if (e instanceof ComException) {
			return dealRuntimeExceptionJson((ComException) e);
		} else {
			return dealRuntimeExceptionJson(new ComException(-1, e));
		}
	}

	public static RuntimeException dealRuntimeException1(Throwable e) {

		if (e instanceof ComException) {
			return  (ComException)e;
		} else {
			return new ComException(-1,e.getMessage());
		}
	}

	private final static Throwable getRootCause(Throwable exception) {
		Throwable ex = exception;
		while (ex.getCause() != null) {
			ex = ex.getCause();
		}
		return ex;
	}

	/**
	 * exception格式 { code ："123", source:"ddff", originalMessage:"fdfdf",
	 * message:"dff", parameters:[{name:'',value:''}], stacktrace:{
	 * exception:{classname:'',message:'' sources:['1212','fdfdf'] }
	 * 
	 * } }
	 * 
	 * @param e
	 * @return
	 */
	private static RuntimeException dealRuntimeExceptionJson(ComException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("{").append("\n");
		sb.append(" code:'").append(String.valueOf(e.getCode())).append("',").append("\n");
		sb.append(" source:'").append(e.getSource()).append("',").append("\n");
		sb.append(" originalMessage:'").append(e.getOriginalMessage()).append("',").append("\n");
		sb.append(" message:'").append(e.getMessage()).append("',").append("\n");
		sb.append(" parameters:").append("\n");
		sb.append("  [").append("\n");
		if (e.getParameters() != null) {
			Iterator<String> itr = e.getParameters().keySet().iterator();
			int j = e.getParameters().keySet().size();
			int i = 0;
			while (itr.hasNext()) {
				String name = (String) itr.next();
				sb.append("   {").append("\n");
				sb.append("    name:'").append(name).append("',").append("\n");
				sb.append("    value:'").append(name).append("'").append("\n");
				sb.append("   }");
				if (i != j - 1) {
					sb.append(",").append("\n");
				} else {
					sb.append("\n");
				}
				i++;
			}
		}
		if (ComException.isReturnStackTrace())
			sb.append("  ],").append("\n");
		else {
			sb.append("  ]").append("\n");
		}

		if (ComException.isReturnStackTrace()) {
			Throwable ex = getRootCause(e);
			sb.append(" stacktrace:").append("\n");
			sb.append("  {").append("\n");
			sb.append("   classname:'").append(ex.getClass().getName()).append("',").append("\n");
			if (ex instanceof ComException) {
				sb.append("   message:'").append(((ComException) ex).getOriginalMessage()).append("',").append("\n");
			} else {
				sb.append("   message:'").append(ex.getMessage()).append("',").append("\n");
			}

			ex = e;
			sb.append("   sources:").append("\n");
			sb.append("    [").append("\n");
			while (ex.getCause() != null) {
				if (ex.getStackTrace()[0].toString().indexOf(ExceptionHelper.class.getName()) == -1) {
					sb.append("     '").append(ex.getStackTrace()[0].toString()).append("',").append("\n");

				}
				ex = ex.getCause();
			}

			for (int i = 0; ex.getStackTrace() != null && i < ex.getStackTrace().length; i++) {

				sb.append("     '").append(ex.getStackTrace()[i].toString()).append("'");
				if (i < ex.getStackTrace().length - 1) {
					sb.append(",").append("\n");
				} else {
					sb.append("\n");
				}
			}
			sb.append("    ]").append("\n");
			// display exception wrap constructor

			sb.append("  }").append("\n");
		}
		sb.append("}");
		RuntimeException runtimeEx = new RuntimeException(sb.toString());

		return runtimeEx;

	}

	private static RuntimeException dealRuntimeExceptionXml(ComException e) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("ComException");
		setElementText(root.addElement("code"), String.valueOf(e.getCode()));
		setElementText(root.addElement("source"), e.getSource());
		setElementText(root.addElement("originalMessage"), e.getOriginalMessage());
		setElementText(root.addElement("message"), e.getMessage());
		Element paramsElement = root.addElement("parameters");
		// parameters
		if (e.getParameters() != null) {
			Iterator<String> itr = e.getParameters().keySet().iterator();
			while (itr.hasNext()) {
				String name = (String) itr.next();
				Element param = paramsElement.addElement("parameter");
				setElementText(param.addElement("name"), name);
				if (e.getParameters().get(name) == null) {
					setElementText(param.addElement("value"), "");
				} else {
					setElementText(param.addElement("value"), e.getParameters().get(name).toString());
				}
			}
		} // stack trace
		if (ComException.isReturnStackTrace()) {
			Element stacktrace = root.addElement("stacktrace");
			// Encoding stacktrace
			Throwable ex = getRootCause(e);
			Element trace = stacktrace.addElement("exception");
			setElementText(trace.addElement("classname"), ex.getClass().getName());
			if (ex instanceof ComException) {
				setElementText(trace.addElement("message"), ((ComException) ex).getOriginalMessage());
			} else {
				setElementText(trace.addElement("message"), ex.getMessage());
			}
			Element sources = trace.addElement("sources");
			ex = e;
			// display exception wrap constructor
			while (ex.getCause() != null) {
				if (ex.getStackTrace()[0].toString().indexOf(ExceptionHelper.class.getName()) == -1) {
					setElementText(sources.addElement("source"), ex.getStackTrace()[0].toString());
				}
				ex = ex.getCause();
			}
			// separator
			setElementText(sources.addElement("source"), "");
			// stacktace
			for (int i = 0; ex.getStackTrace() != null && i < ex.getStackTrace().length; i++) {
				setElementText(sources.addElement("source"), ex.getStackTrace()[i].toString());
			}
		}

		OutputFormat format = new OutputFormat("", true, "UTF-8");
		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = new XMLWriter(stringWriter, format);
		try {
			writer.write(document);
			writer.close();
		} catch (IOException ioe) {
			String errorMessage = "Error with XMLWriter";

			throw new RuntimeException(errorMessage, ioe);
		}

		return new RuntimeException(stringWriter.toString());

	}

	private static void setElementText(Element element, String text) {
		if (text == null) {
			text = "";
		}
		element.setText(text);
	}

	public static String getStackTrace(Throwable e) {
		StringBuffer sb = new StringBuffer();
		Throwable ex = getRootCause(e);
		sb.append("stacktrace:").append("\n");

		sb.append("    classname:").append(ex.getClass().getName()).append("\n");
		if (ex instanceof ComException) {
			sb.append("    message:").append(((ComException) ex).getOriginalMessage()).append("\n");
		} else {
			sb.append("    message:").append(ex.getMessage()).append("\n");
		}

		ex = e;
		sb.append("sources:").append("\n");

		while (ex.getCause() != null) {
			if (ex.getStackTrace()[0].toString().indexOf(ExceptionHelper.class.getName()) == -1) {
				sb.append("    ").append(ex.getStackTrace()[0].toString()).append("\n");

			}
			ex = ex.getCause();
		}

		for (int i = 0; ex.getStackTrace() != null && i < ex.getStackTrace().length; i++) {

			sb.append("    ").append(ex.getStackTrace()[i].toString()).append("\n");

		}
		return sb.toString();
	}

	public static ComException getFirstComException(Throwable exception) {
		Throwable ex = exception;
		while (ex != null) {
			if (ex instanceof ComException) {
				return (ComException) ex;
			}
			ex = ex.getCause();
		}
		return null;
	}
}
