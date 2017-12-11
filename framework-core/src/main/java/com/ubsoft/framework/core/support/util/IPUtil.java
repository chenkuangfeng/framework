package com.ubsoft.framework.core.support.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {

	    /**
	     * 判断当前操作是否Windows.
	     *
	     * @return true---是Windows操作系统
	     */
	    public static boolean isWindowsOS() {
	        boolean isWindowsOS = false;
	        String osName = System.getProperty("os.name");
	        if (osName.toLowerCase().indexOf("windows") > -1) {
	            isWindowsOS = true;
	        }
	        return isWindowsOS;
	    }

	    public static String getWebRequestIp(HttpServletRequest request) {
	        String ip = request.getHeader("x-forwarded-for");
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("Proxy-Client-IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("WL-Proxy-Client-IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("HTTP_CLIENT_IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getRemoteAddr();
	        }
	        return ip;
	    }
	    /**
	     * 获取本机IP地址，并自动区分Windows还是Linux操作系统
	     * @return String
	     */
	    public static String getLocalIP() {
	        InetAddress ip = null;
	        String sIP = null;
	        try {
	            // 如果是Windows操作系统
	            if (isWindowsOS()) {
	                ip = InetAddress.getLocalHost();
	            }
	            // 如果是Linux操作系统
	            else {
	                boolean bFindIP = false;
	                Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
	                        .getNetworkInterfaces();
	                while (netInterfaces.hasMoreElements()) {
	                    if (bFindIP) {
	                        break;
	                    }
	                    NetworkInterface ni = (NetworkInterface) netInterfaces
	                            .nextElement();
	                    // ----------特定情况，可以考虑用ni.getName判断
	                    // 遍历所有ip
	                    Enumeration<InetAddress> ips = ni.getInetAddresses();
	                    while (ips.hasMoreElements()) {
	                        ip = (InetAddress) ips.nextElement();
	                        if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
	                                && ip.getHostAddress().indexOf(":") == -1) {
	                            bFindIP = true;
	                            break;
	                        }
	                    }
	                }
	            }
	        } catch (Exception e) {
//	            logger.error(ExceptionUtil.getExceptionInfo(e));
	            e.printStackTrace();
	        }

	        if (null != ip) {
	            sIP = ip.getHostAddress();
	        }
	        return sIP;
	    }
	}

