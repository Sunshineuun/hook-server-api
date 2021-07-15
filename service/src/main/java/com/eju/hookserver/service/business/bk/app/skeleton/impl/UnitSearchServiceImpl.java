package com.eju.hookserver.service.business.bk.app.skeleton.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eju.hookserver.common.utils.JSONUtils;
import com.eju.hookserver.common.utils.StringUtils;
import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;
import com.eju.hookserver.service.business.bk.app.BkBaseExecute;
import com.eju.hookserver.service.business.bk.app.skeleton.IUnitSearchService;
import com.eju.houseparent.common.model.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 贝壳app端：单元查询v1版本. 入参：楼栋id <br>
 * 面向URL:/yezhu/publish/getUnits?building_id=5012000243156{楼栋ID} <br>
 *
 * @author qiushengming
 */
@Service
public class UnitSearchServiceImpl
        extends BkBaseExecute
        implements IUnitSearchService {

    private static final String URL_TEMPLATE = "%s/yezhu/publish/getUnits?building_id=%s";
    private static final String BUILDING_ID = "building_id";

    @Override
    protected void buildingUrl(BkRequestDto requestDto) {
        Map<String, String> requestParam = requestDto.getRequestParam();
        if (requestParam.containsKey(BUILDING_ID)) {
            String buildingId = requestParam.get(BUILDING_ID).trim();
            if (StringUtils.isNotBlank(buildingId)) {
                String url = String.format(URL_TEMPLATE, DOMAIN_NAME, buildingId);
                requestDto.setUrl(url);
            } else {
                throw new BusinessException("楼栋ID为空");
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
            String id = var.getString("unit_id");
            String name = var.getString("unit_name");
            JSONObject jsonVar = new JSONObject();
            jsonVar.put("unit_name", name);
            jsonVar.put("unit_id", id);
            array.add(jsonVar);
        });
        result.put("list", array);
        responseDto.setResult(result);
    }
}
