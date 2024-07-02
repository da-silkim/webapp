package com.bluenetworks.webapp.app.payment.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.json.simple.JSONObject;

@Mapper
public interface AppPaymentMapper {

	int updateBillTokenMain( Map<String, Object> resMap );
	
	int deleteBillkeyReq( Map<String, Object> resMap );
	
	int updateBillTokenMainOne( Map<String, Object> resMap );
	
	int insertBillToken( Map<String, Object> resMap );
	
	JSONObject selectPaymentOne(Map<String, Object> resMap);
	
	List<JSONObject> selectPaymentList(Map<String, Object> resMap);
	String selectIdToken(Map<String, Object> resMap);
	List<Map<String, Object>> selectPaymentHistoryList(Map<String, Object> resMap);
	int selectPaymentHistoryCount( Map<String, Object> resMap );

}
