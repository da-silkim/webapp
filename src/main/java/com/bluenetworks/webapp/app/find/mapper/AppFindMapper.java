package com.bluenetworks.webapp.app.find.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AppFindMapper {
    public Map<String, Object> selectServiceCompanyId(Map<String, Object> param);
    public List<Map<String, Object>> selectStationList(Map<String, Object> resMap);
    public Map<String, Object> selectStationDetail(Map<String, Object> resMap);
    public List<Map<String, Object>> searchCsList(Map<String, Object> resMap);
    public List<Map<String, Object>> selectMeStationList();
    public List<Map<String, Object>> areaCsList(Map<String, Object> resMap);
    public List<Map<String, Object>> searchCpType();
    public List<Map<String, Object>> searchConnector();
    public List<Map<String, Object>> searchServiceCompany();
    public int favoriteInsert(Map<String, Object> param);
    public int favoriteDelete(Map<String, Object> param);
    public Map<String, Object> csDetail(int csId);
    public List<Map<String, Object>> cpConnectorStatusList(Map<String, Object> param);
    public int cpConnectorStatusCount(Map<String, Object> param);
    public List<Map<String, Object>> selectHistList(Map<String, Object> param);
    public int selectCsId(String csId);
    public Map<String, Object> selectChargingStatus(int csId);
    public Map<String, Object> selectCustomerData(Map<String, Object> param);
    public int updateNotiYn(Map<String, Object> param);
    public int selectFavoritesId();
    public Map<String, Object> selectStationLocation(int id);
    public List<Map<String, Object>> selectArea();
}
