package com.ubsoft.framework.core.support.util;

import java.security.MessageDigest;

/**
 * 采用MD5加密解密
 */
public class PasswordUtil {
	/***
	 * MD5加码 生成32位小写md5码
	 */
	public static String string2MD5(String inStr) {
		try {
			String sign = sign(inStr, "UTF-8");
			return sign;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
			
		} 
	}

	// MD5加码。32位小写
	public static String sign(String data, String encode) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data.getBytes(encode));
		byte[] bArray = md.digest();
		StringBuilder output = new StringBuilder(32);
		for (int i = 0; i < bArray.length; i++) {
			String temp = Integer.toHexString(bArray[i] & 0xff);
			if (temp.length() < 2) {
				output.append("0");
			}
			output.append(temp);
		}
		return output.toString();
	}

	// 测试主函数
	public static void main(String[] args) {
		String s = new String("<request dfdfdf>fdfdfdfdf<dfdfdf>dfdfdfdfdf");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + string2MD5(s));

	}
}
