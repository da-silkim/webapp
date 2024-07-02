package com.bluenetworks.webapp.app.customer.controller;

import javax.servlet.http.HttpServletRequest;

import com.bluenetworks.webapp.app.customer.service.AppCustomerService;
import com.bluenetworks.webapp.app.file.FileUtil;
import com.bluenetworks.webapp.common.CommonUtil;
import com.bluenetworks.webapp.common.NetUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/app/customer")
public class AppCustomerController {

	private final CommonUtil commonUtil;
	private final FileUtil fileUtil;

	private final AppCustomerService appCustomerService;

	@RequestMapping(value = "/my_info")
    public String my_info(HttpServletRequest request, Model model) {
    	model.addAttribute("routerName", "고객 센터");
    	return "app/customer/my_info.app_tiles";
    }
	
	@RequestMapping(value = "/my_noti")
	public String my_noti(HttpServletRequest request, Model model) {
		model.addAttribute("routerName", "공지사항");
		return "app/customer/my_noti.app_tiles";
	}

	@RequestMapping(value = "/my_noti_detail")
	public String my_noti_detail(HttpServletRequest request, Model model) throws Exception {
		model.addAttribute("routerName", "공지사항 상세");

		Map<String, Object> paramMap = new HashMap<>();

		int boardId = Integer.parseInt(request.getParameter("boardId"));

		paramMap.put("boardId", boardId);

		appCustomerService.noticeViewsUpdate(paramMap);
		Map<String, Object> result = appCustomerService.noticeDetail(paramMap);

		model.addAttribute("subject", result.get("subject"));
		model.addAttribute("content", result.get("content"));
		model.addAttribute("fmtRegDate", result.get("fmtRegDate"));
		model.addAttribute("views", result.get("views"));

		return "app/customer/my_noti_detail.app_tiles";
	}
	
	@RequestMapping(value = "/my_question")
	public String my_question(HttpServletRequest request, Model model) {
		model.addAttribute("routerName", "1:1문의");
		return "app/customer/my_question.app_tiles";
	}
	
	@RequestMapping(value = "/my_voc")
	public String my_voc(HttpServletRequest request, Model model) {
		model.addAttribute("routerName", "고장신고");

		String csId = request.getParameter("csId");

		if (csId != null) {
			model.addAttribute("csId", csId);
		}

		return "app/customer/my_voc.app_tiles";
	}

	@RequestMapping(value = "/modify")
	public String modify(HttpServletRequest request, Model model) {
		model.addAttribute("routerName", "내 정보 관리");

		return "app/customer/my_modify.app_tiles";
	}

	@RequestMapping(value = "/my_info_set_data")
	@ResponseBody
	public Map<String, Object> my_info_set_data(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		String id = (String) appCustomerService.selectCustomerId(paramMap);

		Map<String, Object> detail = appCustomerService.selectDetail(id);

		result.put("result", detail);

		return result;
	}

	@RequestMapping(value = "/modify_my_info")
	@ResponseBody
	public JSONObject modify_my_info(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		JSONObject result = new JSONObject();

		String id = (String) appCustomerService.selectCustomerId(paramMap);

		paramMap.put("id", id);

		int success1 = appCustomerService.update(paramMap);

		int myCar = appCustomerService.evCheck(paramMap);

		if (myCar == 1) {
			int success2 = appCustomerService.updateCar(paramMap);
		} else {
			int success3 = appCustomerService.insertCustomerEv(paramMap);
		}

		if (success1 == 1) {
			result.put("result", commonUtil.result("success", 001, "저장 성공", "성공하였습니다."));
		} else if(success1 == 0) {
			result.put("result", commonUtil.result("fail", 002, "저장 실패", "저장에 실패하였습니다."));
		}

		return result;
	}

