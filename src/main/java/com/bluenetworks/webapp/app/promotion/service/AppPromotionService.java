package com.bluenetworks.webapp.app.promotion.service;

import com.bluenetworks.webapp.app.charge.mapper.AppChargeMapper;
import com.bluenetworks.webapp.app.promotion.controller.AppPromotionController;
import com.bluenetworks.webapp.app.promotion.mapper.AppPromotionMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppPromotionService {

    private final AppPromotionMapper appPromotionMapper;

    public List<Map<String, Object>> selectPromotionList(Map<String, Object> param) throws Exception {

        return appPromotionMapper.selectPromotionList(param);
    }

    public Map<String, Object> selectCustomerDetail(Map<String, Object> param) throws Exception {

        return appPromotionMapper.selectCustomerDetail(param);
    }

    public int authCdCheck(Map<String, Object> param) throws Exception {

        return appPromotionMapper.authCdCheck(param);
    }

    public int insertCustomerPromotion(Map<String, Object> param) throws Exception {

        return appPromotionMapper.insertCustomerPromotion(param);
    }

    public int updateCustomerPromotion(Map<String, Object> param) throws Exception {

        return appPromotionMapper.updateCustomerPromotion(param);
    }
}
