package com.bluenetworks.webapp.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SmartroPayUtil {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	/**
	 * 대외 통신 샘플.
	 *  외부 기관과 URL 통신하는 샘플 함수 입니다.
	 *  샘플소스는 서비스 안정성을 보장하지 않으므로, 가맹점 환경에 맞게 구현 바랍니다.
	 *  샘플소스 이용에 따른 이슈 발생시 NICEPAY에서 책임지지 않습니다. 
	*/
	public static String connectToServer(String data, String reqUrl) throws Exception{
		HttpURLConnection conn 		= null;
		BufferedReader resultReader = null;
		PrintWriter pw 				= null;
		URL url 					= null;
		
		int statusCode = 0;
		StringBuilder recvBuffer = null;
		try{
			SSLContext sslCtx = SSLContext.getInstance("TLSv1.2");
			sslCtx.init(null, null, new SecureRandom());
			
			url = new URL(reqUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.addRequestProperty("Content-Type", "application/json");
			conn.addRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(25000);
			conn.setDoOutput(true);
			
			OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(conn.getOutputStream()) , "utf-8" );
			char[] bytes = data.toCharArray();
			osw.write(bytes,0,bytes.length);
			osw.flush();
		    osw.close();
			    
			statusCode = conn.getResponseCode();
			resultReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			recvBuffer = new StringBuilder();
			
			while ((line = resultReader.readLine()) != null) {
		        System.out.println(" response " +  line);
		        recvBuffer.append(line);
		    }
			
			return recvBuffer.toString().trim();
		}catch (Exception e){
			return "ERROR";
		}finally{
			recvBuffer.setLength(0);
			
			try{
				if(resultReader != null){
					resultReader.close();
				}
			}catch(Exception ex){
				resultReader = null;
			}
			
			try{
				if(pw != null) {
					pw.close();
				}
			}catch(Exception ex){
				pw = null;
			}
			
			try{
				if(conn != null) {
					conn.disconnect();
				}
			}catch(Exception ex){
				conn = null;
			}
		}
	}
	
	// SHA-256 형식으로 암호화
	public class DataEncrypt{
		MessageDigest md;
		String strSRCData = "";
		String strENCData = "";
		String strOUTData = "";
		
		public DataEncrypt(){ }
		public String encrypt(String strData){
			String passACL = null;
			MessageDigest md = null;
			try{
				md = MessageDigest.getInstance("SHA-256");
				md.reset();
				md.update(strData.getBytes());
				byte[] raw = md.digest();
				passACL = encodeHex(raw);
			}catch(Exception e){
				logger.error("암호화 에러 : "+ e.toString());
			}
			return passACL;
		}
		
		public String encodeHex(byte [] b){
			char [] c = Hex.encodeHex(b);
			return new String(c);
		}
	}
	
	/* SHA256 암호화 */
	public static final String encodeSHA256Base64(String strPW) {
	    String passACL = null;
	    MessageDigest md = null;

	    try {
	        md = MessageDigest.getInstance("SHA-256");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    md.update(strPW.getBytes());
	    byte[] raw = md.digest();
	    byte[] encodedBytes = Base64.encodeBase64(raw);
	    passACL = new String(encodedBytes);

	    return passACL;
	}
	
	
	/* 현재일자 */
	public static final String getyyyyMMddHHmmss() {
	    SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	    return yyyyMMddHHmmss.format(new Date());
	}

	/* 현재일자  */
	public static final String getyyyyMMddHHmm() {
	    SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyyMMddHHmm");
	    return yyyyMMddHHmm.format(new Date());
	}
	
}
