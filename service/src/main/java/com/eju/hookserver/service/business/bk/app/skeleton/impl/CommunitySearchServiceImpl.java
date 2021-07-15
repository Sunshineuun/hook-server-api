package com.eju.hookserver.service.business.bk.app.skeleton.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eju.hookserver.common.utils.JSONUtils;
import com.eju.hookserver.common.utils.StringUtils;
import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;
import com.eju.hookserver.service.business.bk.app.BkBaseExecute;
import com.eju.hookserver.service.business.bk.app.skeleton.ICommunitySearchService;
import com.eju.houseparent.common.model.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 贝壳app端：小区查询v1版本. <br>
 * 面向URL:%s/house/suggestion/index?city_id={城市编码}&query={关键字}&channel_id=xiaoqu <br>
 * 面向URL:%s/yezhu/publish/getCommunities?city_id=%s&query=%S&hide_yj=0
 *
 * @author qiushengming
 */
@Service
public class CommunitySearchServiceImpl
        extends BkBaseExecute
        implements ICommunitySearchService {

    private static final String URL_TEMPLATE = "%s/yezhu/publish/getCommunities?city_id=%s&query=%S&hide_yj=0";
    private static final String KEY = "key";

    @Override
    protected void buildingUrl(BkRequestDto requestDto) {
        Map<String, String> requestParam = requestDto.getRequestParam();
        if (requestParam.containsKey(KEY)) {
            String key = requestParam.get(KEY).trim();
            if (StringUtils.isNotBlank(key)) {
                String url = String.format(URL_TEMPLATE, DOMAIN_NAME, requestParam.get("city_id"), key);
                requestDto.setUrl(url);
            } else {
                throw new BusinessException("检索关键字为空，请重新输入.");
            }
        }
    }

    @Override
    protected void parser(BkRequestDto requestDto, BkResponseDto responseDto) {
        JSONObject var0 = JSONObject.parseObject(requestDto.getResponseStr());
        JSONArray items = JSONUtils.getJsonArrayByKey(var0, "data.list");

        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        items.forEach(o -> {
            if (o instanceof JSONObject) {
                JSONObject var = (JSONObject) o;
                String communityId = var.getString("community_id");
                String communityName = var.getString("community_name");
                String communityAlias = var.getString("community_alias");
                JSONObject jsonVar = new JSONObject();
                jsonVar.put("community_name", communityName);
                jsonVar.put("community_Id", communityId);
                jsonVar.put("community_alias", communityAlias);
                array.add(jsonVar);
            }
        });
        result.put("list", array);
        responseDto.setResult(result);
    }
}
