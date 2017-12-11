package com.ubsoft.framework.core.support.file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 
 * @ClassName: FileHelper
 * @Description: 文件处理类
 * @author chenkf
 * @date 2017-3-15 下午12:55:35
 * @version V1.0
 */
public class FileHelper {
	public static String readFile(String filePath) throws IOException {
		StringBuffer sb = new StringBuffer();
		InputStream is = new FileInputStream(filePath);
		String line; // 用来保存每行读取的内容

		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
		line = reader.readLine(); // 读取第一行
		while (line != null) { // 如果 line 为空说明读完了
			sb.append(line); // 将读到的内容添加到 buffer 中

			line = reader.readLine(); // 读取下一行
		}
		reader.close();
		is.close();
		return sb.toString();
	}

	/**
	 * 
	* @Title: writeFile
	* @Description: TODO
	* @author chenkf
	* @date  2017-3-15 下午01:54:26
	* @param filePath
	* @param content
	* @throws IOException
	 */
	public static void writeFile(String filePath, String content) throws IOException {
		
		OutputStream is = new FileOutputStream(filePath);
		OutputStreamWriter writer=new OutputStreamWriter(is,"UTF-8");
		writer.write(content);
		writer.close();
		is.close();

	}

	public static byte[] fileToByte(String filePath) throws Exception {  
        byte[] data = new byte[0];  
        File file = new File(filePath);  
        if (file.exists()) {  
            FileInputStream in = new FileInputStream(file);  
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);  
            byte[] cache = new byte[1024];  
            int nRead = 0;  
            while ((nRead = in.read(cache)) != -1) {  
                out.write(cache, 0, nRead);  
                out.flush();  
            }  
            out.close();  
            in.close();  
            data = out.toByteArray();  
         }  
        return data;  
    }  
	
	
	  public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {  
	        InputStream in = new ByteArrayInputStream(bytes);     
	        File destFile = new File(filePath);  
//	        if (!destFile.getParentFile().exists()) {  
//	            destFile.getParentFile().mkdirs();  
//	        }  
	        destFile.createNewFile();  
	        OutputStream out = new FileOutputStream(destFile);  
	        byte[] cache = new byte[1024];  
	        int nRead = 0;  
	        while ((nRead = in.read(cache)) != -1) {     
	            out.write(cache, 0, nRead);  
	            out.flush();  
	        }  
	        out.close();  
	        in.close();  
	    }  
}
