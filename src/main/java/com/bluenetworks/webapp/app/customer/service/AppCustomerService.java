package com.bluenetworks.webapp.app.customer.service;

import com.bluenetworks.webapp.app.customer.mapper.AppCustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppCustomerService {
    private final AppCustomerMapper appCustomerMapper;

    public String selectCustomerId(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.selectCustomerId(paramMap);
    }

    public Map<String, Object> selectDetail(String id) throws Exception {

        return appCustomerMapper.selectDetail(id);
    }

    public Map<String, Object> selectCustomerDetail(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.selectCustomerDetail(paramMap);
    }

    public int update(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.update(paramMap);
    }

    public int updateCar(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.updateCar(paramMap);
    }

    public int passwordUpdate(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.passwordUpdate(paramMap);
    }

    public int evCheck(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.evCheck(paramMap);
    }

    public int insertCustomerEv(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.insertCustomerEv(paramMap);
    }

    public int updateCustomerStatus(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.updateCustomerStatus(paramMap);
    }

    public int updateAuthUnuse(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.updateAuthUnuse(paramMap);
    }

    public int deleteCustomerCard(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.deleteCustomerCard(paramMap);
    }

    public int deleteCustomerEv(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.deleteCustomerEv(paramMap);
    }

    public List<Map<String, Object>> noticeList(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.noticeList(paramMap);
    }

    public Map<String, Object> noticeDetail(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.noticeDetail(paramMap);
    }

    public int noticeViewsUpdate(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.noticeViewsUpdate(paramMap);
    }

    public List<Map<String, Object>> faqList(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.faqList(paramMap);
    }

    public List<Map<String, Object>> selectQnaList(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.selectQnaList(paramMap);
    }

    public int selectQnaCount(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.selectQnaCount(paramMap);
    }

    public List<Map<String, Object>> selectQnaType(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.selectQnaType(paramMap);
    }

    public int insertQna(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.insertQna(paramMap);
    }

    public String selectVocIdMax() throws Exception {

        return appCustomerMapper.selectVocIdMax();
    }

    public Map<String, Object> selectPopDetail(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.selectPopDetail(paramMap);
    }

    public int updateQnaPop(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.updateQnaPop(paramMap);
    }

    public int deleteQna(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.deleteQna(paramMap);
    }

    public List<Map<String, Object>> searchStationList(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.searchStationList(paramMap);
    }

    public List<Map<String, Object>> searchChargerList(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.searchChargerList(paramMap);
    }

    public int insertBrokenCharger(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.insertBrokenCharger(paramMap);
    }

    public int selectPasswordCheck(Map<String, Object> paramMap) throws Exception {

        return appCustomerMapper.selectPasswordCheck(paramMap);
    }
}
