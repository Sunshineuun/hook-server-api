package com.eju.hookserver.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName DateUtil
 * @Description 时间操作工具类
 * @Author wujiangming
 * @Date 2020/11/4 18:09
 */
public class DateUtil {

    public static final String LAST_DAY = "L";
    public static final String SDF_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String SDF_YYYY_MM = "yyyy-MM";
    public static final String SDF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String SDF_YYYY_MM_DD_HH_00_00 = "yyyy-MM-dd HH:00:00";

    /**
     * date日期转string
     **/
    public static String getDateToString(Date date, String sdf) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat sdformat = new SimpleDateFormat(sdf);
        return sdformat.format(date);
    }

    /**
     * @Description: 比较两个时间大小，beginTime<endTime 返回true
     **/
    public static boolean dateCompare(String beginTime, String endTime, String sdf) {
        if (StringUtils.isBlank(beginTime)) {
            return true;
        }
        if (StringUtils.isBlank(endTime)) {
            return false;
        }
        boolean flag = false;
        Date sd1 = stringToDate(beginTime, sdf);
        Date sd2 = stringToDate(endTime, sdf);
        if (sd1.before(sd2)) {
            flag = true;
        }
        return flag;
    }

    /**
     * @Description: 两个时间相差月份
     **/
    public static Integer differenceMonth(String startDate, String endDate, String sdf) {
        Date fromDate = stringToDate(startDate, sdf);
        Date toDate = stringToDate(endDate, sdf);
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);
        //只要年月
        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);
        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);
        int month = toYear * 12 + toMonth - (fromYear * 12 + fromMonth);
        return month;
    }

    /**
     * 计算日期的上一个月
     **/
    public static String getPrevMonthDate(Date date, int n, String sdf) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - n);
        return new SimpleDateFormat(sdf).format(calendar.getTime());
    }

    /**
     * string 时间转date格式
     **/
    public static Date stringToDate(String date, String sdf) {
        SimpleDateFormat df = new SimpleDateFormat(sdf);
        Date newDate = null;
        try {
            newDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }


    /**
     * 计算两个日期
     **/
    public static Integer getDifferMonthCount(Date dateFrom, Date dateTo) {
        Calendar from = Calendar.getInstance();
        from.setTime(dateFrom);
        Calendar to = Calendar.getInstance();
        to.setTime(dateTo);
        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);
        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);
        int month = toYear * 12 + toMonth - (fromYear * 12 + fromMonth);
        return month;
    }

    /**
     * String类型时间转string类型时间，从sdf1格式转成sdf2格式
     **/
    public static String stringToString(String date, String sdf1, String sdf2) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(sdf1);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(sdf2);
        Date d = null;
        try {
            d = simpleDateFormat1.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDateFormat2.format(d);
    }

    public final static Date nextCron(String cron, Date date) {
        if (cron.contains(LAST_DAY)) {
            cron = cron.replace(LAST_DAY, "28");
            Date tmpData = new CronSequenceGenerator(cron).next(date);
            final Calendar cal = Calendar.getInstance();
            cal.setTime(tmpData);
            final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, last);
            return cal.getTime();
        }
        return new CronSequenceGenerator(cron).next(date);
    }

    public static void main(String[] args) {
        System.out.println(dateCompare("", "2021-05-07 12:00:00", SDF_YYYY_MM_DD_HH_MM_SS));
        System.out.println(UUID.randomUUID().toString());

    }
}