	@RequestMapping(value = "/modify_password")
	@ResponseBody
	public JSONObject modify_password(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		JSONObject result = new JSONObject();
		int success = 0;

		String id = (String) NetUtils.getSession(request, "customerId");

		paramMap.put("id", id);

		String prePassword = commonUtil.encryption_sha256((String) paramMap.get("prePassword"));
		paramMap.put("password", prePassword);
		int userCount = appCustomerService.selectPasswordCheck(paramMap);

		if (userCount == 1) {
			String changePassword = commonUtil.encryption_sha256((String) paramMap.get("changePassword"));
			paramMap.put("changePassword", changePassword);

			success = appCustomerService.passwordUpdate(paramMap);
		}

		if (success == 1) {
			result.put("result", commonUtil.result("success", 200, "저장 성공", "성공하였습니다."));
		} else if (userCount == 0) {
			result.put("result", commonUtil.result("password mismatch", 001, "비밀번호 불일치", "입력하신 비밀번호가 고객님의 비밀번호와 일치하지 않습니다."));
		} else {
			result.put("result", commonUtil.result("fail", 001, "저장 실패", "저장에 실패하였습니다."));
		}

		return result;
	}

	@RequestMapping(value = "/withdrawal")
	@ResponseBody
	@Transactional
	public JSONObject withdrawal(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		JSONObject result = new JSONObject();

		String id = (String) appCustomerService.selectCustomerId(paramMap);
		paramMap.put("id", id);

		try {
			int successCustomer = appCustomerService.updateCustomerStatus(paramMap);
			int successAuth = appCustomerService.updateAuthUnuse(paramMap);
			int successCard = appCustomerService.deleteCustomerCard(paramMap);
			int successEv = appCustomerService.deleteCustomerEv(paramMap);
			result.put("result", commonUtil.result("success", 200, "저장되었습니다."));
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			result.put("result", commonUtil.result("error", 001, "오류가 발생했습니다.", "오류가 발생했습니다."));
		}

		return result;
	}

	@RequestMapping(value = "/board_list")
	@ResponseBody
	public Map<String, Object> board_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> list = appCustomerService.noticeList(paramMap);

		result.put("rows", list);

