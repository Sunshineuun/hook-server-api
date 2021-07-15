/*
package com.eju.hookserver.controller.inner;

import com.eju.houseparent.basemodel.BaseDTO;
import com.eju.houseparent.config.starter.BaseInnerController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "执行器api", tags = "v1.0.0执行器")
@RestController
public class RunnerController implements BaseInnerController, IRunnerController {



    */
/**
     * 加载种子队列
     *
     * @param requestListVo
     * @return BaseDTO<Boolean>
     *//*

    @Override
    public BaseDTO<Boolean> loadRootSeeds(@RequestBody List<RequestVo> requestListVo) {
        List<RequestSeed> requestSeeds = TFRequestSeed.INSTANCE.to(requestListVo);
        return new BaseDTO<Boolean>().setResultVo(applicationApiService.loadSeeds(requestSeeds));
    }

    */
/**
     * 清理种子队列
     *
     * @param taskInstanceQueue
     * @return BaseDTO<Boolean>
     *//*

    @Override
    public BaseDTO<Boolean> clearRootSeeds(@PathVariable("queue") String taskInstanceQueue) {
        return new BaseDTO<Boolean>().setResultVo(applicationApiService.clearQueue(taskInstanceQueue));
    }

    */
/**
     * 查询解析树
     *
     * @param handleName
     * @return BaseDTO<List < ResponseHandleInfo>>
     *//*

    @Override
    public BaseDTO<List<String>> searchParsers(@RequestParam(value = "name", required = false) String handleName) {
        return new BaseDTO<List<String>>().setResultVo(applicationApiService.queryHandleClass(handleName));
    }


}
*/
