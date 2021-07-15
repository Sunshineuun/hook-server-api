package com.eju.hookserver.test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Created by zcm on 2018/8/8.
 * @version v0.1.0
 * @see <font color="#0000FF">house-houseasset</font>
 */
public class GenerateDescribeMethod {

    public static void main(String[] args) {

        /*select TABLE_NAME,TABLE_COMMENT from  information_schema.`TABLES`
        where  table_schema ='库名' */


       /* building                          楼栋信息表
        building_area                     楼栋面积
        community                         小区基础信息
        community_addr                    小区出入口地址信息
        community_environment_sd          小区每日环境
        community_picture                 小区图片信息
        house_layout                      户型信息
        price_block_sm                    板块每月价格
        price_city_sm                     城市每月价格
        price_community_sm                小区每月价格
        price_district_sm                 区域每月价格
        */
        Map<String,String> map = new LinkedHashMap<>();

        map.put("block", "城市代码表");
        map.put("build_type_lkp", "建筑类型代码表");
        map.put("building", "楼栋信息表");
        map.put("building_layout_mapping", "楼栋户型mapping");
        map.put("building_pre_sale_permit", "楼栋预售许可证信息");
        map.put("city", "城市代码表");
        map.put("comm_belong_lkp", "交易权属代码表");
        map.put("community", "小区基础信息表");
        map.put("community_addr", "小区地址");
        map.put("community_alias", "小区别名信息");
        map.put("community_assort", "小区配套表");
        map.put("community_assort_coor", "小区配套坐标表");
        map.put("community_detail_score_end", "小区详情得分表");
        map.put("community_environment_sd", "小区每日环境");
        map.put("community_gate_addr", "小区出入口");
        map.put("community_level", "小区等级");
        map.put("community_picture", "小区图片信息");
        map.put("community_score_end", "小区总分表");
        map.put("community_tag", "小区标签信息");
        map.put("community_tag_detail", "小区标签明细");
        map.put("district", "区域代码表");
        map.put("facade_style_lkp", "外立面风格代码表");
        map.put("house_layout_std", "户型信息表");
        map.put("price_block_sm", "板块每月价格");
        map.put("price_city_sm", "城市每月价格");
        map.put("price_community_deal_sm", "小区成交价格");
        map.put("price_community_list_sm", "小区挂牌价格");
        map.put("price_community_rent_sm", "小区租金价格");
        map.put("price_community_sm", "小区价格");
        map.put("price_district_sm", "区域每月价格");
        map.put("property_type_lkp", "物业类型代码表");
        map.put("province", "省份代码表");
        map.put("room", "室户信息表");
        map.put("source_lkp", "渠道来源代码表");


        //describeMethod(map);
        generatorXml(map);
    }

    private static void describeMethod(Map<String, String> describeInfo) {
        String methodInfo = "    @ApiOperation(value = \"%s\",response = %s.class)\n" +
                "    @GetMapping(value = \"%s\")\n" +
                "    public Object %s(){\n" +
                "        return true;\n" +
                "    }";
        for (String key:describeInfo.keySet()) {
            String keyF = formatFieldTable(key);
            System.out.println(String.format(methodInfo,describeInfo.get(key),keyF,keyF,keyF,keyF,keyF));
        }
    }

    private static void generatorXml(Map<String, String> describeInfo){
        String xmlStr =  "<table tableName=\"%s\"\n" +
                "        enableCountByExample=\"false\"\n" +
                "        enableUpdateByExample=\"false\"\n" +
                "        enableDeleteByExample=\"false\"\n" +
                "        enableSelectByExample=\"false\"\n" +
                "        selectByExampleQueryId=\"false\"\n" +
                "        enableSelectByPrimaryKey=\"false\"\n" +
                "        enableUpdateByPrimaryKey=\"false\"\n" +
                "        enableDeleteByPrimaryKey=\"false\">\n" +
                "        <generatedKey column=\"id\" sqlStatement=\"Mysql\" identity=\"true\" />\n" +
                "        </table>";
        for (String key:describeInfo.keySet()) {
            System.out.println(String.format(xmlStr,key));
        }
    }


    public static String formatFieldTable(String table) {
        String [] strs = table.split("_");
        StringBuilder sb  = new StringBuilder();
        for (String str : strs) {
            sb.append(str.substring(0,1).toUpperCase()+str.substring(1));
        }

        return sb.toString();
    }
}
