package com.eju.hookserver.controller.inner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.eju.hookserver.feign.service.api.IBkSkeletonController;
import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;
import com.eju.hookserver.mapper.dto.BkUser;
import com.eju.hookserver.service.business.bk.IBkRedisService;
import com.eju.hookserver.service.business.bk.app.skeleton.IBuildingSearchService;
import com.eju.hookserver.service.business.bk.app.skeleton.ICommunitySearchService;
import com.eju.hookserver.service.business.bk.app.skeleton.IHouseSearchService;
import com.eju.hookserver.service.business.bk.app.skeleton.IUnitSearchService;
import com.eju.houseparent.basemodel.BaseDTO;
import com.eju.houseparent.basemodel.BaseDTOCode;
import com.eju.houseparent.config.starter.BaseInnerController;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static com.eju.hookserver.common.constant.head.BkHttpHeadConstant.LIANJIA_CITY_ID;

/**
 * 贝壳骨架数据查询
 *
 * @author qiushengming
 */
@RestController
public class BkSkeletonController implements IBkSkeletonController, BaseInnerController {

    @Resource
    private ICommunitySearchService communitySearchService;

    @Resource
    private IBuildingSearchService buildingSearchService;

    @Resource
    private IUnitSearchService unitSearchService;

    @Resource
    private IHouseSearchService houseSearchService;

    @Resource
    private IBkRedisService redisService;

    @Override
    public BaseDTO<List<Map<String, String>>> communitySearch(
            String key, @SessionAttribute("phoneNo") String phoneNo, String cityId) {
        BkUser user = redisService.getUserByPhoneNo(phoneNo);
        BkRequestDto requestDto = BkRequestDto.builder()
                .user(user)
                .requestParam("key", key)
                .requestParam("city_id", cityId)
                .head(LIANJIA_CITY_ID, cityId)
                .isLoad(true)
                .build();
        BkResponseDto responseDto = communitySearchService.execute(requestDto);

        return toBaseDto(responseDto);
    }

    @Override
    public BaseDTO<List<Map<String, String>>> buildingSearch(
            String communityId, String phoneNo, String cityId) {
        BkUser user = redisService.getUserByPhoneNo(phoneNo);
        BkRequestDto requestDto = BkRequestDto.builder()
                .user(user)
                .requestParam("community_id", communityId)
                .head(LIANJIA_CITY_ID, cityId)
                .isLoad(true)
                .build();
        BkResponseDto responseDto = buildingSearchService.execute(requestDto);

        return toBaseDto(responseDto);
    }

    @Override
    public BaseDTO<List<Map<String, String>>> unitSearch(
            String buildingId, String phoneNo, String cityId) {
        BkUser user = redisService.getUserByPhoneNo(phoneNo);
        BkRequestDto requestDto = BkRequestDto.builder()
                .user(user)
                .requestParam("building_id", buildingId)
                .head(LIANJIA_CITY_ID, cityId)
                .isLoad(true)
                .build();
        BkResponseDto responseDto = unitSearchService.execute(requestDto);
        return toBaseDto(responseDto);
    }

    @Override
    public BaseDTO<List<Map<String, String>>> houseSearch(String unitId, String phoneNo, String cityId) {
        BkUser user = redisService.getUserByPhoneNo(phoneNo);
        BkRequestDto requestDto = BkRequestDto.builder()
                .user(user)
                .requestParam("unit_id", unitId)
                .head(LIANJIA_CITY_ID, cityId)
                .isLoad(true)
                .build();
        BkResponseDto responseDto = houseSearchService.execute(requestDto);
        return toBaseDto(responseDto);
    }

    private BaseDTO<List<Map<String, String>>> toBaseDto(BkResponseDto responseDto) {
        BaseDTO<List<Map<String, String>>> baseDTO = new BaseDTO<>();
        baseDTO.setMsg(responseDto.getBkErrorMsg());
        if (responseDto.getSuccess()) {
            baseDTO.setCode(BaseDTOCode.SUCCESS.getCode());
            JSONObject json = responseDto.getResult();
            List<Map<String, String>> list = ((JSONArray) json.get("list")).toJavaObject(new TypeReference<List<Map<String, String>>>() {
            });
            baseDTO.setResultVo(list);
        } else {
            baseDTO.setCode(responseDto.getBkErrorCode());
        }
        return baseDTO;
    }
}
