package com.bluenetworks.webapp.app.payment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.bluenetworks.webapp.app.payment.controller.AppPaymentController;
import com.bluenetworks.webapp.app.payment.mapper.AppPaymentMapper;
import com.bluenetworks.webapp.common.SmartroCardBill;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppPaymentService {

	private final AppPaymentMapper appPaymentMapper;
	
	private final SmartroCardBill smartroCardBill;
	
	public int billTokenUpdate(Map<String, Object> paramMap) throws Exception {

		int cnt = appPaymentMapper.updateBillTokenMain(paramMap);

		JSONObject obj = appPaymentMapper.selectPaymentOne(paramMap);
		paramMap.put("mainYn", "Y");
		if( obj == null ) {
			cnt = appPaymentMapper.insertBillToken(paramMap);
		} else {
			appPaymentMapper.updateBillTokenMainOne(paramMap);
			
		}
		
        return cnt;
    }
	
	public int deleteBillkeyReq(Map<String, Object> paramMap) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		paramMap.put("BillTokenKey", paramMap.get("billTokenKey"));
		
		result = smartroCardBill.smartroDeleteardBillKey(paramMap);
		
		int cnt = appPaymentMapper.deleteBillkeyReq(paramMap);
		
		return cnt;
	}
	
	public List<JSONObject> selectPaymentList(Map<String, Object> param) throws Exception {
		
		return appPaymentMapper.selectPaymentList(param);
	}
	
	public int changeMainCard(Map<String, Object> param) throws Exception {
		
		//기존 카드 mainYn N 처리 후 선택한 카드 Y 처리
		appPaymentMapper.updateBillTokenMain(param);
		param.put("mainYn", "Y");
		log.info("param :: "+param);
		return appPaymentMapper.updateBillTokenMainOne(param);
	}

	public String selectIdToken(Map<String, Object> param) throws Exception {

		return appPaymentMapper.selectIdToken(param);
	}

	public List<Map<String, Object>> selectPaymentHistoryList(Map<String, Object> param) throws Exception {

		return appPaymentMapper.selectPaymentHistoryList(param);
	}

	public int selectPaymentHistoryCount(Map<String, Object> param) throws Exception {

		return appPaymentMapper.selectPaymentHistoryCount(param);
	}
}
