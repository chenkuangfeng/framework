package com.ubsoft.framework.core.support.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import sun.misc.BASE64Encoder;

import com.ubsoft.framework.core.support.json.JsonHelper;

public class HttpClientUtil {
	// POST请求模式
	public final static String HTTP_METHOD = "POST";
	// 请求格式参数
	public final static String CONTENT_TYPE = "Content-Type";
	// 请求格式
	// soap 1.2
	public final static String APP_SOAP12 = "application/soap+xml; charset=UTF-8";
	// soap 1.1
	public final static String APP_SOAP11 = "application/xml; charset=UTF-8";

	public final static String APP_JSON = "application/json; charset=UTF-8";

	public final static String APP_FORM = "	application/x-www-form-urlencoded; charset=UTF-8";

	public final static String APP_OCT_STREAM = "application/octet-stream; charset=UTF-8";
	// 连接超时
	public final static int CONNECT_TIME_OUT = 3600000;
	// 返回超时
	public final static int READ_TIME_OUT = 3600000;

	/**
	 * HTTP客户端POST请求方法
	 * 
	 * @param host
	 *            访问的地址
	 * @param postData
	 *            数据
	 * @return 服务器返回的结果数据
	 * @throws Exception
	 *             请求的异常信息
	 */
	public static String post(String host, String postData) throws Exception {
		return HttpClientUtil.post(host, postData, null, null, null);

	}

	/**
	 * HTTP客户端POST请求方法
	 * 
	 * @param host
	 *            访问的地址
	 * @param postData
	 *            数据
	 * @return 服务器返回的结果数据
	 * @throws Exception
	 *             请求的异常信息
	 */
	public static String post(String host, String postData, String contentType) throws Exception {
		return HttpClientUtil.post(host, postData, contentType, null, null);

	}

