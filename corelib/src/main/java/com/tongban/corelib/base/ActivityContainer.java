package com.tongban.corelib.base;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

/**
 * activity管理类
 * 记录所有打开的activity
 */
public class ActivityContainer {

    private List<Activity> activityList = new LinkedList<Activity>();

    private static ActivityContainer instance = null;

    private ActivityContainer() {
    }

    public static synchronized ActivityContainer getInstance() {
        if (instance == null) {
            instance = new ActivityContainer();
        }
        return instance;
    }

    /**
     * 在onCreate中加入
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        for (Activity act : activityList) {
            if (act.equals(activity)) {
                act.finish();
            }
        }
        activityList.add(activity);
    }

    /**
     * 在onDestroy中加入
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 关掉其它activity
     *
     * @param activity 不被关闭的activity
     */
    public void finishActivity(Activity activity) {
        for (Activity act : activityList) {
            if (!act.equals(activity)) {
                act.finish();
            }
        }
    }

    /**
     * 关闭所有activity，一般在注销登录使用
     */
    public void finishActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }
}
