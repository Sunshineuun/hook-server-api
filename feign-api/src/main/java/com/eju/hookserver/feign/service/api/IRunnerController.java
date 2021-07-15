/*
package com.eju.hookserver.feign.service.api;


import com.eju.houseparent.basemodel.BaseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

*/
/**
 * @author zcm
 * ##不要写@RestController(防止客户端url路径重复)
 * @Description:
 *//*


public interface IRunnerController {

    String prefix = "/runner";

    */
/**
     * 加载种子队列
     *
     * @param requestListVo
     * @return BaseDTO<Boolean>
     *//*

    @PostMapping(prefix + "/load/seed")
    BaseDTO<Boolean> loadRootSeeds(@RequestBody List<RequestVo> requestListVo);

    */
/**
     * 清理种子队列
     *
     * @param queue
     * @return BaseDTO<Boolean>
     *//*

    @GetMapping(prefix + "/clear/seed/{queue}")
    BaseDTO<Boolean> clearRootSeeds(@PathVariable("queue") String queue);


    */
/**
     * 查询解析树
     *
     * @param handleName
     * @return BaseDTO<List < ResponseHandleInfo>>
     *//*

    @PostMapping(prefix + "/search/parser")
    BaseDTO<List<String>> searchParsers(@RequestParam(value = "name", required = false) String handleName);


}
*/
