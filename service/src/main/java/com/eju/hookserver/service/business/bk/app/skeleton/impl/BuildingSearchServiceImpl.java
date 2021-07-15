package com.eju.hookserver.service.business.bk.app.skeleton.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eju.hookserver.common.utils.JSONUtils;
import com.eju.hookserver.common.utils.StringUtils;
import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;
import com.eju.hookserver.service.business.bk.app.BkBaseExecute;
import com.eju.hookserver.service.business.bk.app.skeleton.IBuildingSearchService;
import com.eju.houseparent.common.model.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 贝壳app端: 楼栋查询v1版本。入参，小区id；<br>
 * 面向URL:/yezhu/publish/getBuildings?community_id=5011000002700{小区id} <br>
 *
 * @author qiushengming
 */
@Service
public class BuildingSearchServiceImpl
        extends BkBaseExecute
        implements IBuildingSearchService {

    private static final String URL_TEMPLATE = "%s/yezhu/publish/getBuildings?community_id=%s";
    private static final String COMMUNITY_ID = "community_id";

    @Override
    protected void buildingUrl(BkRequestDto requestDto) {
        Map<String, String> requestParam = requestDto.getRequestParam();
        if (requestParam.containsKey(COMMUNITY_ID)) {
            String communityId = requestParam.get(COMMUNITY_ID).trim();
            if (StringUtils.isNotBlank(communityId)) {
                String url = String.format(URL_TEMPLATE, DOMAIN_NAME, communityId);
                requestDto.setUrl(url);
            } else {
                throw new BusinessException("小区ID为空");
            }
        }
    }

    @Override
    protected void parser(BkRequestDto requestDto, BkResponseDto responseDto) {
        JSONObject var0 = JSONObject.parseObject(requestDto.getResponseStr());
        JSONArray list = JSONUtils.getJsonArrayByKey(var0, "data.list");

        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        list.forEach(o -> {
            JSONObject var = (JSONObject) o;
            String buildingId = var.getString("building_id");
            String buildingName = var.getString("building_name");
            JSONObject jsonVar = new JSONObject();
            jsonVar.put("building_name", buildingName);
            jsonVar.put("building_id", buildingId);
            array.add(jsonVar);
        });
        result.put("list", array);
        responseDto.setResult(result);
    }
}
