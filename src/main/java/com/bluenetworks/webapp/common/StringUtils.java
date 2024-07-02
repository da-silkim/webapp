package com.bluenetworks.webapp.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class StringUtils {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * [ Common ] Object Null Check !!
     * @param obj
     * @return boolean
     */
    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }

        if ((obj instanceof String) && (((String) obj).trim().length() == 0)) {
            return true;
        }

        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }

        if (obj instanceof List) {
            return ((List<?>) obj).isEmpty();
        }

        if (obj instanceof Object[]) {
            return (((Object[]) obj).length == 0);
        }

        return false;
    }

    /**
     * [ Common ] Null 일 경우 빈 문자열로 변경하거나 원하는 문자열로 변경해줌 !!
     * @param str
     * @param chgStr
     * @return String
     */
    public static String getNullToString(String str) {
        return getNullToString(str, "");
    }

    public static String getNullToString(String str, String chgStr) {
        return isNull(str) ? chgStr : str;
    }

    /**
     * [ Common ] 숫자에 천단위마다 콤마 넣기 !!
     * @param num
     * @return String
     */
    public static String toNumFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");

        return df.format(num);
    }


    /**
     * IN Query 에 적용할 String
     * @param param
     * @return String
     */
    public static String setInParameter(String param) {
        return setInParameter(param, ",");
    }

    /**
     * IN Query 에 적용할 String
     * @param param
     * @param sep
     * @return String
     */
    public static String setInParameter(String param, String sep) {
        String resParam = "";

        if(param == null || "".equals(param)){
            return "''";
        }

        resParam = "'" + param.replaceAll(sep, "','") + "'";

        return resParam;
    }


    /**
     * [ Common ] RequestContextHolder 를 통하여 HttpServletRequest 를 return
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * [ Common ] request full URL
     * @return String
     * @throws Exception
     */
    public static String getRequestURL() throws Exception {
        return getRequestURL(getHttpServletRequest());
    }

    public static String getRequestURL(HttpServletRequest request) throws Exception {
        return request.getRequestURL().toString();
    }

    /**
     * [ Common ] request URI
     * @return String
     * @throws Exception
     */
    public static String getRequestURI() throws Exception {
        return getRequestURI(getHttpServletRequest());
    }

    public static String getRequestURI(HttpServletRequest request) throws Exception {
        return request.getRequestURI().toString();
    }

    /**
     * [ Common ] Server Port
     * @return int
     * @throws Exception
     */
    public static int getServerPort() throws Exception {
        return getServerPort(getHttpServletRequest());
    }

    public static int getServerPort(HttpServletRequest request) throws Exception {
        return request.getServerPort();
    }

    /**
     * [ Common ] Server Name
     * @return String
     * @throws Exception
     */
    public static String getServerName() throws Exception {
        return getServerName(getHttpServletRequest());
    }

    public static String getServerName(HttpServletRequest request) throws Exception {
        return request.getServerName().toString();
    }

    /**
     * [ Common ] Server IP
     * @return String
     * @throws Exception
     */
    public static String getServerIp() throws Exception {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * [ Common ] Get Client IP
     * @return
     */
    public static String getClientIP() {
        HttpServletRequest request = getHttpServletRequest();

        String clientIP = request.getHeader("X-FORWARDED-FOR");

        if (clientIP == null || clientIP.length() == 0) {
            clientIP = request.getHeader("Proxy-Client-IP");
        }

        if (clientIP == null || clientIP.length() == 0) {
            clientIP = request.getHeader("WL-Proxy-Client-IP");  // 웹로직
        }

        if (clientIP == null || clientIP.length() == 0) {
            clientIP = request.getRemoteAddr();
        }

        return clientIP;
    }


    /**
     * [ Common ] Template Html 읽기
     * @param urlToRead
     * @return String
     * @throws Exception
     */
    public static String getStringHTML(String urlToRead) throws Exception {
        URL url;                // The URL to read
        HttpURLConnection conn; // The actual connection to the web page
        BufferedReader rd;      // Used to read results from the web page
        String line;            // An individual line of the web page HTML
        String result = "";     // A long string containing all the HTML

        try {
           url = new URL(urlToRead);

           conn = (HttpURLConnection) url.openConnection();

           conn.setRequestMethod("GET");

           rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

           while ((line = rd.readLine()) != null) {
              result += line;
           }

           rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
           e.printStackTrace();
        }

        return result;
    }

    /**
     * object null,구분
     * @param obj
     * @return String
     */
    public static String normalizeNull(Object o)
    {
        return normalizeNull(String.valueOf(o));
    }


    /**
     * String null,구분
     * @param String
     * @return String
     */
    public static String normalizeNull(String s)
    {
        if(s == null)
            return "";
        if(equals(s, "null"))
            return "";
        if(equals(s, "undefined"))
            return "";
        else
            return s;
    }

    /**
     * param1 에 들어온 값과 param2 비교
     * @param1 String
     * @param2 String
     * @return boolean
     */
    public static boolean equals(String s, String s1)
    {
        if(s == null && s1 == null)
            return true;
        if(s == null || s1 == null)
            return false;
        else
            return s.equals(s1);
    }

    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    public static String removeTag(String html) {
        return html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
    }
    
    public static String getLocalServerIp()
	{
		try
		{
		    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
		    {
		        NetworkInterface intf = en.nextElement();
		        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
		        {
		            InetAddress inetAddress = enumIpAddr.nextElement();
		            if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
		            {
		            	return inetAddress.getHostAddress().toString();
		            }
		        }
		    }
		}
		catch (SocketException ex) {}
		return "";
	}	

	public static String cleanXSS(String value) {
		if (value != null && !value.equals("")) {
			value = value.replaceAll("<", "& lt;");
			value = value.replaceAll(">", "& gt;");
			value = value.replaceAll("\\(", "& #40;");
			value = value.replaceAll("\\)", "& #41;");
			value = value.replaceAll("'", "& #39;");
			value = value.replaceAll("eval\\((.*)\\)", "");
			value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
			value = value.replaceAll("script", "");
		}

		return value;
	}

	 public static String toNotNullString(Object obj) {
	        if (obj == null) {
	            return "";
	        }
	        return obj.toString();
	    }
	 
}