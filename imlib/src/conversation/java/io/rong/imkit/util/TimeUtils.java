package io.rong.imkit.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DragonJ on 15/3/25.
 */
public class TimeUtils {


    public static String formatData(long timeMillis) {

        if (timeMillis == 0)
            return "";

        String result = null;

        int targetDay = (int) (timeMillis / (24 * 60 * 60 * 1000));
        int nowDay = (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000));

        if (targetDay == nowDay) {
            result = fromatDate(timeMillis, "HH:mm");
        } else if (targetDay + 1 == nowDay) {
            result = String.format("昨天 %1$s", fromatDate(timeMillis, "HH:mm"));
        } else {
            result = fromatDate(timeMillis, "yyyy-MM-dd");
        }


        return result;

    }


    public static String formatTime(long timeMillis) {

        if (timeMillis == 0)
            return "";

        String result = null;

        int targetDay = (int) (timeMillis / (24 * 60 * 60 * 1000));
        int nowDay = (int) (System.currentTimeMillis() / (24 * 60 * 60 * 1000));

        if (targetDay == nowDay) {
            result = fromatDate(timeMillis, "HH:mm");
        } else if (targetDay + 1 == nowDay) {
            result = String.format("昨天 %1$s", fromatDate(timeMillis, "HH:mm"));
        } else {
            result = fromatDate(timeMillis, "yyyy-MM-dd HH:mm");
        }


        return result;

    }


    private static String fromatDate(long timeMillis, String fromat) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromat);
        return sdf.format(new Date(timeMillis));
    }

}
