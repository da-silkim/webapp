package com.bluenetworks.webapp.app.charge.controller;

import com.bluenetworks.webapp.app.charge.dto.type.MessageTrigger;
import com.bluenetworks.webapp.app.charge.service.AppChargeService;
import com.bluenetworks.webapp.app.customer.service.AppCustomerService;
import com.bluenetworks.webapp.common.CommonUtil;
import com.bluenetworks.webapp.common.NetUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.MapUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/app/charge")
public class AppChargeController {

	private final AppChargeService appChargeService;
    private final AppCustomerService appCustomerService;
	
	private final CommonUtil commonUtil;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());
	
	
	@RequestMapping(value = "/my_charge")
    public String my_charge(HttpServletRequest request, Model model) {
        return "app/charge/my_charge.app_tiles";
    }
    
    @RequestMapping(value = "/my_favorites")
    public String my_favorites(HttpServletRequest request, Model model) {
    	model.addAttribute("routerName", "마이 충전소");
    	return "app/charge/my_favorites.app_tiles";
    }

    @RequestMapping(value = "/charge_history")
    public String charge_history(HttpServletRequest request, Model model) {
        model.addAttribute("routerName", "충전 이력");
        return "app/charge/charge_history.app_tiles";
    }


    @RequestMapping(value = "/charge_now")
    public String charge_now(HttpServletRequest request, Model model) throws Exception {
        Map param = new HashMap();

        model.addAttribute("routerName", "충전 현황");

        String customerId = (String) NetUtils.getSession(request, "customerId");
        param.put("customerId", customerId);
        Map infoParam = appChargeService.selectChargingInfo(param);

        if(infoParam != null) {
            Map<String, Object> resParam = appChargeService.chargerDetail(param);
            double price  = Double.parseDouble((String)resParam.get("unitPrice")) * Double.parseDouble((String)infoParam.get("chargePower"));

            model.addAttribute("price", price);
            model.addAttribute("unitPrice", resParam.get("unitPrice"));
            model.addAttribute("csId", infoParam.get("csId"));
            model.addAttribute("cpId", infoParam.get("cpId"));
            model.addAttribute("modelId", infoParam.get("modelId"));
            model.addAttribute("connectorId", infoParam.get("connectorId"));
            model.addAttribute("stationName", infoParam.get("stationName"));
            model.addAttribute("chargerName", infoParam.get("chargerName"));
            model.addAttribute("chargeBoxSerialNumber", infoParam.get("chargeBoxSerialNumber"));
            model.addAttribute("transactionId", infoParam.get("transactionId"));
            model.addAttribute("meterStart", infoParam.get("meterStart"));
            model.addAttribute("remainStopTs", infoParam.get("remainStopTs"));
            model.addAttribute("currentA", infoParam.get("currentA"));
            model.addAttribute("currentPower", infoParam.get("currentPower"));
            model.addAttribute("meterValueTimestamp", infoParam.get("meterValueTimestamp"));
            model.addAttribute("chargePower", infoParam.get("chargePower"));
            model.addAttribute("soc", infoParam.get("soc"));
            model.addAttribute("idTag", infoParam.get("idTag"));
            model.addAttribute("startTimestamp", infoParam.get("startTimestamp"));
            model.addAttribute("chargeSpeedType", infoParam.get("chargeSpeedType"));
        } else {
            model.addAttribute("error", "notCharging");
        }

        return "app/charge/charge_now.app_tiles";
    }
    
    @RequestMapping(value = "/my_charge/action", method = RequestMethod.POST, produces = "application/json;")
    @ResponseBody
    @Transactional
    public JSONObject my_charge_action(HttpServletRequest request, Model model) {

        JSONObject result = new JSONObject();

        try{

            String userId = (String) NetUtils.getSession(request, "customerId");
            List<JSONObject> list = appChargeService.selectFavoritesList(userId);

            result.put("result", commonUtil.result("success", 200, "성공"));
            JSONObject data = new JSONObject();
            data.put("list", list);
            result.put("data", data);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.put("result", commonUtil.result("error", 001, "오류가 발생했습니다."));
        }

        if(commonUtil.is_error(result)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }
    
    
    @RequestMapping(value = "/my_charge/delete", method = RequestMethod.POST, produces = "application/json;")
    @ResponseBody
    @Transactional
    public JSONObject my_charge_delete(HttpServletRequest request, Model model) {
    	
    	JSONObject result = new JSONObject();
    	
    	try{
    		
    		String userId = (String) NetUtils.getSession(request, "customerId");
    		String favId = request.getParameter("favId"); //즐겨찾기 번호
    		JSONObject paramObj = new JSONObject();
    		paramObj.put("userId", userId);
    		paramObj.put("favId", favId);
    		
    		appChargeService.deleteFavoritesOne(paramObj);
    		
    		result.put("result", commonUtil.result("success", 200, "성공"));

            List<JSONObject> list = appChargeService.selectFavoritesList(userId);
            JSONObject data = new JSONObject();
            data.put("list", list);
            result.put("data", data);
    		
    	} catch (Exception e) {
    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    		e.printStackTrace();
    		result.put("result", commonUtil.result("error", 001, "오류가 발생했습니다."));
    	}
    	
    	if(commonUtil.is_error(result)){
    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    	}
    	
    	return result;
    }

    @RequestMapping(value = "/charge_history_list")
    @ResponseBody
    public Map<String, Object> charge_history_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        String id = (String) appCustomerService.selectCustomerId(paramMap);
        paramMap.put("id", id);

        List<Map<String, Object>> chargeList = appChargeService.chargeList(paramMap);
        int totalInfo = chargeList.size();

        result.put("rows", chargeList);
        result.put("total", totalInfo);

        return result;
    }
    
	@RequestMapping(value = "/charge_remote")
	public String charge_remote(@RequestParam(name = "cpId", required = false) String cpId
                                , @RequestParam(name = "connectorId", required = false) String connectorId
                                , HttpServletRequest request, Model model) throws Exception {
		
		LOGGER.info("/app/charge/charge_remote cpId : "+cpId+", connectorId = "+connectorId);
		model.addAttribute("routerName", "원격 충전");
        Map<String, Object> param = new HashMap<String, Object>();

        // idToken 조회
        String customerId = (String) NetUtils.getSession(request, "customerId");
        param.put("customerId", customerId);

        String idToken = appChargeService.selectIdToken(param);
        model.addAttribute("idToken", idToken);

        String returnUrl = "app/charge/charge_remote.app_tiles";
        
        if ( cpId != null ) {
            param.put("cpId", cpId);
            Map<String, Object> resParam = appChargeService.selectPkId(param);
            
            if( MapUtils.isEmpty(resParam) ) {
            	returnUrl = "app/main/index.app_main_tiles";
            	model.addAttribute("result", commonUtil.result("fail", 002, "충전기 조회 실패", "조회하신 충전기가 존재하지 않습니다. "));
            	
            } else {
                Map<String, Object> result = appChargeService.chargerDetail(resParam);
                result.put("connectorId", connectorId);
                Map<String, Object> connectorInfo = appChargeService.selectConnector(result);

                model.addAttribute("stationName", result.get("stationName"));
                model.addAttribute("chargerName", result.get("chargerName"));
                model.addAttribute("chargerModelName", result.get("chargerModelName"));
                model.addAttribute("longitude", result.get("longitude"));
                model.addAttribute("latitude", result.get("latitude"));
                model.addAttribute("csPkId", result.get("csPkId"));
                model.addAttribute("cpPkId", result.get("cpPkId"));
                model.addAttribute("csId", result.get("csId"));
                model.addAttribute("cpId", result.get("cpId"));
                model.addAttribute("cpModelId", result.get("cpModelId"));
                model.addAttribute("unitPrice", result.get("unitPrice"));
                model.addAttribute("chargeBoxSerialNumber", result.get("chargeBoxSerialNumber"));
                model.addAttribute("connectorId", connectorId);
                model.addAttribute("connectorInfo", connectorInfo);
            }
        }

		return returnUrl;
	}
	
	@RequestMapping(value = "/charge_remote/action")
	@ResponseBody
	public JSONObject modify_my_info(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		
		JSONObject result = new JSONObject();

		// 충전기 상태 확인
		appChargeService.checkChargerStatus(paramMap);
		

		return result;
	}

    @RequestMapping(value = "/connector_list")
    @ResponseBody
    public List<Map<String, Object>> connector_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> param = appChargeService.selectPkId(paramMap);
        paramMap.put("cpId", param.get("cpPkId"));

        result = appChargeService.selectConnectorList(paramMap);

        return result;
    }
    
    @RequestMapping(value = "/custPaymentInfo")
    @ResponseBody
    public Map<String, Object> custPaymentInfo(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
    	
    	String customerId = (String) NetUtils.getSession(request, "customerId");
    	paramMap.put("customerId", customerId);
        
    	return appChargeService.selectCustPaymentInfo(paramMap);
    }

    @Transactional(rollbackFor= {Exception.class})
    @RequestMapping(value = "/remote_charge_control")
    public @ResponseBody Map remote_charge_control(@RequestParam Map<String, Object> map, HttpServletRequest request) throws Exception {

        Map rtnMap = new HashMap();			//return 맵
        Map callerMap =  new HashMap();		//콜할 때 사용하는 맵
        Map argsMap =  new HashMap();		//callerMap에 담는 argument 맵

        rtnMap.put("successYN", "Y");

        String errCd = "";

        map.put("chargeBoxSerialNumber", map.get("chargeBoxSerialNumber"));
        map.put("companyId", NetUtils.getSession(request, "companyId"));
        map.put("customerId", NetUtils.getSession(request, "customerId"));
        map.put("userId", NetUtils.getSession(request, "userid"));
        LOGGER.info("routerUrl map :::::::::: " + map.toString());
        try {
            String controlType = String.valueOf(map.get("controlType"));

            argsMap.put("chargeBoxSerialNumber", map.get("chargeBoxSerialNumber"));

            /***2. 원격 충전 ***/
            // 원격 트랜잭션 시작인 경우
            if( "RemoteStartTransaction".equals(controlType) ) {
                int chargingNow = appChargeService.selectChargingNow(map);
                String cpStatus = appChargeService.selectCpConnectionInfo(map);

                if (chargingNow > 0) {
                    rtnMap.put("successYN", "N");
                    rtnMap.put("errCd", "InUse");
                } else if(cpStatus.equals("N")) {
                    rtnMap.put("successYN", "N");
                    rtnMap.put("errCd", "cpNotConnected");
                } else {
                    //args(argsMap)값 알맞게 셋팅
                    argsMap.put("idTag", map.get("idTag"));
                    argsMap.put("connectorId", map.get("connectorId"));
                    argsMap.put("uri", "/remote-start-transaction");

                    // callerMap을 파라미터로 넘겨서 wamp콜 진행
                    rtnMap = appChargeService.constollCallerSend(argsMap);
                }
            }
            // 원격 트랜잭션 중지인 경우
            else if( "RemoteStopTransaction".equals(controlType) ) {
                int transactionId = appChargeService.selectTransactionId(map);

                //args(argsMap)값 알맞게 셋팅
                argsMap.put("transactionId", transactionId);
                argsMap.put("uri", "/remote-stop-transaction");

                // callerMap을 파라미터로 넘겨서 wamp콜 진행
                rtnMap = appChargeService.constollCallerSend(argsMap);
            }

            String called = String.valueOf(rtnMap.get("isCall"));

            if ("called".equals(called)) {
                rtnMap.put("successYN", "Y");
            } else {
                rtnMap.put("successYN", "N");
            }

        } catch(DuplicateKeyException ee) {
            LOGGER.error(ee.getMessage());

            rtnMap.put("successYN", "N");
            rtnMap.put("errCd", "DuplicateKey");

        } catch(Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(e.toString());

            rtnMap.put("successYN", "N");
            rtnMap.put("errCd", "Exception");
        }

        return rtnMap;
    }

    @RequestMapping(value = "/charge_possible")
    @ResponseBody
    public Map charge_possible(@RequestParam(name = "cpId", required = false) String cpId, HttpServletRequest request, Model model) throws Exception {
        Map result = new HashMap();
        Map param = new HashMap();

        String status = "";
        String customerId = (String)NetUtils.getSession(request, "customerId");
        param.put("customerId", customerId);
        param.put("cpId", cpId);

        // 1. 커넥터랑 차량이 연결 됐는지 확인
        String recvPayload = appChargeService.selectPreparingStartYn(param);

        if (recvPayload != null) {
            // Jackson ObjectMapper 생성
            ObjectMapper mapper = new ObjectMapper();
            // JSON 문자열을 맵으로 변환
            Map<String, Object> payloadMap = mapper.readValue(recvPayload, Map.class);
            // "status" 키의 값 추출
            status = (String) payloadMap.get("status");
        }

        // 2. 차량과 커넥터랑 충전 시작이 됐는지 확인
        int currentTxCount = appChargeService.selectChargingStartYn(param);

        if (currentTxCount > 0) {
            result.put("result", "charging");
        } else if (status.equals("Preparing"))  {
            result.put("result", "preparing");
        } else {
            result.put("result", "notCharging");
        }

        return result;
    }

    @RequestMapping(value = "/charging_info_refresh")
    @ResponseBody
    public Map charging_info_refresh(HttpServletRequest request, Model model) throws Exception {
        Map result = new HashMap();
        Map param = new HashMap();

        String customerId = (String)NetUtils.getSession(request, "customerId");
        param.put("customerId", customerId);

        result = appChargeService.selectChargingInfo(param);

        return result;
    }
    
    @Transactional(rollbackFor= {Exception.class})
    @RequestMapping(value = "/triggerMessage")
    public @ResponseBody Map triggerMessage(@RequestParam Map<String, Object> map, HttpServletRequest request) throws Exception {

        Map rtnMap = new HashMap();			//return 맵
        Map callerMap =  new HashMap();		//콜할 때 사용하는 맵
        Map argsMap =  new HashMap();		//callerMap에 담는 argument 맵

        rtnMap.put("successYN", "Y");

        String errCd = "";

        LOGGER.info("routerUrl map :::::::::: " + map.toString());
        try {

            argsMap.put("chargeBoxSerialNumber", map.get("chargeBoxSerialNumber"));
			argsMap.put("connectorId", map.get("connectorId"));
            argsMap.put("uri", "/triggerMessage");

            LOGGER.info("routerUrl argsMap :::::::::: " + argsMap.toString());
            
            // callerMap을 파라미터로 넘겨서 wamp콜 진행
            rtnMap = appChargeService.constollCallerSend(argsMap);

            String called = String.valueOf(rtnMap.get("isCall"));

            if ("called".equals(called)) {
                rtnMap.put("successYN", "Y");
            } else {
                rtnMap.put("successYN", "N");
            }

        } catch(Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(e.toString());

            rtnMap.put("successYN", "N");
            rtnMap.put("errCd", "Exception");
        }

        return rtnMap;
    }
    
}
