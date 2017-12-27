package com.ubsoft.framework.mainframe.widgets.util;

import java.util.Map;

import javax.swing.JOptionPane;

import com.ubsoft.framework.core.exception.ExceptionHelper;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.mainframe.widgets.component.ErrorDialog;

public class MessageBox {

	public static void showInfo(String info) {
		JOptionPane.showMessageDialog(null, info, "提示", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showError(String error) {
		JOptionPane.showMessageDialog(null, error, "异常信息", JOptionPane.ERROR_MESSAGE);
	}

	public static void showException(Throwable e) {
		String message=e.getMessage();
		String stack =message;		
		int code=0;
		if(message!=null &&message.startsWith("{")){
			message=replace(message);//.replace("\"", "").replace("(", "").replace(")", "");
			Map ex=(Map) JsonHelper.json2Bean(message, Map.class);
			code=Integer.parseInt(ex.get("code")+"");			
			message=code+":"+ex.get("message");			
		}else{		
			stack=ExceptionHelper.getStackTrace(e);
		}
		
		if (message != null && message.length() >= 70) {
			message =  message.substring(0, 70)+"...";
		}
		//1000-3000是用户自定义异常，前端提示
		if (code>=1000  && code < 3000) {
			JOptionPane.showMessageDialog(null, message, "提示", JOptionPane.INFORMATION_MESSAGE);				
		} else {				
			ErrorDialog dialog = new ErrorDialog(message, stack);
			// dialog.setPreferredSize(new Dimension(400,300));
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			
		}
	} 
	/*
		ComException ex = ExceptionHelper.getFirstComException(e);
		String stack = ExceptionHelper.getStackTrace(e);
		String title;
		if (ex != null) {
			title = ex.getCode() + ":"+ex.getOriginalMessage();
			
			if (title != null && title.length() >= 60) {
				title =  title.substring(0, 60)+"...";
			}
			if (ex instanceof UserException) {
				JOptionPane.showMessageDialog(null, title, "提示", JOptionPane.INFORMATION_MESSAGE);				
			} else {				
				ErrorDialog dialog = new ErrorDialog(title, stack);
				// dialog.setPreferredSize(new Dimension(400,300));
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
				// JOptionPane.showMessageDialog(null,
				// ex.getCode()+":"+ex.getMessage(), "异常信息",
				// JOptionPane.ERROR_MESSAGE);
			}
		} else {
			title = e.getMessage();
			if (title != null && title.length() > 60) {
				title = title.substring(0, 60);
			}
			ErrorDialog dialog = new ErrorDialog(title, stack);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);

		}
		*/
	
	  public static String replace(String s) {        
	        StringBuffer sb = new StringBuffer();        
	        for (int i=0; i<s.length(); i++) {  
	            char c = s.charAt(i);    
	             switch (c){  
	             case '\"':        
	                 sb.append("\\\"");        
	                 break;        
	             case '\\':        
	                 sb.append("\\\\");        
	                 break;        
	             case '/':        
	                 sb.append("\\/");        
	                 break;        
	             case '\b':        
	                 sb.append("\\b");        
	                 break;        
	             case '\f':        
	                 sb.append("\\f");        
	                 break;        
	             case '\n':        
	                 sb.append("");        
	                 break;        
	             case '\r':        
	                 sb.append("\\r");        
	                 break;        
	             case '\t':        
	                 sb.append("\\t");        
	                 break;        
	             default:        
	                 sb.append(c);     
	             }  
	         }      
	        return sb.toString();     
	        }  


	
}
