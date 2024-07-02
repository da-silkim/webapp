package com.bluenetworks.webapp.app.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bluenetworks.webapp.app.main.domain.NoticeVo;
import com.bluenetworks.webapp.app.main.mapper.AppMainMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AppMainService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final AppMainMapper appMainMapper;
	
	public List<Map<String, Object>> selectNoticeList(Map<String, Object> param) throws Exception {
		
		List<Map<String, Object>> resultList = appMainMapper.selectNoticeList(param);
		return resultList;
	}
	public List<Map<String, Object>> selectPeriStationList(Map<String, Object> param) throws Exception {
		
		List<Map<String, Object>> resultList = appMainMapper.selectPeriStationList(param);
		return resultList;
	}
	
	public List<Map<String, Object>> selectMyStationList(Map<String, Object> param) throws Exception {
		
		List<Map<String, Object>> resultList = appMainMapper.selectMyStationList(param);
		return resultList;
	}
	
	public Map<String, Object> selectPaymentInfo(Map<String, Object> param) throws Exception {
		return appMainMapper.selectPaymentInfo(param);
	}

	public List<Map<String, Object>> selectMonthDashboard(Map<String, Object> param) throws Exception {
		return appMainMapper.selectMonthDashboard(param);
	}
}
