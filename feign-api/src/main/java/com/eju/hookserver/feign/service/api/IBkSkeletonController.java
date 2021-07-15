package com.eju.hookserver.feign.service.api;

import com.eju.houseparent.basemodel.BaseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Map;

/**
 * 贝壳骨架数据查询
 *
 * @author qiushengming
 */
public interface IBkSkeletonController {
    String PREFIX = "/bk/skeleton/";

    /**
     * 通过关键字查询小区列表 <br>
     *
     * @param key     小区关键字
     * @param phoneNo 手机号码
     * @param cityId  城市6位编码
     * @return 返回小区列表
     */
    @GetMapping(PREFIX + "community")
    BaseDTO<List<Map<String, String>>> communitySearch(
            String key,
            @SessionAttribute("phoneNo") String phoneNo,
            String cityId);

    /**
     * 通过小区ID查询楼栋列表
     *
     * @param communityId 小区id
     * @param phoneNo     手机号码
     * @param cityId      城市6位编码
     * @return 返回楼栋列表
     */
    @GetMapping(PREFIX + "building")
    BaseDTO<List<Map<String, String>>> buildingSearch(
            String communityId,
            @SessionAttribute("phoneNo") String phoneNo,
            String cityId);

    /**
     * 通过楼栋ID查询单元列表
     *
     * @param buildingId 楼栋id
     * @param phoneNo    手机号码
     * @param cityId     城市6位编码
     * @return 单元列表
     */
    @GetMapping(PREFIX + "unit")
    BaseDTO<List<Map<String, String>>> unitSearch(
            String buildingId,
            @SessionAttribute("phoneNo") String phoneNo,
            String cityId);

    /**
     * 通过单元列表获取房号列表
     *
     * @param unitId  单元列表
     * @param phoneNo 手机号码
     * @param cityId  城市6位编码
     * @return 房号列表
     */
    @GetMapping(PREFIX + "house")
    BaseDTO<List<Map<String, String>>> houseSearch(
            String unitId,
            @SessionAttribute("phoneNo") String phoneNo,
            String cityId);
}
