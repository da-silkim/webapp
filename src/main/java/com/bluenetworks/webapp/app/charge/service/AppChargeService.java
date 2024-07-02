package com.bluenetworks.webapp.app.charge.service;

import com.bluenetworks.webapp.app.charge.dto.request.RemoteStartTransactionRequest;
import com.bluenetworks.webapp.app.charge.dto.request.RemoteStopTransactionRequest;
import com.bluenetworks.webapp.app.charge.dto.request.TriggerMessageRequest;
import com.bluenetworks.webapp.app.charge.dto.response.RemoteStartTransactionResponse;
import com.bluenetworks.webapp.app.charge.dto.response.RemoteStopTransactionResponse;
import com.bluenetworks.webapp.app.charge.dto.type.MessageTrigger;
import com.bluenetworks.webapp.app.charge.dto.type.RemoteStartStopStatus;
import com.bluenetworks.webapp.app.charge.mapper.AppChargeMapper;
import com.bluenetworks.webapp.app.main.mapper.AppMainMapper;
import com.bluenetworks.webapp.common.StringUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppChargeService {

	@Value("${blue.router.url.v2}")
	private String routerUrl;

	private String isCall = "Not called";

	private final AppChargeMapper appChargeMapper;
	
	private final AppMainMapper appMainMapper;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public List<JSONObject> selectFavoritesList(String userId) {
        List<JSONObject> info = appChargeMapper.selectFavoritesList(userId);
        return info;
    }
	
	public int deleteFavoritesOne(JSONObject param) {
		return appChargeMapper.deleteFavoritesOne(param);
	}

	public List<Map<String, Object>> chargeList(Map<String, Object> param) throws Exception {

		return appChargeMapper.chargeList(param);
	}

	public Map<String, Object> selectPkId(Map<String, Object> param) throws Exception {

		return appChargeMapper.selectPkId(param);
	}

	public Map<String, Object> chargerDetail(Map<String, Object> param) throws Exception {

		return appChargeMapper.chargerDetail(param);
	}
	
	public String checkChargerStatus(Map<String, Object> param) throws Exception {
		String status = "";
		Map<String, Object> info = appChargeMapper.selectCharging(param);
		
		return status;
	}

	public Map<String, Object> selectConnector(Map<String, Object> param) throws Exception {

		return appChargeMapper.selectConnector(param);
	}
	
	public List<Map<String, Object>> selectConnectorList(Map<String, Object> param) throws Exception {
		
		return appChargeMapper.selectConnectorList(param);
	}
	
	public Map<String, Object> selectCustPaymentInfo(Map<String, Object> param) throws Exception {
		
		return appMainMapper.selectPaymentInfo(param);
	}

	public String selectIdToken(Map<String, Object> param) throws Exception {

		return appChargeMapper.selectIdToken(param);
	}

	public int selectChargingNow(Map param) throws Exception{
		return appChargeMapper.selectChargingNow(param);
	}

	// 제어실행전 cp상태 체크 : 충전기와 연결되어 있는 상태 여부 확인
	public String selectCpConnectionInfo(Map param) throws Exception{
		return appChargeMapper.selectCpConnectionInfo(param);
	}

	// 원격 충전 종료 시 transactionId 조회
	public int selectTransactionId(Map param) throws Exception{
		return appChargeMapper.selectTransactionId(param);
	}

	public Map<String, String> constollCallerSend(Map param) throws Exception {

		String url = routerUrl;
		String uri = String.valueOf(param.get("uri"));

		isCall = "Not called";
		log.info("routerUrl is 1 :::::::::: " + url + uri);
		log.info("routerUrl param :::::::::: " + param.toString());
		String chargeBoxSerialNumber = StringUtil.toNotNullString(param.get("chargeBoxSerialNumber"));

		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, String> rtnMap = new HashMap<String, String>();

		WebClient client = WebClient.builder()
				.baseUrl(url)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();

		if( "/remote-start-transaction".equals(uri) ) {

			RemoteStartTransactionRequest request = RemoteStartTransactionRequest.builder()
					.chargePointId(chargeBoxSerialNumber)
					.idTag(StringUtil.toNotNullString(param.get("idTag")))
					.connectorId(Integer.valueOf((String)param.get("connectorId")))
					.build();

			RemoteStartTransactionResponse response = client.post().uri(uriBuilder -> uriBuilder.path(uri).build())
					.bodyValue(request).retrieve().bodyToMono(RemoteStartTransactionResponse.class).block();

			if( response != null && RemoteStartStopStatus.Accepted.equals(response.getStatus()) ) {
				log.info("===================== /remote-start-transaction success ====================");
				log.info(response.toString());
				log.info(response.getStatus().toString());
				isCall = "called";

			} else if( response != null ){
				log.info("===================== /remote-start-transaction fail ====================");
				log.info(response.toString());
				log.info(response.getStatus().toString());

			}

		} else if( "/remote-stop-transaction".equals(uri) ) {

			RemoteStopTransactionRequest request = RemoteStopTransactionRequest.builder()
					.chargePointId(chargeBoxSerialNumber)
					.transactionId(StringUtil.toNotNullString(param.get("transactionId")))
					.build();

			RemoteStopTransactionResponse response = client.post().uri(uriBuilder -> uriBuilder.path(uri).build())
					.bodyValue(request).retrieve().bodyToMono(RemoteStopTransactionResponse.class).block();

			if( response != null && RemoteStartStopStatus.Accepted.equals(response.getStatus()) ) {
				log.info("===================== /remote-stop-transaction success ====================");
				log.info(response.toString());
				log.info(response.getStatus().toString());
				isCall = "called";

			} else if( response != null ){
				log.info("===================== /remote-stop-transaction fail ====================");
				log.info(response.toString());
				log.info(response.getStatus().toString());

			}
		} else if( "/triggerMessage".equals(uri) ) {
			log.info("===================== /triggerMessage constollCallerSend ====================");
			
			TriggerMessageRequest request = TriggerMessageRequest.builder()
					.requestedMessage(MessageTrigger.MeterValues)
					.chargePointId(chargeBoxSerialNumber)
					.connectorId(StringUtil.toNotNullString(param.get("connectorId")))
					.build();
			
			RemoteStopTransactionResponse response = client.post().uri(uriBuilder -> uriBuilder.path(uri).build())
					.bodyValue(request).retrieve().bodyToMono(RemoteStopTransactionResponse.class).block();

			
			if( response != null && RemoteStartStopStatus.Accepted.equals(response.getStatus()) ) {
				log.info("===================== /triggerMessage success ====================");
				log.info(response.toString());
				log.info(response.getStatus().toString());
				isCall = "called";
				
			} else if( response != null ){
				log.info("===================== /triggerMessage fail ====================");
				log.info(response.toString());
				log.info(response.getStatus().toString());
				
			}
		}

		rtnMap.put("isCall", isCall);

		return rtnMap;
	}

	public String selectChargerType(Map param) throws Exception{
		return appChargeMapper.selectChargerType(param);
	}

	// 충전현황 조회
	public Map<String, String> selectChargingInfo(Map param) throws Exception{
		return appChargeMapper.selectChargingInfo(param);
	}
	
	// 커넥터 연결 여부 조회
	public String selectPreparingStartYn(Map param) throws Exception{
		return appChargeMapper.selectPreparingStartYn(param);
	}

	// 충전 시작 여부 조회
	public int selectChargingStartYn(Map param) throws Exception{
		return appChargeMapper.selectChargingStartYn(param);
	}
}
