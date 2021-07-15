package com.eju.hookserver.test;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: Nj
 * @Date: 2020/10/22 2:54 下午
 */
@Slf4j
public class TestMain {


    public static void main(String[] args) throws Exception {


//        while (--tryCount >0){
//            System.out.println(tryCount);
//        }
//        LinkedHashMap object = new LinkedHashMap();
//        object.put("house_unique_id", 153195);
//        object.put("city", "ab");
//        object.put("hid", 132324);
//        object.put("source", 22);
//        object.put("type", 1);
//        object.put("log_index_site", "ab");
//        object.put("log_index_hid", 132324);
//        object.put("change", "insert");
//        object.put("timestamp",1621221796);
//        object.put("nonce",",OJqLd");
//        System.out.println(JSONObject.toJSONString(object) + "lejuhouse20210511" + "1621221796" + ",OJqLd");
//        System.out.println(MD5.doMd5(JSONObject.toJSONString(object) + "lejuhouse20210511" + "1621221796" + ",OJqLd","UTF-8"));
//
//
//        boolean tmp = MD5.verifySign(JSONObject.toJSONString(object) + "lejuhouse20210511" + "1621221796" + ",OJqLd", "431d1c1f58028c82d19f381f029202b7");
//        System.out.println(tmp);
//        Date curTime = new Date();
//        CronSequenceGenerator expression;
//        try {
//            expression = new CronSequenceGenerator("0 42 16 * * ?");
//            Date newDate = expression.next(curTime);
//            System.out.println(newDate);
//
//            System.out.println((newDate.getTime() - curTime.getTime()) / 1000) ;
//        }  catch (Exception e) {
//            log.error("fail to update rule nextTime", e);
//        }
//        //测试
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE,10);
//        long day = betweenDays(new Date(),calendar.getTime());
//        //差10天
//        System.out.println(day);
//        //差2天
//        System.out.println(betweenDays("2020-10-29 12:12:12","2020-10-31 12:12:12"));
    }


    public static final long betweenDays(Date date1, Date date2) {
        // 获取相差的天数
        return subDay(date1, date2);
    }

    public static final long betweenDays(String dateStr1, String dateStr2) throws ParseException {
        // 格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse(dateStr1);
        Date date2 = sdf.parse(dateStr2);
        // 获取相差的天数
        return subDay(date1, date2);
    }

    /**
     * 一天的毫秒数
     */
    private static final long ONE_DAY_MILLIS = 1000L * 3600L * 24L;

    /**
     * 计算差天：date2-date1
     *
     * @param date1
     * @param date2
     * @return
     */
    private static long subDay(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        long timeInMillis1 = calendar.getTimeInMillis();
        calendar.setTime(date2);
        long timeInMillis2 = calendar.getTimeInMillis();

        long betweenDays = (timeInMillis2 - timeInMillis1) / ONE_DAY_MILLIS;

        return betweenDays;
    }

}

