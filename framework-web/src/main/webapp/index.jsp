<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String server = request.getServerName() + ":" + request.getServerPort();
	String siteName = request.getScheme() + "://" + server;
	String contextpath = request.getContextPath();
	String contextName = siteName + contextpath;
	//String applet = request.getParameter("__applet");
	//if (applet==null||applet.length()==0)
	//	applet = SPM.getProperties("CODEBASE_SERVICE");
	//String service = request.getParameter("__service");
	//if (service==null||service.length()==0)
	//	service = SPM.getProperties("APP_SERVICE");
	boolean loadloginui = true;
	boolean loadxml = true;
	if (request.getParameter("__loadloginui")!=null&&request.getParameter("__loadloginui").length()!=0) {
		loadloginui = Boolean.valueOf(request.getParameter("__loadloginui"));
	}
	
	if (request.getParameter("__loadxml")!=null&&request.getParameter("__loadxml").length()!=0) {
		loadxml = Boolean.valueOf(request.getParameter("__loadxml"));
	}
%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8,chrome=1" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>YIGO</title>
</head>
<bofy>
<applet name="myApplet" width="1024" height="768" archive="mainframe.jar" align="middle" id="myApplet" codebase="." code="com.ubsoft.framework.system.StartApplet.class" type="applet">
</applet>
</body>
</html>