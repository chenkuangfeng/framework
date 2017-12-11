package com.ubsoft.framework.core.support.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class DateUtil {
	public static final String dateFormat ="yyyy-MM-dd";
	public static final String dateTimeFormat ="yyyy-MM-dd HH:mm:ss";
	/**
	 * 获得当前的时间
	 */
	public static Date now() {
		return (new Date());
	}

	

	/**
	 * 格式化日期到指定格式<br>
	 * (具体格式参见java.text.SimpleDateFormat的有关文档)
	 * @param date 日期时间对象
	 * @param fmt 格式字符串
	 * @param loc 地区编码
	 * @return 经过格式化的日期字符串
	 */
	public static String date2String(Date date, String fmt) {
		SimpleDateFormat ofmt = new SimpleDateFormat(fmt);
		return (ofmt.format(date));
	}	

	/**
	 * 将标准的日期/时间字符串自动转换为日期<br>
	 * 目前只处理6种格式 :<br>
	 * yyyy/MM/dd HH:mm:ss, yyyy/M/d H:m:s <br>
	 * yyyy/MM/dd HH:mm, yyyy/M/d H:m <br>
	 * yyyy/MM/dd, yyyy/M/d  <br>
	 * 其中日期的分隔符除了可以是'/'之外, 还可以是'-'和'.', 但时间分隔符只能是':'! <br>
	 * 注意此函数是建立在这样一个事实的基础上:<br>
	 * 1)yyyy/M/d H:m:s的模式也可以用于解析yyyy/MM/dd HH:mm:ss模式;<br>
	 * 2)实际输入字符串中各部分数字前的空格并不影响解析;(例如:"  2002/ 4/ 06   1: 05")<br>
	 * @param dateString 日期时间字符串
	 * @param loc 地区编码
	 * @return 解析得到的日期时间对象, 如果解析失败, 返回 null
	 * @deprecated 此方法无法处理时区, 建议使用 stdString2Date(...) 代替
	 */
	public static Date string2Date(String dateString) {
		String sDate = dateString;		
		Date date=null;
		//处理头尾空格
		sDate = sDate.trim();
		//首先将'-','.'分隔符替换为'/'分隔符
		sDate = StringUtil.replace(sDate, "/", "-");
		sDate = StringUtil.replace(sDate, ".", "-");
		for (int i=0; i<DATE_FORMATS.length; i++){
			date = string2Date(sDate, DATE_FORMATS[i]);
			if (date != null) {
				break;				
			}
		}
		return date;
	}

	public static Date string2DateTime(String dateString, Locale loc) {
		String sDate = dateString;		
		Date date=null;
		//处理头尾空格
		sDate = sDate.trim();
		//首先将'-','.'分隔符替换为'/'分隔符
		sDate = StringUtil.replace(sDate, "-", "/");
		sDate = StringUtil.replace(sDate, ".", "/");
		
		for (int i=0; i<DATETIME_FORMATS.length; i++){
			date = string2Date(sDate, DATETIME_FORMATS[i]);
			if (date != null) {
				break;
			}
		}
		return date;
	}
	
	

	/**
	 * 将日期时间字符串解析到日期时间对象
	 * @param sDate 日期时间字符串
	 * @param fmt 解析格式字符串
	 * @return 解析得到的日期时间对象, 如果解析失败, 返回 null
	 */
	public static Date string2Date(String sDate, String fmt) {
		Date date;
		sDate = StringUtil.replace(sDate, "/", "-");
		sDate = StringUtil.replace(sDate, ".", "-");
		SimpleDateFormat ofmt = new SimpleDateFormat(fmt);
		try {
			date = ofmt.parse(sDate);
		}
		catch (ParseException ex) {
			date = null;
		}
		return (date);
	}
	
	/**
	 * 将时间字符串解析成日期时间对象
	 * @param sDate 时间字符串
	 * @return 解析得到的日期时间对象, 如果解析失败, 返回 null
	 * @deprecated 此方法无法处理时区, 建议使用 stdString2Time(...) 代替
	 */
	public static Date string2Time(String sDate) {
		String s1 = "H:m:s";
		String s2 = "H:m";
		Date date;
		date = string2Date(sDate, s1);
		if (date != null)
			return date;
		date = string2Date(sDate, s2);
		if (date != null)
			return date;
		return null;
	}

	/**
	 * 返回系统约定的无效日期: 1900/01/01 00:00:00
	 * @return
	 */
	public static Date getInvalidDate() {
		return string2Date("1900/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * 判断日期是否是系统定义的有效日期
	 * @param dt
	 * @return 大于 1900 年的日期被认为是有效日期
	 */
	public static boolean isDateValid(Date dt) {
		if (null == dt)
			return false;

		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		int year = c.get(Calendar.YEAR);
		return (year > 1900);
	}

	/**
	 * 将一个包含日期/时间的Date类型变量的时间部分除去(注意: 这个方法不影响时间类型变量的时区)
	 * @param date
	 * @return
	 */
	public static Date truncateTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 以指定间隔获得起始日期/时间和结束日期/时间之间的所有日期/时间
	 * @param from 其实日期/时间
	 * @param to 结束日期/时间
	 * @param field 指定间隔 Calendar.YEAR, MONTH, DATE, HOUR, MINUTE, SECOND, 等等
	 * @param step 指定步长
	 * @return 包含其中日期/时间的数组,
	 *         在 from != to 的情况下, 返回数组的头必然是 from, 但是尾可能不包括 to;
	 *         如果 from == to, 则返回的数组中只有一个元素 [0] == from == to.
	 * <br>如果 from > to, 返回结果中 from 和 to 将会互换
	 */
	public static Date[] getDateRange(Date from, Date to, int field, int step) {
		Calendar calFm = Calendar.getInstance();
		Calendar calTo = Calendar.getInstance();
		if (to.compareTo(from) == 0) {
			Date[] ret = new Date[1];
			ret[0] = from;
			return ret;
		}
		else if (to.compareTo(from) < 0) {
			calFm.setTime(to);
			calTo.setTime(from);
		}
		else {
			calFm.setTime(from);
			calTo.setTime(to);
		}
		List dates = new ArrayList();
		for (Calendar pt = calFm; !pt.after(calTo); pt.add(field, step)) {
			dates.add(pt.getTime().clone());
		}
		int size = dates.size();
		Date[] ret = new Date[size];
		for (int i = 0; i < size; i++) {
			ret[i] = (Date) dates.get(i);
		}
		return ret;
	}

	/**
	 * 将起始日期/时间加上响应的毫秒, 获得一个新的时间
	 * @param dateFm 其实日期/时间
	 * @param ms 需要加上的毫秒数
	 * @return
	 */
	public static Date addTime(Date dateFm, long ms) {
		long msFm = dateFm.getTime();
		return new Date(msFm + ms);
	}
	 public static Date addMonth(Date date, int n)
	  {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(2, n);
	    return cal.getTime();
	  }

	  public static Date addDay(Date date, int n)
	  {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(5, n);
	    return cal.getTime();
	  }

	  public static Date addHour(Date date, int n)
	  {
	    return new Date(date.getTime() + 3600000 * n);
	  }
	/**匹配 ＋0800， －0700 这样的时区表达的正则表达式*/
	private static Pattern END_WITH_TIMEZONE = Pattern.compile(".*\\s+([\\+|-][0-1]\\d00)$");
	
	private static String[] DATE_FORMATS = new String[]{
		"yyyy-MM-dd","MM-dd-yyyy"};
	private static String[] DATETIME_FORMATS = new String[]{
		"M/d/yyyy H:m:s","yyyy/M/d H:m:s.S", "yyyy/M/d H:m:s", "yyyy/M/d H:m"
	};
	/**所有认为标准的日期格式*/private static String[] STD_DATE_FORMATS = new String[]{
		"yyyy/M/d H:m:s.S Z", "yyyy/M/d H:m:s Z", "yyyy/M/d H:m Z", "yyyy/M/d Z"
	};
	
	/**所有认为标准的时间格式*/
	private static String[] STD_TIME_FORMATS = new String[]{
		"H:m:s.S Z", "H:m:s Z", "H:m Z"
	};
	/**
	 * 将标准的日期/时间字符串自动转换为日期(带时区，没有则默认当前时区)<br>
	 * 目前只处理 8 种格式 :<br>
	 * yyyy/MM/dd HH:mm:ss.SSS Z, yyyy/M/d H:m:s.S Z<br>
	 * yyyy/MM/dd HH:mm:ss Z, yyyy/M/d H:m:s Z<br>
	 * yyyy/MM/dd HH:mm Z, yyyy/M/d H:m Z<br>
	 * yyyy/MM/dd Z, yyyy/M/d Z <br>
	 * 其中日期的分隔符除了可以是'/'之外, 还可以是'-', 但时间分隔符只能是':'! <br>
	 * 注意此函数是建立在这样一个事实的基础上:<br>
	 * 1)yyyy/M/d H:m:s'Z'Z的模式也可以用于解析yyyy/MM/dd HH:mm:ss'Z'Z模式;<br>
	 * 2)实际输入字符串中各部分数字前的空格并不影响解析;(例如:" 2002/ 4/ 06 1: 05 +0000")<br>
	 * 
	 * @param dateString 日期时间字符串
	 * @return 解析得到的日期时间对象, 如果解析失败, 返回 null
	 */
	public static Date stdString2Date(String dateString) {
		String sDate = dateString;
		if (null==sDate) return null;
		
		// 处理头尾空格
		sDate = sDate.trim();

		String tz = splitTimeZone(sDate);
		String tm = sDate;
		if (null==tz){
			tz = getCurrTimeZone();
		}else{
			tm = sDate.substring(0, sDate.length() - tz.length());
		}
		// 将'-','.'分隔符替换为'/'分隔符
		tm = StringUtil.replace(tm, "-", "/");
		sDate = tm.trim() + " " +tz;
		
		// 通过尝试获得合适的返回值
		for (int i=0; i<STD_DATE_FORMATS.length; i++){
			Date date = _string2Date(sDate, STD_DATE_FORMATS[i], tz);
			if (date != null) {
				return (date);
			}
		}		
		// 如果不能解析 ...
		return null;
	}
	/**
	 * 将时间字符串解析成日期时间对象(带时区，没有则默认当前时区)
	 * @param timeString 时间字符串
	 * @return 解析得到的日期时间对象, 如果解析失败, 返回 null
	 */
	public static Date stdString2Time(String timeString) {
		String sTime = timeString;
		if (null==sTime) return null;
		
		// 处理头尾空格
		sTime = sTime.trim();
		
		String tz = splitTimeZone(sTime);
		String tm = sTime;
		if (null==tz){
			tz = getCurrTimeZone();
		}else{
			tm = sTime.substring(0, sTime.length() - tz.length());
		}
		sTime = tm + " " +tz;
		
		// 通过尝试获得合适的返回值
		for (int i=0; i<STD_TIME_FORMATS.length; i++){
			Date time = _string2Date(sTime, STD_TIME_FORMATS[i], tz);
			if (time != null) {
				return (time);
			}
		}		
		// 如果不能解析 ...
		return null;
	}
	private static String splitTimeZone(String s){
		Matcher m = END_WITH_TIMEZONE.matcher(s);
		if (m.matches()){
			return m.group(1);
		}else{
			return null;
		}
	}
	private static String getCurrTimeZone() {
		int offset = TimeZone.getDefault().getRawOffset();
		String wOrE = "";
		if (offset > 0)
			wOrE = "+";
		else
			wOrE = "-";
		String timeZoneCode = String.valueOf(offset / (60 * 60 * 1000));
		if (timeZoneCode.length() < 2) {
			timeZoneCode = wOrE + "0" + timeZoneCode + "00";
		} else {
			timeZoneCode = wOrE + timeZoneCode + "00";
		}
		return timeZoneCode;
	}
	private static Date _string2Date(String sDate, String fmt, String timeZone) {
		Date date;
		SimpleDateFormat ofmt = new SimpleDateFormat(fmt);
		try {
			date = ofmt.parse(sDate);
			
			return date;	//FIXME: 在目前的 JDK 中, 无论使用什么方法, 返回的 Date 对象一定是当前时区的
		} catch (ParseException ex) {
			date = null;
		}
		return (date);
	}

}
