package com.eju.hookserver.mapper;

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


       /*
        tb_dictionary	配置数据库信息
        tb_instance_config	实例任务信息
        tb_instance_log	实例任务日志
        tb_instance_monitor	实例任务监控
        tb_role	角色信息
        tb_task_config	配置任务信息
        tb_url_parse_config	配置url解析器
        tb_user	用户信息
        tb_user_role
        * */
        Map<String,String> map = new LinkedHashMap<>();

        map.put("tb_task_config","任务配置");
        map.put("tb_url_parse_config","url解析配置");
        map.put("tb_dictionary","字典配置");
        map.put("tb_role","角色信息");
        map.put("tb_instance_config","实例配置");
        map.put("tb_instance_log","实例日志");
        map.put("tb_instance_monitor","实例监控");
        map.put("tb_user","	用户信息");
        map.put("tb_user_role","用户角色");



        //describeMethod(map);
        generatorXml(map);
        //generatorTableDLL(map);
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

    private static void generatorTableDLL(Map<String, String> describeInfo){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,String> entry :describeInfo.entrySet()){
            sb.append(String.format("CREATE TABLE `%s` (\n" +
                "  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `version` int(10) DEFAULT '0' COMMENT '版本号 默认0 ',\n" +
                "  `is_del` int(1) DEFAULT '0' COMMENT '逻辑删除标志 默认0 未删除 ,1 已删除',\n" +
                "  `name` varchar(100) DEFAULT NULL COMMENT '名称',\n" +
                "  `describe` varchar(255) DEFAULT NULL COMMENT '描述',\n" +
                "  `remark` varchar(255) DEFAULT NULL COMMENT '备注',\n" +
                "  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',\n" +
                "  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',\n" +
                "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='%s';",entry.getKey().trim(),entry.getValue()));
        }

        System.out.println(sb.toString());

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
