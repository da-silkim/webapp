package com.bluenetworks.webapp.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SmartroCardBill {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Value("${blue.smartro.MerchantKey}")
	private String MerchantKey;
	
	@Value("${blue.smartro.Mid}")
	private String Mid; 		//"t_2309082m";           // 발급받은 테스트 Mid 설정(Real 전환 시 운영 Mid 설정)

	private String Moid = "";           // 상품주문번호
	
	@Value("${blue.smartro.SspMallId}")
	private String SspMallId;
	
	@Value("${blue.smartro.targetURL}")
	private String TargetURL;
	
	@Value("${blue.smartro.targetURL.getBill}")
	private String TargetURLGetBill;
	
	@Value("${blue.smartro.targetURL.dellBill}")
	private String TargetURLDellBill;
	
	/**
	 * 결제카드 빌키 발급 프로세서
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> smartroPayCardBillKey(Map param) throws Exception{
		
		logger.info("smartroPayCardBillKey start ====================================");
		
		JSONObject body = new JSONObject();
		JSONObject snd = new JSONObject();
		String MallUserId 		= StringUtils.toNotNullString(param.get("payUserIdPop")); 		// 카드번호
		
		String EdiDate = getyyyyMMddHHmmss();
		// 요청 파라미터 (각 값들은 가맹점 환경에 맞추어 설정해 주세요.)
		body.put("PayMethod", "CARDBILLKEY");		// CARDBILLKEY: 신용카드
		body.put("Moid", Moid);	//주문번호
		body.put("Mid", Mid);
		body.put("SspMallId", SspMallId);		// 결제MALL ID
		body.put("MallUserId", MallUserId);		// 회원사 고객 ID
		body.put("EdiDate", EdiDate);	// 결제요청일시
		body.put("SecureType", "S2"); 				// 위변조검증값 S2(기본값)
		body.put("EncodingType", "utf8"); 			// 인코딩타입 utf8(기본값), euckr
		body.put("MallReserved", ""); 				// 상점예약필드
		
		//카드 정보	
		String CardNo 		= StringUtils.toNotNullString(param.get("payCardNumberPop")); 		// 카드번호
		String ExpYear 		= StringUtils.toNotNullString(param.get("payCreditYearPop")); 		// 유효기간(년)
		if( ExpYear != null && ExpYear.length() == 4 ){
			ExpYear = ExpYear.substring(2, 4);
		}
		String ExpMonth 	= StringUtils.toNotNullString(param.get("payCreditMonthPop")); 		// 유효기간(월)
		String IDNo 		= StringUtils.toNotNullString(param.get("paybirthDayPop")); 			// 생년월일/사업자번호
		String CardPw 		= StringUtils.toNotNullString(param.get("payPassTwoPop")); 			// 카드비밀번호
		String BuyerName 	= StringUtils.toNotNullString(param.get("customerNameBillPop")); 	// 구매자
		String EdiType 		= StringUtils.toNotNullString(param.get("EdiType")); 		// 응답전문 유형
		body.put("CardType" ,"0"); // 개인 - 0 , 법인 - 1
		body.put("CardNum" , CardNo); // 카드 번호
		body.put("CardExpire" , ExpYear+ExpMonth); // 카드 유효기간(년/월)
		body.put("BuyerAuthNum" , IDNo); //개인 0 (생년월일) , 법인 1 (사업자번호)
		body.put("CardPwd" , CardPw);  // 앞자리 2자리
		
		//EdiDate + Mid + Moid + "SMARTRO!@#"
		String serverEncryptData = EdiDate + Mid + Moid + "SMARTRO!@#";
		String verifyValue 		= encodeSHA256Base64(serverEncryptData);
		body.put("VerifyValue", verifyValue);
		logger.info("smartroPayCardBillKey body : {}", body);
		
		// json 데이터 AES256 암호화
		try {
		    snd.put("EncData", AES256Cipher.AES_Encode(body.toString(), MerchantKey.substring(0, 32)));
		    snd.put("Mid", Mid);
		    
		} catch(Exception e){
		    e.printStackTrace();
		}
		
		String resultJsonStr = connectToServer(snd.toString(), TargetURL + TargetURLGetBill);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result = new ObjectMapper().readValue(resultJsonStr.toString(), HashMap.class);
		logger.info("smartroPayCardBillKey result : {}", result);
		
		String BillTokenKey = String.valueOf(result.get("BillTokenKey"));
	    BillTokenKey = AES256Cipher.AES_Decode(BillTokenKey, MerchantKey.substring(0,32));   // AESDecode 후 가맹점DB에 빌링키 저장
	    logger.info("smartroPayCardBillKey BillTokenKey : {}", BillTokenKey);
	    result.put("BillTokenKey", BillTokenKey);
	    
	    logger.info("smartroPayCardBillKey end ====================================");
		
		
		return result;
	}
	
	/**
	 * 결제카드 빌키 삭제 프로세서
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> smartroDeleteardBillKey(Map param) throws Exception{
		
		logger.info("smartroDeleteardBillKey start ====================================");
		
		JSONObject body 	= new JSONObject();
		JSONObject snd 		= new JSONObject();
		String EdiDate 		= getyyyyMMddHHmmss();
		String BillTokenKey = StringUtils.toNotNullString(param.get("BillTokenKey"));
		String MallUserId 	= StringUtils.toNotNullString(param.get("userIdx")); 		// 카드번호
		String VerifyValue 	= encodeSHA256Base64(Mid +SspMallId+ BillTokenKey);
		
		// 요청 파라미터 (각 값들은 가맹점 환경에 맞추어 설정해 주세요.)
		body.put("ServiceType" ,"CD");		// CARDBILLKEY: 신용카드
		body.put("Mid" , Mid);
		body.put("BillTokenKey" , BillTokenKey);
		body.put("SspMallId" , SspMallId);		// 결제MALL ID
		body.put("MallUserId" , MallUserId);		// 회원사 고객 ID	
		body.put("EdiDate" , EdiDate);	// 결제요청일시
		body.put("VerifyValue" , VerifyValue); 				// 위변조검증값 S2(기본값)
		
		logger.info("smartroDeleteardBillKey body : {}", body);
		// json 데이터 AES256 암호화
		try {
			snd.put("EncData", AES256Cipher.AES_Encode(body.toString(), MerchantKey.substring(0, 32)));
			snd.put("Mid", Mid);
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		String resultJsonStr = connectToServer(snd.toString(), TargetURL + TargetURLDellBill);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result = new ObjectMapper().readValue(resultJsonStr.toString(), HashMap.class);
		logger.info("smartroDeleteardBillKey result : {}", result);
		
		logger.info("smartroDeleteardBillKey end ====================================");
		
		return result;
	}
	
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
