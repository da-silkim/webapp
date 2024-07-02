package com.bluenetworks.webapp.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
       
public class PayUtil {
	
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 현재날짜를 YYYYMMDDHHMMSS로 리턴
     */
    public final synchronized static String getyyyyMMddHHmmss(){
    	/** yyyyMMddHHmmss Date Format */
    	SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    	
    	return yyyyMMddHHmmss.format(new Date());
    }
    /**
     * <pre>
     * MD5+Base64
     * </pre>
     * @param pw
     * @return String
     */
    public final String encodeMD5Base64(String str){
    	return new String(Base64.encodeBase64(DigestUtils.md5(str)));
    }
     
    public final static String encodeMD5HexBase64(String pw){
    	return new String(Base64.encodeBase64(DigestUtils.md5Hex(pw).getBytes()));
    }

    /**
     * <pre>
     * MD5+Base64
     * </pre>
     * @param pw
     * @return String
     */
    public final String UrlencodeBase64(String str){
    	return new String(Base64.encodeBase64(str.getBytes()));
    }
    public final String UrldecodeBase64(String str){
    	return new String(Base64.decodeBase64(str.getBytes()));
    }

    /**
    * 기준날짜에서 몇일 전,후의 날짜를 구한다.
    * @param	sourceTS	기준날짜
    * @param	day			변경할 일수
    * @return	기준날짜에서 입력한 일수를 계산한 날짜
    */
    public static Timestamp getTimestampWithSpan(Timestamp sourceTS, long day) throws Exception {
       Timestamp targetTS = null;	
       if (sourceTS != null) 
    	   targetTS = new Timestamp(sourceTS.getTime() + (day * 1000 * 60 * 60 * 24));
       return targetTS;
    }

    /**
    * 응답메세지 파싱
    * @param plainText
    * @param delim
    * @param delim2
    * @return
    */
    public static Hashtable<String,String> parseMessage(String plainText, String delim, String delim2)
    {
        Hashtable<String,String> retData = new Hashtable<String,String>();
        ArrayList<String> tokened_array = tokenizerWithBlanks(plainText, delim);
        String temp = "";
        for (int i = 0; i < tokened_array.size(); i++) {
            temp = tokened_array.get(i);
            if (StringUtils.isNotEmpty(temp)) {
                retData.put( temp.substring(0,temp.indexOf(delim2)),temp.substring(temp.indexOf(delim2)+1).trim() );
            }
        }
        return retData;
    }


