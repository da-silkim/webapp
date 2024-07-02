package com.bluenetworks.webapp.common;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommonUtil {

	public JSONObject result(String message, int code, String info) {
		JSONObject result = new JSONObject();
		result.put("message", message);
		result.put("code", code);
		result.put("info", info);
		result.put("content", info);
		return result;
	}

	public JSONObject result(String message, int code, String info, String content) {
		JSONObject result = new JSONObject();
		result.put("message", message);
		result.put("code", code);
		result.put("info", info);
		result.put("content", content);
		return result;
	}

	public boolean is_error(JSONObject target) {

		boolean is_error = true;

		if(target != null) {
			JSONObject result = (JSONObject) target.get("result");
			if(result != null){
				String message = (String) result.get("message");
				if(message != null && message.equals("success")){
					is_error = false;
				}
			}
		}

		return is_error;
	}

	public boolean check_pattern(String type, String text) {

		boolean is_ok = true;

		if(type.equals("id")){
			//시작은 영문으로만, '_'를 제외한 특수문자 안되며 영문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하
			String pattern = "^[a-zA-Z]{1}[a-zA-Z0-9_]{5,12}$";
			Matcher matcher = Pattern.compile(pattern).matcher(text);
			if(!matcher.matches()){
				is_ok = false;
			}
		}
		else if(type.equals("password")){
			//1. 영문, 숫자, 특수문자 조합
			//2. 4~10자리 사이 문자
			String pattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$";
			Matcher matcher = Pattern.compile(pattern).matcher(text);
			if(!matcher.matches()){
				is_ok = false;
			}
		}
		else if(type.equals("email")){
			String pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Matcher matcher = Pattern.compile(pattern).matcher(text);
			if(!matcher.matches()){
				is_ok = false;
			}
		}
		else if(type.equals("phone")){
			String pattern = "^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}$";
			Matcher matcher = Pattern.compile(pattern).matcher(text);
			if(!matcher.matches()){
				is_ok = false;
			}
		}
		else if(type.equals("platenumber")) {
			//차량번호 검증정규식
	        String pattern1 = "^[가-힣]{2}\\d{2}[가-힣]{1}\\d{4}$"; // 서울12가1234
	        String pattern2 = "^\\d{3}[가-힣]{1}\\d{4}$"; // 123조1234
	        String pattern3 = "^\\d{2}[가-힣]{1}\\d{4}$"; // 12조1234
	        boolean regex = Pattern.matches(pattern1, text) | Pattern.matches(pattern2, text) | Pattern.matches(pattern3, text);
	        if(!(regex==true)) {
	        	is_ok = false;
	        }
		}

		return is_ok;
	}
	
	
	

	public String encryption_sha256(String origin) {

		String sha256_password = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
//			digest.reset();
//			digest.update(salt);
			byte[] hash = digest.digest(origin.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			sha256_password = hexString.toString();
		}
		catch (Exception e){
			System.err.println("Exception 예외 발생");
			sha256_password = "";
		}

		return sha256_password;

	}
	public String substrTelByBar(String tel) {
		if (tel.length() == 8) {
			tel = tel.substring(0, 4) + "-" + tel.substring(4, 8);
		} else if (tel.length() == 9) {
			tel = tel.substring(0, 2) + "-" + tel.substring(2, 5) + "-" + tel.substring(5, 9);
		} else if (tel.length() == 10) {
			if (tel.substring(0, 2).equals("02")) {
				tel = tel.substring(0, 2) + "-" + tel.substring(2, 6) + "-" + tel.substring(6, 10);
			} else {
				tel = tel.substring(0, 3) + "-" + tel.substring(3, 6) + "-" + tel.substring(6, 10);
			}
		} else if (tel.length() == 11) {
			tel = tel.substring(0, 3) + "-" + tel.substring(3, 7) + "-" + tel.substring(7, 11);
		}
		else if (tel.length() == 12) {
			tel = tel.substring(0, 4) + "-" + tel.substring(4, 8) + "-" + tel.substring(8, 12);
		}
		return tel;
	}

	public String getNowDate() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		String strNow = format.format(now);

		return strNow;
	}

	public String getNowDateFirst() {
		Date now = new Date();
		now.setDate(1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		String strNow = format.format(now);

		return strNow;
	}

	public String formatCardNo( String cardno ) {

		String result = cardno ;

		if(cardno.length() == 16){
			result = cardno.substring(0,4) + "-" + cardno.substring(4,8) + "-" + cardno.substring(8,12) + "-" + cardno.substring(12,16);
		}

		return result;
	}

	public static String removeLeadingZeroPrefix(String value){
		return value.replaceFirst("^0+(?!$)", "");
	}

	/**
	 * [ Common ] Page List Page Information Return !!
	 * @param paramMap
	 * @param listCount
	 * @return Map<String, String>
	 */
	public static Map<String, Object> getPageInfo(Map<String, Object> paramMap, String listCount) {
		Map<String, Object> map  = new HashMap<>();
		int currPageNo = paramMap.get("currPageNo") == null ? 1  : Integer.parseInt(paramMap.get("currPageNo").toString());
		int pageCount  = paramMap.get("pageCount")  == null ? 10 : Integer.parseInt(paramMap.get("pageCount").toString());
		int blockCount = paramMap.get("blockCount") == null ? 5 : Integer.parseInt(paramMap.get("blockCount").toString());

		int totalPage = (int) Math.ceil( Double.parseDouble(listCount) / pageCount);

		map.put("totalCount", listCount);
		map.put("currPageNo", ""+currPageNo);
		map.put("pageCount",  ""+pageCount);
		map.put("blockCount", ""+blockCount);
		map.put("totalPage",  ""+totalPage);

		return map;
	}

	// 현재 한국 날짜 yyyymmdd 만들기
	public static String getCurrentKoreanDate() {
		// 현재 날짜를 가져오기
		LocalDate currentDate = LocalDate.now();

		// 날짜 포맷 지정 (yyyymmdd 형식)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		// 포맷 적용
		String formattedDate = currentDate.format(formatter);

		return formattedDate;
	}
}
