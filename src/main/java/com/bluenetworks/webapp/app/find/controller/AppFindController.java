package com.bluenetworks.webapp.app.find.controller;

import com.bluenetworks.webapp.app.customer.service.AppCustomerService;
import com.bluenetworks.webapp.app.find.service.AppFindService;
import com.bluenetworks.webapp.common.CommonUtil;
import com.bluenetworks.webapp.common.NetUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/app/find")
public class AppFindController {

    private final CommonUtil commonUtil;

    private final AppFindService appFindService;
    private final AppCustomerService appCustomerService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @RequestMapping(value = "/find_map")
    public String find_map(@RequestParam(name = "id", required = false) Integer id, HttpServletRequest request, Model model) throws Exception {

        if (id != null) {
            Map<String, Object> csLocation = appFindService.selectStationLocation(id);

            model.addAttribute("csId", csLocation.get("csId"));
            model.addAttribute("longitude", csLocation.get("longitude"));
            model.addAttribute("latitude", csLocation.get("latitude"));
            model.addAttribute("id", id);
        }

        return "app/find/find_map.app_main_tiles";
    }

    @RequestMapping(value = "/cs_detail")
    public String cs_detail(@RequestParam(name = "id", required = false) Integer id, @RequestParam(name = "csId", required = false) String csId
                                            , HttpServletRequest request, Model model) throws Exception {
        model.addAttribute("routerName", "상세 보기");

        if (csId != null) {
            id = appFindService.selectCsId(csId);
        }

        Map<String, Object> csDetail = appFindService.csDetail(id);
        String operatingTimeStart = (String) csDetail.get("operatingTimeStart");
        String operatingTimeEnd = (String) csDetail.get("operatingTimeEnd");
        if (!operatingTimeStart.isEmpty()) {
            operatingTimeStart = formatTime(operatingTimeStart);
        }
        if (!operatingTimeEnd.isEmpty()) {
            operatingTimeEnd = formatTime(operatingTimeEnd);
        }
        String operatingTime = operatingTimeStart + " ~ " + operatingTimeEnd;
        if (csDetail.get("serviceCompanyName").equals("블루네트웍스")) {
            csDetail.put("serviceCompanyName", "이채움");
        }

        Map<String, Object> cpCount = appFindService.selectChargingStatus(id);

        model.addAttribute("address", csDetail.get("address"));
        model.addAttribute("serviceName", csDetail.get("serviceCompanyName"));
        model.addAttribute("operatingTime", operatingTime);
        model.addAttribute("parkingFee", csDetail.get("parkingFee"));
        model.addAttribute("csId", id);

        model.addAttribute("available", cpCount.get("available"));
        model.addAttribute("chargingReserve", cpCount.get("chargingReserve"));
        model.addAttribute("charging", cpCount.get("charging"));
        model.addAttribute("unAvailable", cpCount.get("unAvailable"));

        return "app/find/cs_detail.app_tiles";
    }
    
    @RequestMapping(value = "/setup")
    public String setup(HttpServletRequest request, Model model) {
    	model.addAttribute("routerName", "설정");
    	return "app/find/setup.app_tiles";
    }

    @RequestMapping(value = "/near_by_station")
    public String near_by_station(HttpServletRequest request, Model model) throws Exception {
        model.addAttribute("routerName", "내 주변 충전소");

        return "app/find/near_by_station.app_tiles";
    }

    @RequestMapping(value = "/by_region_station")
    public String by_region_station(HttpServletRequest request, Model model) throws Exception {
        model.addAttribute("routerName", "지역별 충전소");

        return "app/find/by_region_station.app_tiles";
    }

    @RequestMapping(value = "/station_list")
    @ResponseBody
    public Map<String, Object> station_list(HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("customerId", NetUtils.getSession(request, "customerId"));
        param.put("userId", NetUtils.getSession(request, "userid"));
        param.put("companyId", NetUtils.getSession(request, "companyId"));

        List<Map<String, Object>> csList = appFindService.searchCsList(param);

        if (request.getParameter("gubun").equals("ME")) {
            List<Map<String, Object>> meCsList = appFindService.selectMeStationList();

            for(Map item : meCsList) {
                item.put("serviceCompanyId", "ME");
                csList.add(item);
            }
        }

        resultMap.put("list", csList);

        return resultMap;
    }

    @RequestMapping(value = "/station_detail")
    @ResponseBody
    public Map<String, Object> station_detail(HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("customerId", NetUtils.getSession(request, "customerId"));
        param.put("userId", NetUtils.getSession(request, "userid"));
        param.put("csId", request.getParameter("csId"));

        Map<String, Object> csDetail = appFindService.selectStationDetail(param);

        return csDetail;
    }

    @RequestMapping(value = "/option")
    @ResponseBody
    public Map<String, Object> option(HttpServletRequest request, Model model) throws Exception {
        HashMap<String, Object> result = new HashMap<>();

        result.put("cpTypeList", 		appFindService.searchCpType());
        result.put("connectorTypeList", appFindService.searchConnector());
        //result.put("serviceCompanyList", appFindService.searchServiceCompany());

        return result;
    }

