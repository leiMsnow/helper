package io.rong.imkit.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhjchen on 4/10/15.
 */

public class Utils {


    /**
     * 本地化通知免打扰时间。
     *
     * @param startTime   默认  “-1”
     * @param spanMinutes 默认 -1
     */
    public static void saveNotificationQuietHours(Context mContext, String startTime, int spanMinutes) {

        SharedPreferences mPreferences = null;

        if (mContext != null)
            mPreferences = mContext.getSharedPreferences("RONG_SDK", Context.MODE_PRIVATE);

        if (mPreferences != null) {
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("QUIET_HOURS_START_TIME", startTime);
            editor.putInt("QUIET_HOURS_SPAN_MINUTES", spanMinutes);
            editor.commit();
        }
    }

    /**
     * 获取通知免打扰开始时间
     *
     * @return
     */
    public static String getNotificationQuietHoursForStartTime(Context mContext) {
        SharedPreferences mPreferences = null;

        if (mPreferences == null && mContext != null)
            mPreferences = mContext.getSharedPreferences("RONG_SDK", Context.MODE_PRIVATE);

        if (mPreferences != null) {
            return mPreferences.getString("QUIET_HOURS_START_TIME", "");
        }

        return "";
    }

    /**
     * 获取通知免打扰时间间隔
     *
     * @return
     */
    public static int getNotificationQuietHoursForSpanMinutes(Context mContext) {
        SharedPreferences mPreferences = null;

        if (mPreferences == null && mContext != null)
            mPreferences = mContext.getSharedPreferences("RONG_SDK", Context.MODE_PRIVATE);

        if (mPreferences != null) {
            return mPreferences.getInt("QUIET_HOURS_SPAN_MINUTES", 0);
        }

        return 0;
    }
}
