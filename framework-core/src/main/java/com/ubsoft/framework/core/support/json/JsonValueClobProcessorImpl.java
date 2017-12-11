package com.ubsoft.framework.core.support.json;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonValueClobProcessorImpl implements JsonValueProcessor {

	@Override
	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
//		// TODO Auto-generated method stub
//		String[] obj = {};
//		if (value instanceof SerializableClob[]) {
//			SerializableClob[] clobs = (SerializableClob[])value;
//			
//			obj = new String[clobs.length];
//			for (int i = 0; i < clobs.length; i++) {
//				//obj[i] = clobs[i].;
//				try {
//					obj[i]= ((CLOB) ((org.hibernate.lob.SerializableClob)clobs[i]).getWrappedClob()).stringValue();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		}
//		
//		return obj;
//	}
//
//	@Override
//	public Object processObjectValue(String key, Object value,
//			JsonConfig jsonConfig) {
//		// TODO Auto-generated method stub
//		if(Utl.isEmpty(value))
//			return "";
//		if(value instanceof SerializableClob)
//		{
//			try {
//				return ((CLOB) ((org.hibernate.lob.SerializableClob)value).getWrappedClob()).stringValue();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return value.toString();
//	}

}
