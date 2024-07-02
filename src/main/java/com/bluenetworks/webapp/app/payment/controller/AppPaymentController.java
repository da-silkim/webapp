package com.bluenetworks.webapp.app.payment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluenetworks.webapp.app.payment.service.AppPaymentService;
import com.bluenetworks.webapp.common.CommonUtil;
import com.bluenetworks.webapp.common.NetUtils;
import com.bluenetworks.webapp.common.SmartroPayUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/app/payment")
public class AppPaymentController {

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
	
	@Value("${blue.service-domain}")
	private String serviceDomain;
	
	private final AppPaymentService appPaymentService;
	
	private final SmartroPayUtil smartroPayUtil;
	
	private final CommonUtil commonUtil;
	
	
	@RequestMapping(value = "/payment")
    public String payment(HttpServletRequest request, Model model) throws Exception  {
		model.addAttribute("routerName", "결제카드 관리");
	
		//smartRo 관련
		String EdiDate = smartroPayUtil.getyyyyMMddHHmmss();
		Moid = "bluenetworks";
		String encData = EdiDate + Mid + Moid + "SMARTRO!@#";
		String MallUserId = (String) NetUtils.getSession(request, "customerId");
		
		String EncryptData = SmartroPayUtil.encodeSHA256Base64(encData);
		
		model.addAttribute("Mid", Mid);
		model.addAttribute("Moid", Moid);
		model.addAttribute("MallUserId", MallUserId);
		model.addAttribute("SspMallId", SspMallId);
		model.addAttribute("EncryptData", EncryptData);
		model.addAttribute("EdiDate", EdiDate);
		model.addAttribute("ReturnUrl", serviceDomain + "/app/payment/returnBillPay");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("customerId", String.valueOf(NetUtils.getSession(request, "customerId")));
		
		return "app/payment/payment.app_tiles";
    }
	
	@RequestMapping(value = "/payment/cardList")
	@ResponseBody
	public Map<String, Object> paymentCardList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<>();
		paramMap.put("customerId", String.valueOf(NetUtils.getSession(request, "customerId")));
		result.put("cardList", appPaymentService.selectPaymentList(paramMap));

		return result;
	}
	
	@RequestMapping(value = "/payment/changeMainCard")
	@ResponseBody
	public Map<String, Object> changeMainCard(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<>();
		paramMap.put("customerId", String.valueOf(NetUtils.getSession(request, "customerId")));
		result.put("result", appPaymentService.changeMainCard(paramMap));
		
		return result;
	}
	
	@RequestMapping(value = "/returnBillPay")
	public String returnBillPay(HttpServletRequest request, Model model) {
		model.addAttribute("routerName", "결제카드 등록");
		return "app/payment/returnBillPay.app_tiles";
	}
	
	@RequestMapping(value = "/billTokenUpdate")
	@ResponseBody
	public JSONObject billTokenUpdate(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		JSONObject result = new JSONObject();
		paramMap.put("customerId", String.valueOf(NetUtils.getSession(request, "customerId")));
		
		int success = appPaymentService.billTokenUpdate(paramMap);

		if (success > 1) {
			result.put("result", commonUtil.result("success", 001, "저장 성공", "성공하였습니다."));
		} else {
			result.put("result", commonUtil.result("fail", 002, "저장 실패", "저장에 실패하였습니다."));
		}

		return result;
	}
	
	@RequestMapping(value = "/deleteBillkeyReq")
    public @ResponseBody Map<String, Object> deleteBillkeyReq(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
    	
		JSONObject result = new JSONObject();
		paramMap.put("customerId", (String) NetUtils.getSession(request, "customerId"));
		paramMap.put("userIdx", (String) NetUtils.getSession(request, "userid"));
    	int success = appPaymentService.deleteBillkeyReq(paramMap);
    	
    	if (success > 1) {
			result.put("result", commonUtil.result("success", 001, "저장 성공", "성공하였습니다."));
		} else {
			result.put("result", commonUtil.result("fail", 002, "저장 실패", "저장에 실패하였습니다."));
		}
    	
    	return result;
    }

	@RequestMapping(value = "/payment_history")
	public String payment_history(HttpServletRequest request, Model model) {
		model.addAttribute("routerName", "결제 이력");
		return "app/payment/payment_history.app_tiles";
	}

	@RequestMapping(value = "/payment_history_list")
	@ResponseBody
	public Map<String, Object> payment_history_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<>();

		String pageCount = "5";

		if (paramMap.containsKey("currPageNo")) {
			paramMap.put("pageCount" , Integer.parseInt( pageCount ) );
			paramMap.put("startRow" , Integer.parseInt( paramMap.get("currPageNo").toString() ) - 1 );
			if( !paramMap.get("currPageNo").equals("1") )
				paramMap.put("startRow", ( Integer.parseInt( paramMap.get("currPageNo").toString() ) - 1 ) *  5 );
		}

		String idToken = appPaymentService.selectIdToken(paramMap);

		paramMap.put("idToken", idToken);

		List<Map<String, Object>> paymentList = appPaymentService.selectPaymentHistoryList(paramMap);

		int totalAmount = 0;
		if (!paymentList.isEmpty()) {
			for (Map<String, Object> list : paymentList) {
				totalAmount += Integer.parseInt((String)list.get("authAmount"));
			}
		}

		int listCount = appPaymentService.selectPaymentHistoryCount(paramMap);

		result.put("rows", paymentList);
		result.put("totalAmount", totalAmount);
		result.put("pageInfo", commonUtil.getPageInfo(paramMap, listCount+""));

		return result;
	}
}