    @RequestMapping(value = "/favorites_add")
    @ResponseBody
    public Map<String, Object> favorites_add(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String id = (String) appCustomerService.selectCustomerId(paramMap);
        paramMap.put("id", id);

        int count = appFindService.favoriteInsert(paramMap);

        int favId = appFindService.selectFavoritesId();

        if (count == 0) {
            result.put("result", "error");
            result.put("content", "저장에 실패했습니다. 관리자에게 문의하세요.");
        } else {
            result.put("result", "success");
            result.put("favId", favId);
        }

        return result;
    }

    @RequestMapping(value = "/favorites_delete")
    @ResponseBody
    public JSONObject favorites_delete(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
        JSONObject result = new JSONObject();

        int count = appFindService.favoriteDelete(paramMap);

        if (count == 0) {
            result.put("result", commonUtil.result("error", 003, "저장 실패", "저장에 실패했습니다. 관리자에게 문의하세요."));
        } else {
            result.put("result", commonUtil.result("success", 001, "저장 성공", "성공하였습니다."));
        }

        return result;
    }

    @RequestMapping(value = "/cs_detail_data")
    @ResponseBody
    public Map<String, Object> cs_detail_data(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
        HashMap<String, Object> result = new HashMap<>();

        String customerId = (String) NetUtils.getSession(request, "customerId");
        paramMap.put("customerId", customerId);

        List<Map<String, Object>> chargingHistory = appFindService.selectHistList(paramMap);

        result.put("histList", chargingHistory);

        return result;
    }

    @RequestMapping(value = "/cp_list")
    @ResponseBody
    public Map<String, Object> cp_list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String pageCount = "5";

        if (paramMap.containsKey("currPageNo")) {
            paramMap.put("pageCount" , Integer.parseInt( pageCount ) );
            paramMap.put("startRow" , Integer.parseInt( paramMap.get("currPageNo").toString() ) - 1 );
            if( !paramMap.get("currPageNo").equals("1") )
                paramMap.put("startRow", ( Integer.parseInt( paramMap.get("currPageNo").toString() ) - 1 ) *  5 );
        }

        String customerId = (String) NetUtils.getSession(request, "customerId");
        paramMap.put("customerId", customerId);

        List<Map<String, Object>> cpList = appFindService.cpConnectorStatusList(paramMap);
        int listCount = appFindService.cpConnectorStatusCount(paramMap);

        result.put("rows", cpList );
        result.put("pageInfo", commonUtil.getPageInfo(paramMap, listCount+""));

        return result;
    }

    @RequestMapping(value = "/setup_data")
    @ResponseBody
    public Map<String, Object> setup_data(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> result = new HashMap<>();

        result = appFindService.selectCustomerData(paramMap);

        return result;
    }


    @RequestMapping(value = "/update_noti_yn")
    @ResponseBody
    public JSONObject update_noti_yn(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, Model model) throws Exception {
        JSONObject result = new JSONObject();

        int count = appFindService.updateNotiYn(paramMap);

        if (count == 0) {
            result.put("result", commonUtil.result("error", 003, "저장 실패", "저장에 실패했습니다. 관리자에게 문의하세요."));
        } else {
            result.put("result", commonUtil.result("success", 001, "저장 성공", "성공하였습니다."));
        }

        return result;
    }

    @RequestMapping(value = "/near_by_station_list")
    @ResponseBody
    public Map<String, Object> near_by_station_list(HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();

        double latitude = Double.parseDouble((String) request.getParameter("lat"));
        double longitude = Double.parseDouble((String) request.getParameter("lon"));
        int distance = Integer.parseInt((String) request.getParameter("distance"));

        param.put("latitude", latitude);
        param.put("longitude", longitude);
        param.put("distance", distance);
        param.put("userId", (String) request.getParameter("userId"));
        String id = (String) appCustomerService.selectCustomerId(param);
        param.put("customerId", id);

        List<Map<String, Object>> csList = appFindService.searchCsList(param);

        resultMap.put("list", csList);

        return resultMap;
    }

    @RequestMapping(value = "/area")
    @ResponseBody
    public List<Map<String, Object>> area(HttpServletRequest request, Model model) throws Exception {

        return appFindService.selectArea();
    }

    @RequestMapping(value = "/by_region_station_list")
    @ResponseBody
    public Map<String, Object> by_region_station_list(@RequestParam(name = "zone", required = false) Integer zone,
                                                      @RequestParam(name = "searchWord", required = false) String searchWord,
                                                      HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();

        double latitude = Double.parseDouble((String) request.getParameter("lat"));
        double longitude = Double.parseDouble((String) request.getParameter("lon"));

        if (zone != null) {
            param.put("zone", zone);
        }

        if (searchWord != null) {
            param.put("searchWord", searchWord);
        }

        param.put("latitude", latitude);
        param.put("longitude", longitude);
        param.put("userId", (String) request.getParameter("userId"));
        String id = (String) appCustomerService.selectCustomerId(param);
        param.put("customerId", id);

        List<Map<String, Object>> csList = appFindService.areaCsList(param);

        resultMap.put("list", csList);

        return resultMap;
    }

    private static String formatTime(String inputTime) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HHmm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");

        Date date = inputFormat.parse(inputTime);

        return outputFormat.format(date);
    }
}
