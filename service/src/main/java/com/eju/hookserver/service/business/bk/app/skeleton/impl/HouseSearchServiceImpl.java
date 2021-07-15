package com.eju.hookserver.service.business.bk.app.skeleton.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eju.hookserver.common.utils.JSONUtils;
import com.eju.hookserver.common.utils.StringUtils;
import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;
import com.eju.hookserver.service.business.bk.app.BkBaseExecute;
import com.eju.hookserver.service.business.bk.app.skeleton.IHouseSearchService;
import com.eju.houseparent.common.model.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 贝壳app端：房号查询v1版本. 入参：单元id <br>
 * 面向URL:/yezhu/publish/getHouses?unit_id=5013000243156{单元id} <br>
 *
 * @author qiushengming
 */
@Service
public class HouseSearchServiceImpl
        extends BkBaseExecute
        implements IHouseSearchService {

    private static final String URL_TEMPLATE = "%s/yezhu/publish/getHouses?unit_id=%s";
    private static final String UNIT_ID = "unit_id";

    @Override
    protected void buildingUrl(BkRequestDto requestDto) {
        Map<String, String> requestParam = requestDto.getRequestParam();
        if (requestParam.containsKey(UNIT_ID)) {
            String unitId = requestParam.get(UNIT_ID).trim();
            if (StringUtils.isNotBlank(unitId)) {
                String url = String.format(URL_TEMPLATE, DOMAIN_NAME, unitId);
                requestDto.setUrl(url);
            } else {
                throw new BusinessException("单元ID为空");
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
            String id = var.getString("house_id");
            String name = var.getString("house_name");
            JSONObject jsonVar = new JSONObject();
            jsonVar.put("house_name", name);
            jsonVar.put("house_id", id);
            array.add(jsonVar);
        });
        result.put("list", array);
        responseDto.setResult(result);
    }
}