    /**
     * 응답메세지 파싱, EUC-KR 디코딩 내재
     * @param plainText
     * @param delim
     * @param delim2
     * @return
     */
    public static Hashtable<String,String> parseMessageForEucKr(String plainText, String delim, String delim2)
    {
        Hashtable<String,String> retData = new Hashtable<String,String>();
        ArrayList<String> tokened_array = tokenizerWithBlanks(plainText, delim);
        String temp = "";
        for (int i = 0; i < tokened_array.size(); i++) {
            temp = tokened_array.get(i);
            if (StringUtils.isNotEmpty(temp)) {
                try {
                    retData.put( temp.substring(0,temp.indexOf(delim2)), URLDecoder.decode(temp.substring(temp.indexOf(delim2)+1).trim(), "EUC-KR"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return retData;
    }

    public static String urlEncodeEuckr(String str)
    {
    	try {
    		str =  URLEncoder.encode(str, "euc-kr");
    	} catch (UnsupportedEncodingException e) {
    		e.printStackTrace();
    	}
    	return str;
    }

    public String urlDecodeEuckr(String str)
    {
    	try {
    		str =  URLDecoder.decode(str, "euc-kr");
    	} catch (UnsupportedEncodingException e) {
    		e.printStackTrace();
    	}
    	return str;
    }

    /**
     * 스트링 null 체크
     * @param o
     * @return
     */
    public String nullCheck(Object o)
    {
       String temp = "";
       
       if (o != null)
       {
       	   temp = (String) o;
       	   
       	   try
       	   {
       	       temp = (temp.length()>0 && "null".equals(temp.trim().toLowerCase()) )? "":temp;
           }catch(NullPointerException exx) {
        	   exx.printStackTrace();
       	   }catch(Exception exx){
       		   exx.printStackTrace();
       	   }
       	   
       }
       
       return temp;
    }

    /**
    * Collenction 객체 Map 데이터를 NM=VL& 형식의 문자열로 만든다.
    * @param data
    * @param charset
    * @return
    */
    public static String appendStr(HashMap<String, String> data, String charset)
    {
       StringBuffer sb = new StringBuffer();
       String temp = "";

       for(String key : data.keySet())
       {
    	   appendData(sb, key, data.get(key), charset);
       }
       
       return sb.toString();
    }

    /**
    * NM=VL& 형식의 문자열을 만든다.
    * @param sb
    * @param key
    * @param value
    * @param charset
    */
    private static void appendData(StringBuffer sb , String key , String value, String charset)
    {
       try{
    	   sb.append(key).append("=")
    	   .append( URLEncoder.encode( (value == null )? "" : value , charset) )
    	   .append("&");
       }catch(UnsupportedEncodingException uee){
    	   uee.printStackTrace();
       }
    }

    /**
    * 입력받은 스트링을 공백과 함께 delimiter로 자른다.
    *
    * @param input 파싱할 문자열
    * @param delimiter 구분자
    */
    static ArrayList<String> tokenizerWithBlanks(String input, String delimiter)
    {
        ArrayList<String> array = new ArrayList<String>();
        String token;
        int pos;
        int delimiterSize = delimiter.length();
        do{
            pos = input.indexOf(delimiter);
            if (pos >= 0)
            {
                token = input.substring(0, pos);
                input = input.substring(pos + delimiterSize);
            }
            else
            {
                token = input;
                input = "";
            }
            array.add(token);
        } while (pos >= 0);
        return array;
    }

    /**
     * Post Method 방식으로 통신
     * 
     * @param request		요청할 데이터
     * @param actionURL		요청할 URL
     * @return
     * @throws Exception
     */

     public static String sendByPost(HashMap<String,String> requestMap, String requestURL) throws Exception
    {
    	try 
    	{
    		StringBuffer buffer = new StringBuffer();
    		Iterator<String> keys = requestMap.keySet().iterator();
    		int size = requestMap.size();

    		buffer.append("?");
    		while (keys.hasNext()) {
    			String key = keys.next();
    			buffer.append(key).append("=").append(urlEncodeEuckr(requestMap.get(key)));
    			if (true == keys.hasNext()) {
    				buffer.append("&"); 
    			}
    		}
    		
    		System.out.println("requestURL [ " + requestURL + buffer.toString() + " ]");
    		
    		URL url = new URL(requestURL + buffer.toString());

    		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    		connection.setRequestMethod("POST");
    		connection.setDoOutput(true);
    		connection.setRequestProperty("Content-Type", "application/json");
    		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream(), "euc-kr"); // 서버는  "EUC-KR" 입니다 변경하지 마세요..
    		//wr.write(buffer.toString());
    		wr.flush();

    		BufferedReader rd = new BufferedReader(new InputStreamReader( connection.getInputStream()));  // 서버는  "EUC-KR" 입니다 필요시 UTF-8 형으로 변경하세요.
    		String result = "";
    		String line;
    		while ((line = rd.readLine()) != null)
    		{
    			result += line;
    		}
    		wr.close();
    		rd.close();

    		return result;
    	} 
    	catch (IOException e) 
    	{
    		System.out.println(" UrlCall IOException :"+e.toString());
    		return "1";
    	}
		catch (RuntimeException e) 
		{
			System.out.println(" UrlCall RuntimeException :"+e.toString());
			return "1";
    	} 
    	catch (Exception e) 
    	{
    		System.out.println(" UrlCall Exception :"+e.toString());
    		return "1";
    	}
    }

    /**
    * AES256 암호화
    * @param str
    * @param key
    * @return
    * @throws java.io.UnsupportedEncodingException
    * @throws NoSuchAlgorithmException
    * @throws NoSuchPaddingException
    * @throws InvalidKeyException
    * @throws InvalidAlgorithmParameterException
    * @throws IllegalBlockSizeException
    * @throws BadPaddingException
    */
    public static String AESEncode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,	IllegalBlockSizeException, BadPaddingException 
    {
    	byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    	byte[] textBytes = str.getBytes("UTF-8");
    	AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    	SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
    	Cipher cipher = null;
    	cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
    	//return Base64.encodeBase64String(cipher.doFinal(textBytes));//commons-codec-1.6.jar
    	return new String(Base64.encodeBase64(cipher.doFinal(textBytes)));//commons-codec-1.3.jar
    }

    /**
    * AES256 복호화
    * @param str
    * @param key
    * @return
    * @throws java.io.UnsupportedEncodingException
    * @throws NoSuchAlgorithmException
    * @throws NoSuchPaddingException
    * @throws InvalidKeyException
    * @throws InvalidAlgorithmParameterException
    * @throws IllegalBlockSizeException
    * @throws BadPaddingException
    */
    public static String AESDecode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException 
    {
    	byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    	byte[] textBytes = Base64.decodeBase64(str.getBytes());//commons-codec-1.6.jar
    	AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    	SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
    	return new String(cipher.doFinal(textBytes), "UTF-8");
    }
    
}