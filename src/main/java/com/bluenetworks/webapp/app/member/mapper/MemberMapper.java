package com.bluenetworks.webapp.app.member.mapper;

import java.util.List;
import java.util.Map;

import com.bluenetworks.webapp.app.member.model.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.json.simple.JSONObject;

@Mapper
public interface MemberMapper {
    JSONObject SELECT_COM_CUSTOMER(JSONObject param);
    int INSERT_COM_CUSTOMER(JSONObject param);
    JSONObject SELECT_WITHDRAWL_USER(JSONObject param);
    int insertCustomerAuth(JSONObject param) throws Exception;
    int insertCustomerEv(JSONObject param);
    int userAuthCheck(String idToken) throws Exception;
    String selectIdMax();
    int insertCustomerHist(JSONObject param) throws Exception;
    int idCheck(Map<String, Object> resMap) throws Exception;
    Map<String, Object> getIdAndPw(Map<String, Object> resMap);
    int passwordUpdate( Map<String, Object> resMap );
    int selectCarNumberCheck( String carNo );
}
