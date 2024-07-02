package com.bluenetworks.webapp.app.main.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.bluenetworks.webapp.app.main.domain.NoticeVo;

@Mapper
public interface AppMainMapper {

	List<Map<String, Object>> selectNoticeList(Map<String, Object> param);
	List<Map<String, Object>> selectPeriStationList(Map<String, Object> param);
	
	List<Map<String, Object>> selectMyStationList(Map<String, Object> param);
	
	Map<String, Object> selectPaymentInfo(Map<String, Object> param);

	List<Map<String, Object>> selectMonthDashboard(Map<String, Object> param);

}
