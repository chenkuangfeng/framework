package com.ubsoft.framework.core.support.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;


public class StringUtil {
	public static final String EMPTY_STRING = "";

	public static final char DOT = '.';

	public static final char UNDERSCORE = '_';

	public static final String COMMA_SPACE = ", ";

	public static final String COMMA = ",";

	public static final String OPEN_PAREN = "(";

	public static final String CLOSE_PAREN = ")";

	public static final char SINGLE_QUOTE = '\'';

	private StringUtil() { /* static methods only - hide constructor */

	}
	/**
	 * 判断对象是否Empty(null或元素为0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 * 
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 */
	public static boolean isEmpty(Object pObj) {
		if (pObj == null)
			return true;
		if (pObj == "")
			return true;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return true;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return true;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断对象是否为NotEmpty(!null或元素>0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 * 
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 */
	public static boolean isNotEmpty(Object pObj) {
		if (pObj == null)
			return false;
		if (pObj == "")
			return false;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return false;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return false;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isTrue(String flag){
		if (flag == null||flag.length()==0)
			return false;
		if(flag.equals("1")||flag.toLowerCase().equals("true")){
			return true;
		}
		if(flag.equals("0")||flag.toLowerCase().equals("false")){
			return false;
		}
		return false;
	}
	public static String join(String seperator, String[] strings) {
		int length = strings.length;
		if (length == 0) {
			return EMPTY_STRING;
		}
		StringBuffer buf = new StringBuffer(length * strings[0].length()).append(strings[0]);
		for (int i = 1; i < length; i++) {
			buf.append(seperator).append(strings[i]);
		}
		return buf.toString();
	}

	public static String join(String seperator, Iterator objects) {
		StringBuffer buf = new StringBuffer();
		if (objects.hasNext()) {
			buf.append(objects.next());
		}
		while (objects.hasNext()) {
			buf.append(seperator).append(objects.next());
		}
		return buf.toString();
	}

    /**
     * 将两部分字符串组合起来, 考虑连接字符串不能重复的情况
     */
	public static String smartJoin (String sSplit, String str1, String str2) {
        String s1 = str1;
        String s2 = str2;
        int iLen = sSplit.length();
        if (iLen>0) {
            if (s1.length()>=iLen) {
                String sTail = s1.substring(s1.length()-iLen);
                if (sSplit.equals(sTail)) s1 = s1.substring(0,s1.length()-iLen) ;
            }
            if (s2.length()>=iLen) {
                String sHead = s2.substring(0,iLen);
                if (sSplit.equals(sHead)) s2 = s2.substring(iLen);
            }
        }
        return (s1+sSplit+s2) ;
    }

	public static String[] add(String[] x, String sep, String[] y) {

		String[] result = new String[x.length];

		for (int i = 0; i < x.length; i++) {
			result[i] = x[i] + sep + y[i];
		}

		return result;
	}

	public static String repeat(String string, int times) {

		StringBuffer buf = new StringBuffer(string.length() * times);

		for (int i = 0; i < times; i++)
			buf.append(string);

		return buf.toString();
	}

	public static String replace(String template, String placeholder, String replacement) {

		return replace(template, placeholder, replacement, false);
	}

	public static String replace(String template, String placeholder, String replacement, boolean wholeWords) {

		int loc = template.indexOf(placeholder);

		if (loc < 0) {
			return template;
		}
		final boolean actuallyReplace = !wholeWords || ((loc + placeholder.length()) == template.length())
				|| !Character.isJavaIdentifierPart(template.charAt(loc + placeholder.length()));
		String actualReplacement = actuallyReplace ? replacement : placeholder;

		return new StringBuffer(template.substring(0, loc)).append(actualReplacement).append(
				replace(template.substring(loc + placeholder.length()), placeholder, replacement, wholeWords))
				.toString();
	}

	public static String replaceOnce(String template, String placeholder, String replacement) {

		int loc = template.indexOf(placeholder);

		if (loc < 0) {
			return template;
		}
		return new StringBuffer(template.substring(0, loc)).append(replacement).append(
				template.substring(loc + placeholder.length())).toString();
	}

	public static String[] split(String seperators, String list) {

		return split(seperators, list, false);
	}

	public static String[] split(String seperators, String list, boolean include) {

		StringTokenizer tokens = new StringTokenizer(list, seperators, include);
		String[] result = new String[tokens.countTokens()];
		int i = 0;

		while (tokens.hasMoreTokens()) {
			result[i++] = tokens.nextToken();
		}

		return result;
	}

	public static String unqualify(String qualifiedName) {

		return unqualify(qualifiedName, ".");
	}

	public static String unqualify(String qualifiedName, String seperator) {

		return qualifiedName.substring(qualifiedName.lastIndexOf(seperator) + 1);
	}

	public static String qualifier(String qualifiedName) {

		int loc = qualifiedName.lastIndexOf(".");

		if (loc < 0) {
			return EMPTY_STRING;
		}
		return qualifiedName.substring(0, loc);
	}

	public static String[] suffix(String[] columns, String suffix) {

		if (suffix == null) {
			return columns;
		}

		String[] qualified = new String[columns.length];

		for (int i = 0; i < columns.length; i++) {
			qualified[i] = suffix(columns[i], suffix);
		}

		return qualified;
	}

	public static String suffix(String name, String suffix) {

		return (suffix == null) ? name : (name + suffix);
	}

	public static String[] prefix(String[] columns, String prefix) {

		if (prefix == null) {
			return columns;
		}

		String[] qualified = new String[columns.length];

		for (int i = 0; i < columns.length; i++) {
			qualified[i] = prefix + columns[i];
		}

		return qualified;
	}

	public static String root(String qualifiedName) {

		int loc = qualifiedName.indexOf(".");

		return (loc < 0) ? qualifiedName : qualifiedName.substring(0, loc);
	}

	public static boolean booleanValue(String tfString) {

		String trimmed = tfString.trim().toLowerCase();

		return trimmed.equals("true") || trimmed.equals("t");
	}

	public static String toString(Object[] array) {

		int len = array.length;

		if (len == 0) {
			return StringUtil.EMPTY_STRING;
		}

		StringBuffer buf = new StringBuffer(len * 12);

		for (int i = 0; i < (len - 1); i++) {
			buf.append(array[i]).append(StringUtil.COMMA_SPACE);
		}

		return buf.append(array[len - 1]).toString();
	}

	public static String[] multiply(String string, Iterator placeholders, Iterator replacements) {

		String[] result = new String[] { string };

		while (placeholders.hasNext()) {
			result = multiply(result, (String) placeholders.next(), (String[]) replacements.next());
		}

		return result;
	}

	private static String[] multiply(String[] strings, String placeholder, String[] replacements) {

		String[] results = new String[replacements.length * strings.length];
		int n = 0;

		for (int i = 0; i < replacements.length; i++) {
			for (int j = 0; j < strings.length; j++) {
				results[n++] = replaceOnce(strings[j], placeholder, replacements[i]);
			}
		}

		return results;
	}

	/*public static String unQuote(String name) {
	 return ( Dialect.QUOTE.indexOf( name.charAt(0) ) > -1 ) ?
	 name.substring(1, name.length()-1) :
	 name;
	 }

	 public static void unQuoteInPlace(String[] names) {
	 for ( int i=0; i<names.length; i++ ) names[i] = unQuote( names[i] );
	 }

	 public static String[] unQuote(String[] names) {
	 String[] unquoted = new String[ names.length ];
	 for ( int i=0; i<names.length; i++ ) unquoted[i] = unQuote( names[i] );
	 return unquoted;
	 }*/
	public static int count(String string, char character) {

		int n = 0;

		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == character) {
				n++;
			}
		}

		return n;
	}

	public static int countUnquoted(String string, char character) {

		if (SINGLE_QUOTE == character) {
			throw new IllegalArgumentException("Unquoted count of quotes is invalid");
		}

		// Impl note: takes advantage of the fact that an escpaed single quote
		// embedded within a quote-block can really be handled as two seperate
		// quote-blocks for the purposes of this method...
		int count = 0;
		int stringLength = (string == null) ? 0 : string.length();
		boolean inQuote = false;

		for (int indx = 0; indx < stringLength; indx++) {
			if (inQuote) {
				if (SINGLE_QUOTE == string.charAt(indx)) {
					inQuote = false;
				}
			} else if (SINGLE_QUOTE == string.charAt(indx)) {
				inQuote = true;
			} else if (string.charAt(indx) == character) {
				count++;
			}
		}

		return count;
	}

	public static boolean isNotEmpty(String string) {

		return (string != null) && (string.length() > 0);
	}

	public static String qualify(String prefix, String name) {

		char first = name.charAt(0);

		if ((first == SINGLE_QUOTE) || // a SQLstring literal
				Character.isDigit(first) // a SQL numeric literal
		) {
			return name;
		}
		return new StringBuffer(prefix.length() + name.length() + 1).append(prefix).append(DOT).append(name)
				.toString();
	}

	public static String[] qualify(String prefix, String[] names) {

		if (prefix == null) {
			return names;
		}

		int len = names.length;
		String[] qualified = new String[len];

		for (int i = 0; i < len; i++) {
			qualified[i] = qualify(prefix, names[i]);
		}

		return qualified;
	}

	public static int firstIndexOfChar(String sqlString, String string, int startindex) {

		int matchAt = -1;

		for (int i = 0; i < string.length(); i++) {
			int curMatch = sqlString.indexOf(string.charAt(i), startindex);

			if (curMatch >= 0) {
				if (matchAt == -1) { // first time we find match!
					matchAt = curMatch;
				} else {
					matchAt = Math.min(matchAt, curMatch);
				}
			}
		}

		return matchAt;
	}

	public static String truncate(String string, int length) {

		if (string.length() <= length) {
			return string;
		}
		return string.substring(0, length);
	}

    /**
     * 按照最大长度截取字符串
     * @param str 需要截取的字符串
     * @param maxLength 需要截取的字符串允许的最大长度
     * @param mask 标志字符串是否经过截取的符号, 例如 " ..." 等
     * @return 截取后的字符串, 其最大长度不会超过 maxLength
     * <br>注意:如果需要截取的字符串是 null, 则照样返回 null
     */
    static public String truncate (String str, int maxLength, String mask){
        if (null==str) return str;
        if ("".equals(str)) return str;
        if (maxLength <=0) return str;
        
        String tail = mask;
        int len = str.length();
        if (null==mask){
            if (len > maxLength) len=maxLength;
            tail="";
        }else{
            if (len > (maxLength-mask.length())) len=maxLength-mask.length();
        }
        String head = str.substring(0, len);
        if (str.length() != head.length()) head += tail;
        
        return head;
    }

	public static String toUpperCase(String str) {

		return (str == null) ? null : str.toUpperCase();
	}
	
	/**
	 * 将字符串进行HTML/XML字符转义，转义常用的&lt; &gt; &amp; &quot; &apos;
	 * @param content 要进行转义的字符串
	 * @return 转义后的字符串
	 */
	public static String escape(String content) {
		if (content!=null && content.length()>0) {
	    StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < content.length(); i++) {
				char c = content.charAt(i);
				if (c == '<')
					buffer.append("&lt;");
				else if (c == '>')
					buffer.append("&gt;");
				else if (c == '&')
					buffer.append("&amp;");
				else if (c == '\"')
					buffer.append("&quot;");
				else if (c == '\'')
					buffer.append("&apos;");
				else
					buffer.append(c);
			}
			return buffer.toString();
		}else {
			return content;
		}
	}
}
