package com.tarena.lbstest.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by baidu on 17/1/23.
 */

public class CommonUtil {

    private static DecimalFormat df = new DecimalFormat("######0.00");

    public static final double DISTANCE = 0.0001;

    /**
     * 获取当前时间戳(单位：秒)
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 校验double数值是否为0
     *
     * @param value
     *
     * @return
     */
    /**
     * 经纬度是否为(0,0)点
     *
     * @return
     */
/**
     * 将字符串转为时间戳
     */
    public static long toTimeStamp(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date date;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime() / 1000;
    }
//    /**
//     * 获取年月日 时分秒
//     *
//     * @param timestamp 时间戳（单位：毫秒）
//     *
//     * @return
//     */
//    public static String formatTime(long timestamp) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            return sdf.format(new Timestamp(timestamp));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return String.valueOf(timestamp);
//    }
//
//    public static String formatSecond(int second) {
//        String format = "%1$,02d:%2$,02d:%3$,02d";
//        Integer hours = second / (60 * 60);
//        Integer minutes = second / 60 - hours * 60;
//        Integer seconds = second - minutes * 60 - hours * 60 * 60;
//        Object[] array = new Object[] {hours, minutes, seconds};
//        return String.format(format, array);
//    }




}
