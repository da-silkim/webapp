package com.bluenetworks.webapp.app.customer.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AppCustomerMapper {
    String selectCustomerId(Map<String, Object> resMap);
    Map<String, Object> selectDetail(String id);
    Map<String, Object> selectCustomerDetail(Map<String, Object> paramMap);
    int update( Map<String, Object> resMap );
    int updateCar( Map<String, Object> resMap );
    int passwordUpdate( Map<String, Object> resMap );
    int evCheck( Map<String, Object> resMap );
    int insertCustomerEv( Map<String, Object> resMap );
    int updateCustomerStatus( Map<String, Object> resMap );
    int updateAuthUnuse( Map<String, Object> resMap );
    int deleteCustomerCard( Map<String, Object> resMap );
    int deleteCustomerEv( Map<String, Object> resMap );
    List<Map<String , Object>> noticeList(Map<String, Object> resMap);
    Map<String, Object> noticeDetail(Map<String, Object> resMap);
    int noticeViewsUpdate(Map<String, Object> resMap);
    List<Map<String , Object>> faqList(Map<String, Object> resMap);
    List<Map<String , Object>> selectQnaList(Map<String, Object> resMap);
    int selectQnaCount( Map<String, Object> resMap );
    List<Map<String, Object>> selectQnaType(Map<String, Object> resMap);
    int insertQna( Map<String, Object> resMap );
    String selectVocIdMax();
    Map<String, Object> selectPopDetail(Map<String, Object> resMap);
    int updateQnaPop( Map<String, Object> resMap );
    int deleteQna( Map<String, Object> resMap );
    List<Map<String, Object>> searchStationList(Map<String, Object> resMap);
    List<Map<String, Object>> searchChargerList(Map<String, Object> resMap);
    int insertBrokenCharger( Map<String, Object> resMap );
    int selectPasswordCheck( Map<String, Object> resMap );
}
