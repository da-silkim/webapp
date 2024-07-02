package com.bluenetworks.webapp.app.find.service;

import com.bluenetworks.webapp.app.find.mapper.AppFindMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppFindService {
    private final AppFindMapper appFindMapper;

    public List<Map<String, Object>> selectStationList(Map<String, Object> param) throws Exception {

        return appFindMapper.selectStationList(param);
    }

    public Map<String, Object> selectStationDetail(Map<String, Object> param) throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 현재 분 가져오기
        int month = currentDateTime.getMonthValue();
        int priceType = 0;

        if (month == 3 || month == 4 || month == 5 || month == 9 || month == 10) {
            priceType = 1;
        } else if (month == 6 || month == 7 || month == 8) {
            priceType = 2;
        } else if (month == 11 || month == 12 || month == 1 || month == 2) {
            priceType = 3;
        }

        Map<String, Object> companyDetail = appFindMapper.selectServiceCompanyId(param);

        List<Map<String, Object>> conectorList = appFindMapper.searchConnector();
        param.put("connectorList", conectorList);

        param.put("priceType", priceType);
        param.put("serviceCompanyId", 1);
        param.put("pricePolicy", companyDetail.get("pricePolicy"));

        return appFindMapper.selectStationDetail(param);
    }

    public List<Map<String, Object>> searchCsList(Map<String, Object> param) throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 현재 분 가져오기
        int month = currentDateTime.getMonthValue();
        int priceType = 0;

        if (month == 3 || month == 4 || month == 5 || month == 9 || month == 10) {
            priceType = 1;
        } else if (month == 6 || month == 7 || month == 8) {
            priceType = 2;
        } else if (month == 11 || month == 12 || month == 1 || month == 2) {
            priceType = 3;
        }


        Map<String, Object> companyDetail = appFindMapper.selectServiceCompanyId(param);

        List<Map<String, Object>> conectorList = appFindMapper.searchConnector();
        param.put("connectorList", conectorList);

        param.put("priceType", priceType);
        param.put("serviceCompanyId", 1);
        param.put("pricePolicy", companyDetail.get("pricePolicy"));

        return appFindMapper.searchCsList(param);
    }

    public List<Map<String, Object>> selectMeStationList() throws Exception {


        return appFindMapper.selectMeStationList();
    }

    public List<Map<String, Object>> areaCsList(Map<String, Object> param) throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 현재 분 가져오기
        int month = currentDateTime.getMonthValue();
        int priceType = 0;

        if (month == 3 || month == 4 || month == 5 || month == 9 || month == 10) {
            priceType = 1;
        } else if (month == 6 || month == 7 || month == 8) {
            priceType = 2;
        } else if (month == 11 || month == 12 || month == 1 || month == 2) {
            priceType = 3;
        }

        Map<String, Object> companyDetail = appFindMapper.selectServiceCompanyId(param);

        List<Map<String, Object>> conectorList = appFindMapper.searchConnector();
        param.put("connectorList", conectorList);

        param.put("priceType", priceType);
        param.put("serviceCompanyId", 1);
        param.put("pricePolicy", companyDetail.get("pricePolicy"));

        return appFindMapper.areaCsList(param);
    }

    public List<Map<String, Object>> searchCpType() throws Exception {

        return appFindMapper.searchCpType();
    }

    public List<Map<String, Object>> searchConnector() throws Exception {

        return appFindMapper.searchConnector();
    }

    public List<Map<String, Object>> searchServiceCompany() throws Exception {

        return appFindMapper.searchServiceCompany();
    }

    public int favoriteInsert(Map<String, Object> param) throws Exception {

        return appFindMapper.favoriteInsert(param);
    }

    public int favoriteDelete(Map<String, Object> param) throws Exception {

        return appFindMapper.favoriteDelete(param);
    }

    public Map<String, Object> csDetail(int csId) throws Exception {

        return appFindMapper.csDetail(csId);
    }

    public List<Map<String, Object>> cpConnectorStatusList(Map<String, Object> param) throws Exception {

        return appFindMapper.cpConnectorStatusList(param);
    }

    public int cpConnectorStatusCount(Map<String, Object> param) throws Exception {

        return appFindMapper.cpConnectorStatusCount(param);
    }

    public List<Map<String, Object>> selectHistList(Map<String, Object> param) throws Exception {

        return appFindMapper.selectHistList(param);
    }

    public int selectCsId(String csId) throws Exception {

        return appFindMapper.selectCsId(csId);
    }

    public Map<String, Object> selectChargingStatus(int csId) throws Exception {

        return appFindMapper.selectChargingStatus(csId);
    }

    public Map<String, Object> selectCustomerData(Map<String, Object> param) throws Exception {

        return appFindMapper.selectCustomerData(param);
    }

    public int updateNotiYn(Map<String, Object> param) throws Exception {

        return appFindMapper.updateNotiYn(param);
    }

    public int selectFavoritesId() throws Exception {

        return appFindMapper.selectFavoritesId();
    }

    public Map<String, Object> selectStationLocation(int id) throws Exception {

        return appFindMapper.selectStationLocation(id);
    }

    public List<Map<String, Object>> selectArea() throws Exception {

        return appFindMapper.selectArea();
    }
}
