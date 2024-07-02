package com.bluenetworks.webapp.app.promotion.controller;

import com.bluenetworks.webapp.app.charge.service.AppChargeService;
import com.bluenetworks.webapp.app.customer.service.AppCustomerService;
import com.bluenetworks.webapp.app.promotion.service.AppPromotionService;
import com.bluenetworks.webapp.common.CommonUtil;
import com.bluenetworks.webapp.common.NetUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/app/promotion")
public class AppPromotionController {

    private final AppPromotionService appPromotionService;

    private final CommonUtil commonUtil;

    @RequestMapping(value = "/register")
    public String my_favorites(HttpServletRequest request, Model model) {
        model.addAttribute("routerName", "프로모션 등록");

        return "app/promotion/promotion_register.app_tiles";
    }

    @RequestMapping(value = "/promotion_list")
    @ResponseBody
    public Map<String, Object> promotion_list(HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("userId", request.getParameter("userId"));

        Map<String, Object> paramMap = appPromotionService.selectCustomerDetail(param);
        param.put("customerId", paramMap.get("id"));
        param.put("companyId", paramMap.get("companyId"));

        List<Map<String, Object>> promotionList = appPromotionService.selectPromotionList(param);

        resultMap.put("result", promotionList);

        return resultMap;
    }

    @RequestMapping(value = "/promotion_register")
    @ResponseBody
    public JSONObject promotion_register(@RequestParam Map<String, Object> param, HttpServletRequest request, Model model) throws Exception {
        JSONObject result = new JSONObject();

        int promotionId = Integer.parseInt((String)param.get("promotionId"));
        String authCode = (String) param.get("authCode");

        param.put("userId", request.getParameter("userId"));

        Map<String, Object> paramMap = appPromotionService.selectCustomerDetail(param);

        param.put("customerId", paramMap.get("id"));
        param.put("companyId", paramMap.get("companyId"));
        param.put("promotionId", promotionId);
        param.put("authCode", authCode);

        int availability = appPromotionService.authCdCheck(param);

        if (availability == 1) {
            int success = appPromotionService.insertCustomerPromotion(param);
            success += appPromotionService.updateCustomerPromotion(param);

            if (success == 2) {
                result.put("result", commonUtil.result("success", 200, "성공"));
            } else {
                result.put("result", commonUtil.result("error", 500, "에러", "등록에 실패했습니다. 관리자에게 문의해 주세요."));
            }
        } else if (availability == 0) {
            result.put("result", commonUtil.result("duplication", 100, "실패", "잘못된 코드입니다."));
        } else {
            result.put("result", commonUtil.result("error", 500, "에러", "등록에 실패했습니다. 관리자에게 문의해 주세요."));
        }

        return result;
    }
}
