package com.bluenetworks.webapp.app.promotion.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

@Mapper
public interface AppPromotionMapper {
    public List<Map<String, Object>> selectPromotionList(Map<String, Object> resMap);
    public Map<String, Object> selectCustomerDetail(Map<String, Object> resMap);
    public int authCdCheck(Map<String, Object> resMap);
    public int insertCustomerPromotion(Map<String, Object> resMap);
    public int updateCustomerPromotion(Map<String, Object> resMap);
}
