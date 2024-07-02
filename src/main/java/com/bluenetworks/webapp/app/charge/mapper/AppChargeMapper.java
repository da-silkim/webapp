package com.bluenetworks.webapp.app.charge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.json.simple.JSONObject;

@Mapper
public interface AppChargeMapper {

	List<JSONObject> selectFavoritesList(String userId);
	
	int deleteFavoritesOne(JSONObject param);
	
	public List<Map<String, Object>> chargeList(Map<String, Object> param);
	
	public Map<String, Object> selectPkId(Map<String, Object> param);
	
	public Map<String, Object> chargerDetail(Map<String, Object> param);
	
	public Map<String, Object> selectCharging(Map<String, Object> param);
	public Map<String, Object> selectConnector(Map<String, Object> param);
	public List<Map<String, Object>> selectConnectorList(Map<String, Object> param);
	
	public String selectIdToken(Map<String, Object> param);
	//충전기제어 팝업 - 제어실행전 cp상태 체크 : 충전기와 연결되어 있는 상태 여부 확인
	int selectChargingNow(Map param);
	String selectCpConnectionInfo(Map param);

	// 원격 충전 종료 시 transactionId 조회
	int selectTransactionId(Map param);

	// 충전기 타입 조회
	String selectChargerType(Map param);

	// 충전현황 조회
	Map<String, String> selectChargingInfo(Map param);

	// 커넥터 연결 여부 조회
	String selectPreparingStartYn(Map param);

	// 충전 시작 여부 조회
	int selectChargingStartYn(Map param);
}
