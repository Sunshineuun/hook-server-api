package com.eju.hookserver.test.base;

/**
 * Created by zcm
 */

import com.eju.hookserver.Application;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


/**
 * baseTest
 *
 * @author zcm
 * @since 2018-08-21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
//@Transactional(transactionManager = "transactionManager")
//@Rollback
public abstract class BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    /*
        MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.get("/xxxController/xxx_query")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .param("xxx","xxx")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andReturn();
        System.out.println(result);
    */
    /**
     * 引入 ContiPerf 进行性能测试
     * 使用@Rule注释激活ContiPerf
     * 通过@Test指定测试方法
     * @PerfTest指定调用次数和线程数量
     * @Required指定性能要求（每次执行的最长时间，平均时间，总时间等）
     *
     * PerfTest参数
     * @PerfTest(invocations = 300)：执行300次，和线程数量无关，默认值为1，表示执行1次；
     * @PerfTest(threads=30)：并发执行30个线程，默认值为1个线程；
     * @PerfTest(duration = 20000)：重复地执行测试至少执行20s。
     *
     * Required参数
     * @Required(throughput = 20)：要求每秒至少执行20个测试；
     * @Required(average = 50)：要求平均执行时间不超过50ms；
     * @Required(median = 45)：要求所有执行的50%不超过45ms；
     * @Required(max = 2000)：要求没有测试超过2s；
     * @Required(totalTime = 5000)：要求总的执行时间不超过5s；
     * @Required(percentile90 = 3000)：要求90%的测试不超过3s；
     * @Required(percentile95 = 5000)：要求95%的测试不超过5s；
     * @Required(percentile99 = 10000)：要求99%的测试不超过10s;
     * @Required(percentiles = “66:200,96:500”)：要求66%的测试不超过200ms，96%的测试不超过500ms。
     */
    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();


}
