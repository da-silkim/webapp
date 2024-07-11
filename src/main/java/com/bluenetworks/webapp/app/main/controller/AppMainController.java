package com.bluenetworks.webapp.app.main.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluenetworks.webapp.app.charge.service.AppChargeService;
import com.bluenetworks.webapp.app.main.service.AppMainService;
import com.bluenetworks.webapp.common.NetUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class AppMainController {

	private final AppMainService appMainService; 
	private final AppChargeService appChargeService;

	@RequestMapping(value = "/main")
	public String index1(@RequestParam Map<String,String> params, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {

		// 팝업
		JSONObject param = new JSONObject();
		param.put("popuptype", 1); // 앱

		//본인인증모듈로 요청
		String popup = request.getParameter("popup");
		if(popup != null && !popup.equals("")){
			model.addAttribute("popupUrl", popup);
			model.addAttribute("popupData", params);
		}
		
		String userId = (String) NetUtils.getSession(request, "userid");
		String userName = (String) NetUtils.getSession(request, "username");
		String customerId = (String) NetUtils.getSession(request, "customerId");
		
		System.out.println("userid :::::::: "+userId);
		System.out.println("username :::::::: "+userName);
		System.out.println("customerId :::::::: "+customerId);
		model.addAttribute("userName", userName);

		return "app/main/index.app_main_tiles";
	}
	
	@RequestMapping(value = "/main/list")
    @ResponseBody
    public Map<String, Object> station_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<Map<String, Object>> noticList = appMainService.selectNoticeList(paramMap);
		resultMap.put("notiList", noticList);
		
		String userId = (String) NetUtils.getSession(request, "userid");
		String customerId = (String) NetUtils.getSession(request, "customerId");
		
		paramMap.put("customerId", customerId); // 앱
		
		List<Map<String, Object>> stationList = new ArrayList<Map<String, Object>>();
		if(userId == null || "".equals(userId)) {
			stationList = appMainService.selectPeriStationList(paramMap);
			resultMap.put("paymentUrl", "");
		} else {
			stationList = appMainService.selectMyStationList(paramMap);
			resultMap.put("paymentUrl", appMainService.selectPaymentInfo(paramMap));
		}

		LocalDateTime currentDateTime = LocalDateTime.now();

		// Define the custom date-time formatter
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.appendPattern("yyyy-MM")
				.toFormatter();

		// Format the current year and month
		String formattedYearMonth = currentDateTime.format(formatter);

		paramMap.put("yearMonth", formattedYearMonth);

		List<Map<String, Object>> dashboard = new ArrayList<>();
		dashboard = appMainService.selectMonthDashboard(paramMap);

		double power = 0;
		int totalPrice = 0;
		int totCount = 0;

		for(Map<String, Object> item : dashboard) {
			power += Double.parseDouble((String) item.get("power"));
			totalPrice += Integer.parseInt((String) item.get("totalPrice"));
			totCount += Integer.parseInt((String) item.get("totCount"));
		}
			
		resultMap.put("customerId", customerId);
		resultMap.put("stationList", stationList);
		resultMap.put("power", power);
		resultMap.put("totalPrice", totalPrice);
		resultMap.put("totCount", totCount);


        return resultMap;
    }

	@RequestMapping(value = "/main/charge_status")
	@ResponseBody
	public Map charge_status(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		return appChargeService.selectChargingInfo(paramMap);
	}
}
