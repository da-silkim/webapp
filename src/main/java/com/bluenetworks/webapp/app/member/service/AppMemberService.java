package com.bluenetworks.webapp.app.member.service;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.bluenetworks.webapp.app.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppMemberService {

	private final MemberMapper memberMapper;
	
	public boolean checkCi(String userci) {
        boolean is_ok = false;

        JSONObject param = new JSONObject();
        param.put("userci", userci);
        param.put("status", 0);
        JSONObject info = memberMapper.SELECT_COM_CUSTOMER(param);

        if(info == null || info.isEmpty()){
            is_ok = true;
        }

        return is_ok;
    }
	
	public JSONObject SELECT_USER_CI(String userci) {
        JSONObject param = new JSONObject();
        param.put("userci", userci);
        param.put("status", 0);
        JSONObject info = memberMapper.SELECT_COM_CUSTOMER(param);
        return info;
    }
	
	//아이디 중복확인
	public boolean checkId(String userid) {
        boolean is_ok = false;

        JSONObject param = new JSONObject();
        param.put("userid", userid);
        JSONObject info = memberMapper.SELECT_COM_CUSTOMER(param);

        if(info == null || info.isEmpty()){
            is_ok = true;
        }

        return is_ok;
    }
	
	public int insertUser(JSONObject param) {

        return memberMapper.INSERT_COM_CUSTOMER(param);
    }

    public int insertUserAuth(JSONObject param) throws Exception {

        return memberMapper.insertCustomerAuth(param);
    }

    public int insertCustomerEv(JSONObject param) throws Exception {

        return memberMapper.insertCustomerEv(param);
    }

    public int userAuthCheck(String idToken) throws Exception {

        return memberMapper.userAuthCheck(idToken);
    }

    public String selectIdMax() throws Exception {

        return memberMapper.selectIdMax();
    }
    public int insertCustomerMemberInfoHist(JSONObject param) throws Exception {

        return memberMapper.insertCustomerHist(param);
    }
	
	public JSONObject selectWithDrawl(JSONObject param) {
		
		return memberMapper.SELECT_WITHDRAWL_USER(param);
	}


    public int idCheck(Map<String, Object> paramMap) throws Exception {

        return memberMapper.idCheck(paramMap);
    }

    public Map<String, Object> getIdAndPw(Map<String, Object> paramMap) throws Exception {

        return memberMapper.getIdAndPw(paramMap);
    }

    public int passwordUpdate(Map<String, Object> paramMap) throws Exception {

        return memberMapper.passwordUpdate(paramMap);
    }

    public int selectCarNumberCheck(String carNo) throws Exception {

        return memberMapper.selectCarNumberCheck(carNo);
    }
}