		return result;
	}

	@RequestMapping(value = "/faq_list")
	@ResponseBody
	public Map<String, Object> faq_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> faqList = appCustomerService.faqList(paramMap);

		result.put("result", faqList);

		return result;
	}

	@RequestMapping(value = "/qna_list")
	@ResponseBody
	public Map<String, Object> qna_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		String pageCount = "5";

		if (paramMap.containsKey("currPageNo")) {
			paramMap.put("pageCount" , Integer.parseInt( pageCount ) );
			paramMap.put("startRow" , Integer.parseInt( paramMap.get("currPageNo").toString() ) - 1 );
			if( !paramMap.get("currPageNo").equals("1") )
				paramMap.put("startRow", ( Integer.parseInt( paramMap.get("currPageNo").toString() ) - 1 ) *  5 );
		}

		String id = (String) appCustomerService.selectCustomerId(paramMap);
		paramMap.put("customerId", id);

		List<Map<String, Object>> qnaList = appCustomerService.selectQnaList(paramMap);
		int listCount = appCustomerService.selectQnaCount(paramMap);

		result.put("rows", qnaList );
		result.put("pageInfo", commonUtil.getPageInfo(paramMap, listCount+""));

		return result;
	}

	@RequestMapping(value = "/select_qna_type")
	@ResponseBody
	public Map<String, Object> select_qna_type(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		paramMap.put("groupCd", paramMap.get("qnaGubun"));

		List<Map<String, Object>> selectQnaType = appCustomerService.selectQnaType(paramMap);

		result.put("selectQnaType", selectQnaType);

		return result;
	}

	@RequestMapping(value = "/qna_insert")
	@ResponseBody
	public JSONObject qna_insert(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		JSONObject result = new JSONObject();

		Map<String, Object> userDetail = appCustomerService.selectCustomerDetail(paramMap);

		String vocId = appCustomerService.selectVocIdMax();
		int chgVocId = Integer.parseInt(vocId) + 1;
		vocId = String.valueOf(chgVocId);

		paramMap.put("vocId", vocId);
		paramMap.put("customerName", userDetail.get("customerName"));
		paramMap.put("mobile", userDetail.get("mobile"));
		paramMap.put("id", userDetail.get("id"));

		int success = appCustomerService.insertQna(paramMap);

		if (success == 1) {
			result.put("result", commonUtil.result("success", 001, "저장 성공", "성공하였습니다."));
		} else if(success == 0) {
			result.put("result", commonUtil.result("fail", 002, "저장 실패", "저장에 실패하였습니다."));
		}

		return result;
	}

	@RequestMapping(value = "/qna_detail")
	@ResponseBody
	public Map<String, Object> qna_detail(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> qnaPopList = appCustomerService.selectPopDetail(paramMap);

		paramMap.put("groupCd", qnaPopList.get("vocKindCd"));
		List<Map<String, Object>> selectQnaType = appCustomerService.selectQnaType(paramMap);

		result.put("result", qnaPopList);
		result.put("selectQnaType", selectQnaType);

		return result;
	}

	@RequestMapping(value = "/qna_modify")
	@ResponseBody
	public JSONObject qna_modify(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		JSONObject result = new JSONObject();

		Map<String, Object> userDetail = appCustomerService.selectCustomerDetail(paramMap);

		paramMap.put("customerId", userDetail.get("id"));

		int success = appCustomerService.updateQnaPop(paramMap);

		if (success == 1) {
			result.put("result", commonUtil.result("success", 001, "저장 성공", "성공하였습니다."));
		} else if(success == 0) {
			result.put("result", commonUtil.result("fail", 002, "저장 실패", "저장에 실패하였습니다."));
		}

		return result;
	}

	@RequestMapping(value = "/qna_delete")
	@ResponseBody
	public JSONObject qna_delete(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		JSONObject result = new JSONObject();

		int success = appCustomerService.deleteQna(paramMap);

		if (success == 1) {
			result.put("result", commonUtil.result("success", 001, "저장 성공", "성공하였습니다."));
		} else if(success == 0) {
			result.put("result", commonUtil.result("fail", 002, "저장 실패", "저장에 실패하였습니다."));
		}

		return result;
	}

	@RequestMapping(value = "/station_list")
	@ResponseBody
	public List<Map<String, Object>> station_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();
		result = appCustomerService.searchStationList(paramMap);

		return result;
	}

	@RequestMapping(value = "/charger_list")
	@ResponseBody
	public List<Map<String, Object>> charger_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();
		result = appCustomerService.searchChargerList(paramMap);

		return result;
	}


	@RequestMapping(value = "/as_type_list")
	@ResponseBody
	public List<Map<String, Object>> as_type_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();
		result = appCustomerService.selectQnaType(paramMap);

		return result;
	}

	@RequestMapping(value = "/broken_report")
	@ResponseBody
	public Map<String, Object> broken_report(@RequestPart(value="file",required = false) MultipartFile file
			, @RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		log.info("file = " + file);
		log.info("paramMap = " + paramMap);

		if( file != null ) {
			Map<String, Object> fileMap = new HashMap<String, Object>();
			fileMap = fileUtil.commImageUpdate(file, "broken", paramMap, request);
			paramMap.put("uploadId", fileMap.get("atchFileId"));

		}

		Map<String, Object> userDetail = appCustomerService.selectCustomerDetail(paramMap);
		String vocId = appCustomerService.selectVocIdMax();
		int chgVocId = Integer.parseInt(vocId) + 1;
		vocId = String.valueOf(chgVocId);

		paramMap.put("vocId", vocId);
		paramMap.put("id", userDetail.get("id"));

		int success = appCustomerService.insertBrokenCharger(paramMap);

		if (success == 1) {
			result.put("result", commonUtil.result("success", 001, "저장 성공", "성공하였습니다."));
		} else if(success == 0) {
			result.put("result", commonUtil.result("fail", 002, "저장 실패", "저장에 실패하였습니다."));
		}

		return result;
	}
}
