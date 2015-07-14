package com.tongban.corelib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.text.TextUtils;

/**
 * 时间转换工具
 */
public class DateUtils {

    public static final Date convertStringToDate(String aMask, String strDate)
            throws ParseException {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(aMask);

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    public static final String getDateTime(String aMask, Date aDate) {
        String returnValue = "";
        if (aDate != null) {
            SimpleDateFormat df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * 截取时间（用于评论列表的时间格式如2014-02-18 00:00:00转为2014-02-18）
     */
    public static final String cutOutDate(String date) {
        String result = "";
        if (!TextUtils.isEmpty(date)) {
            // 不空的情况下
            if (date.contains(" ")) {
                int spaceIndex = date.trim().indexOf(" ");
                result = date.substring(0, spaceIndex);
            } else {
                result = date;
            }
        }
        return result;
    }

    /**
     * 计算两个日期型的时间相差多少时间
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public String twoDateDistance(Date startDate, Date endDate) {

        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000)
            return timeLong / 1000 + "秒前";
        else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.format(startDate);
        }
    }

    public static String formatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return dateFormat.format(date);
    }

    /**
     * 判断时间
     *
     * @param nowDate 原始
     * @param date    要进行对比的
     * @return 相差天数
     */
    public static int compareDate(Date nowDate, Date date) {
        Date today = nowDate;
        int day = date.getDate();
        int thisDay = today.getDate();
        return thisDay - day;
    }

    public static String dateToString(Date date, String formatType) {
        if (date != null) {
            return new SimpleDateFormat(formatType).format(date);
        } else {
            return "";
        }
    }

    // long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType) {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public static Date stringToDate(String strTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatTime(int time) {
        time = time / 1000;
        int sec = time % 60;
        return time / 60 + ":" + (sec > 9 ? sec + "" : "0" + sec);
    }

    /**
     * 计算时间差
     *
     * @param date
     * @param predate
     * @return long
     */
    public static long getTimeDifference(Date date, Date predate) {
        long time = date.getTime() - predate.getTime();
        long min = time / 1000 / 60;
        return min;
    }

}
