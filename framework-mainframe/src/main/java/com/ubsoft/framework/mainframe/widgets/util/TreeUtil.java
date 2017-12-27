package com.ubsoft.framework.mainframe.widgets.util;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TreeUtil {

	public static void main(String [] args) {
		ScriptEngineManager manager = new ScriptEngineManager();  
        ScriptEngine engine = manager.getEngineByName("JavaScript"); 		
		ScriptContext context = engine.getContext();
		Bindings bindings1 = engine.createBindings();
		Bindings bindings2 = engine.createBindings();
		TreeUtil user=new TreeUtil();bindings2.put("user", user);
		context.setBindings(bindings2, ScriptContext.ENGINE_SCOPE);
		try {
			engine.eval("user.test();");
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Bob
	}
	
	public void test(){
		System.out.println("script111 enginetest");
	}
}