	/**
	 * 
	 * @param host
	 * @param postData
	 * @param contentType
	 * @param userName
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public static String post(String host, String postData, String contentType, String userName, String pwd) throws Exception {
		// Logs.debug("准备连接服务:" + host);
		// Logs.debug("发送数据:" + postData);
		URL url = null;
		HttpURLConnection connection = null;
		OutputStreamWriter out = null;
		InputStream urlInStream = null;
		BufferedReader reader = null;
		StringBuilder totalString = new StringBuilder();
		try {
			// 创建连接对象
			url = new URL(host);
			// 建立连接
			connection = (HttpURLConnection) url.openConnection();
			if (userName != null && pwd != null) {
				String auth = userName + ":" + pwd;
				BASE64Encoder enc = new BASE64Encoder();
				String encoding = enc.encode(auth.getBytes());
				connection.setRequestProperty("Authorization", "Basic " + encoding);
			}
			// Logs.debug("已经建立连接.");
			// 把连接设为输出模式。URLConnection通常作为输入来使用
			connection.setDoOutput(true);
			// 把连接设为输入模式
			connection.setDoInput(true);
			// POST请求模式
			connection.setRequestMethod(HTTP_METHOD);
			// 设置本次发送请求的数据格式为 application_xml
			if (CONTENT_TYPE != null) {
				connection.setRequestProperty(CONTENT_TYPE, contentType);
			} else {
				connection.setRequestProperty(CONTENT_TYPE, APP_JSON);
			}
			// 设置连接超时时限
			connection.setConnectTimeout(CONNECT_TIME_OUT);
			// 设置返回超时时限
			connection.setReadTimeout(READ_TIME_OUT);
			// 指定字符集，对URL进行encoded
			// connection.addRequestProperty("Content-type", "application/x");//
			// setRequestProperty方法，如果key存在，则覆盖；不存在，直接添加。addRequestProperty方法，不管key存在不存在，直接添加。
			// connection.addRequestProperty("charset", "UTF-8");
			// 建立数据发送通道
			out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			// 发送数据
			out.write(postData);
			// 清空通道
			out.flush();
			// Logs.debug("数据发送完毕.");
			// 读取服务器返回结果
			String currentLine = null;
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 建立数据读取通道
				urlInStream = connection.getInputStream();
				// 包装通道
				reader = new BufferedReader(new InputStreamReader(urlInStream, "UTF-8"));
				// int lineNum = 1;
				while ((currentLine = (reader.readLine())) != null) {
					// if (lineNum > 1)
					// totalString.append("\n");
					totalString.append(currentLine);
					// lineNum ++;
				}
			} else {
				urlInStream = connection.getInputStream();
				// 包装通道
				reader = new BufferedReader(new InputStreamReader(urlInStream, "UTF-8"));
				// int lineNum = 1;
				while ((currentLine = (reader.readLine())) != null) {
					System.out.println(currentLine);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			// 释放连接资源
			if (connection != null) {
				connection.disconnect();
			}
			if (out != null) {
				out.close();
			}
			if (urlInStream != null) {
				urlInStream.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
		// 返回请求结果数据
		return totalString.toString();

	}

	
	
	public static String upload(String host, String file) throws Exception {
		String fileName = "fileName";
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		URL url = null;
		HttpURLConnection connection = null;
		OutputStreamWriter out = null;
		InputStream urlInStream = null;
		BufferedReader reader = null;
		StringBuilder totalString = new StringBuilder();
		try {
			// 创建连接对象
			url = new URL(host);
			// 建立连接
			connection = (HttpURLConnection) url.openConnection();
			// Logs.debug("已经建立连接.");
			// 把连接设为输出模式。URLConnection通常作为输入来使用
			connection.setDoOutput(true);
			// 把连接设为输入模式
			connection.setDoInput(true);
			// POST请求模式
			connection.setRequestMethod(HTTP_METHOD);

			// 设置连接超时时限
			connection.setConnectTimeout(CONNECT_TIME_OUT);
			// 设置返回超时时限
			connection.setReadTimeout(READ_TIME_OUT);
			// 指定字符集，对URL进行encoded
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			connection.addRequestProperty("charset", "UTF-8");
			// 建立数据发送通道
			// out = new OutputStreamWriter(connection.getOutputStream(),
			// "UTF-8");
			DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; " + "name=\"file0\";filename=\"" + fileName + "\"" + end);
			ds.writeBytes(end);
			FileInputStream fStream = new FileInputStream(file);
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			// 发送数据
			while ((length = fStream.read(buffer)) != -1) {
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();

			// 读取服务器返回结果
			String currentLine = null;
			// 建立数据读取通道
			urlInStream = connection.getInputStream();
			// 包装通道
			reader = new BufferedReader(new InputStreamReader(urlInStream, "UTF-8"));
			// int lineNum = 1;
			while ((currentLine = (reader.readLine())) != null) {
				// if (lineNum > 1)
				// totalString.append("\n");
				totalString.append(currentLine);
				// lineNum ++;
			}
			// Logs.debug("数据接收完毕.");

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			// 释放连接资源
			if (connection != null) {
				connection.disconnect();
			}
			if (out != null) {
				out.close();
			}
			if (urlInStream != null) {
				urlInStream.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
		// 返回请求结果数据
		return totalString.toString();

	}

	public static String get(String host, String params) throws Exception {

		URL url = null;
		HttpURLConnection connection = null;
		OutputStreamWriter out = null;
		InputStream urlInStream = null;
		BufferedReader reader = null;
		StringBuilder totalString = new StringBuilder();
		try {
			// 创建连接对象
			url = new URL(host + "?" + params);
			// 建立连接
			connection = (HttpURLConnection) url.openConnection();

			// 设置本次发送请求的数据格式为 application_xml
			connection.setRequestProperty(CONTENT_TYPE, APP_OCT_STREAM);
			// 设置连接超时时限
			connection.setConnectTimeout(CONNECT_TIME_OUT);
			// 设置返回超时时限
			connection.setReadTimeout(READ_TIME_OUT);
			// 指定字符集，对URL进行encoded
			connection.addRequestProperty("Content-type", "application/x-www-form-urlencoded");// setRequestProperty方法，如果key存在，则覆盖；不存在，直接添加。addRequestProperty方法，不管key存在不存在，直接添加。
			connection.addRequestProperty("charset", "UTF-8");

			// Logs.debug("数据发送完毕.");
			// 读取服务器返回结果
			String currentLine = null;
			// 建立数据读取通道
			urlInStream = connection.getInputStream();
			// 包装通道
			reader = new BufferedReader(new InputStreamReader(urlInStream, "UTF-8"));
			// int lineNum = 1;
			while ((currentLine = (reader.readLine())) != null) {
				// if (lineNum > 1)
				// totalString.append("\n");
				totalString.append(currentLine);
				// lineNum ++;
			}
			// Logs.debug("数据接收完毕.");

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			// 释放连接资源
			if (connection != null) {
				connection.disconnect();
			}
			if (out != null) {
				out.close();
			}
			if (urlInStream != null) {
				urlInStream.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
		// 返回请求结果数据
		return totalString.toString();
	}

	public static void main(String[] args) throws Exception {

		/*
		 * String postInfo = "inputPara=<?xml version='1.0' encoding='UTF-8'?>";
		 * postInfo +=
		 * "<requestinfo><reqBegDate>20150923 21:57:05</reqBegDate><reqEndDate>20150924 09:57:15</reqEndDate><expComCode>YTO</expComCode></requestinfo>"
		 * ;
		 * 
		 * //postInfo=
		 * "<requestinfo><reqBegDate>20150923205853</reqBegDate><reqEndDate>20150924085853</reqEndDate>"
		 * ;
		 * 
		 * //postInfo=
		 * "<requestinfo><reqBegDate>20150923 20:58:53</reqBegDate><reqEndDate>20150924 08:58:53</reqEndDate>"
		 * ; // postInfo //+=
		 * "<requestinfo><reqBegDate>20150602 11:12:01</reqBegDate><reqEndDate>20150603 16:00:00</reqEndDate>"
		 * // postInfo+= "<expComCode>YTO</expComCode></requestinfo>";
		 * 
		 * String s =
		 * HttpClientUtil.post("http://222.73.66.100:81/Gateway/rms/api/express.do"
		 * , postInfo); // String s = //
		 * HttpClientUtil.post("http://localhost:81/Gateway/rms/api/express.do",
		 * // postInfo);
		 */
		String s = HttpClientUtil.get("http://bms.microc.cn/shopguide/api/common/checkToken", "accessToken=ffee61742d7a461309795bc0ca4270a6_csb");
		HashMap result = (HashMap) JsonHelper.json2Bean(s, HashMap.class);

		System.out.println(result.get("value"));
	}

}