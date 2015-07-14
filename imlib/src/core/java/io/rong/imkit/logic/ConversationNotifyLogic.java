package io.rong.imkit.logic;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.utils.Utils;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by zhjchen on 4/10/15.
 */

/**
 * 控制弹通知和消息音
 * <p/>
 * 1、应用是否在后台
 * 2、新消息提醒设置
 * 3、安静时间设置
 */
public class ConversationNotifyLogic {

    public interface ConversationNotifyCallback {
        public void onComplete(boolean isNotify);
    }

    /**
     * 是否消息提醒
     *
     * @param context
     * @param message
     * @return
     */
    public static void isNotify(Context context, Message message, final ConversationNotifyCallback callback) {
        NotifyComposite composite = new NotifyComposite(context, message);

        if (!composite.isNotify()) {
            if (callback != null) {
                callback.onComplete(false);
            }
            return;
        }

        ConversationNotifyStatus status = new ConversationNotifyStatus(context, message);

        status.isNotify(new ConversationNotifyCallback() {

            @Override
            public void onComplete(boolean isNotify) {
                if (callback != null) {
                    callback.onComplete(isNotify);
                }
            }
        });

    }

    public static boolean isRunningInBackground(Context context, Message message) {
        AppIsBackground appIsBackground = new AppIsBackground(context, message);
        return appIsBackground.isAppInBackground();
    }

    public static class NotifyComposite extends Notification {
        List<Notification> mNotifications = new ArrayList<>();

        public NotifyComposite(Context context, Message message) {
            super(context, message);

//            mNotifications.add(new AppIsBackground(context, message));
            mNotifications.add(new ConversationQuietHours(context, message));

        }

        @Override
        public boolean isNotify() {
            boolean isNotify = true;

            for (Notification notify : mNotifications) {
                if (!notify.isNotify()) {
                    isNotify = false;
                    break;
                }
            }
            return isNotify;
        }

        @Override
        public void isNotify(ConversationNotifyCallback callback) {

        }
    }


    public static abstract class Notification {
        protected Message mMessage;
        protected Context mContext;

        public Notification(Context context, Message message) {
            mMessage = message;
            mContext = context;
        }

        public abstract boolean isNotify();

        public abstract void isNotify(ConversationNotifyCallback callback);
    }

    /**
     * 新消息通知
     */
    public static class ConversationNotifyStatus extends Notification {

        public ConversationNotifyStatus(Context context, Message message) {
            super(context, message);
        }

        @Override
        public boolean isNotify() {
            return true;
        }

        @Override
        public void isNotify(final ConversationNotifyCallback callback) {

            if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {

                if (RongContext.getInstance() != null) {

                    ConversationKey key = ConversationKey.obtain(mMessage.getTargetId(), mMessage.getConversationType());
                    Conversation.ConversationNotificationStatus notificationStatus = RongContext.getInstance().getConversationNotifyStatusFromCache(key);

                    if (notificationStatus != null && callback != null) {

                        if (notificationStatus == Conversation.ConversationNotificationStatus.NOTIFY) {
                            callback.onComplete(true);
                        } else {
                            callback.onComplete(false);
                        }

                        return;
                    }
                }

                RongIM.getInstance().getRongIMClient().getConversationNotificationStatus(mMessage.getConversationType(), mMessage.getTargetId(), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {

                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus status) {

                        if (Conversation.ConversationNotificationStatus.NOTIFY == status) {
                            if (callback != null)
                                callback.onComplete(true);
                        } else {
                            if (callback != null)
                                callback.onComplete(false);
                        }

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        if (callback != null)
                            callback.onComplete(false);
                    }


                });
            }
        }
    }

    /**
     * 会话安静时间
     */
    public static class ConversationQuietHours extends Notification {

        public ConversationQuietHours(Context context, Message message) {
            super(context, message);
        }

        @Override
        public boolean isNotify() {

            return !isQuietHours(mContext);
        }

        /**
         * 设置安静时间
         *
         * @return
         */
        private boolean isQuietHours(Context context) {

            String startTimeStr = Utils.getNotificationQuietHoursForStartTime(context);

            int hour = -1;
            int minute = -1;
            int second = -1;

            if (!TextUtils.isEmpty(startTimeStr) && startTimeStr.indexOf(":") != -1) {
                String[] time = startTimeStr.split(":");

                try {
                    if (time.length >= 3) {
                        hour = Integer.parseInt(time[0]);
                        minute = Integer.parseInt(time[1]);
                        second = Integer.parseInt(time[2]);
                    }
                } catch (NumberFormatException e) {
                    RLog.e(ConversationNotifyLogic.class, "getConversationNotificationStatus", "NumberFormatException");
                }
            }

            if (hour == -1 || minute == -1 || second == -1) {
                return false;
            }

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(Calendar.HOUR_OF_DAY, hour);
            startCalendar.set(Calendar.MINUTE, minute);
            startCalendar.set(Calendar.SECOND, second);


            long spanTime = Utils.getNotificationQuietHoursForSpanMinutes(context) * 60;
            long startTime = startCalendar.getTimeInMillis() / 1000;

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTimeInMillis(startTime * 1000 + spanTime * 1000);

            Calendar currentCalendar = Calendar.getInstance();

            if (currentCalendar.after(startCalendar) && currentCalendar.before(endCalendar)) {
                return true;
            } else {
                return false;
            }

        }

        @Override
        public void isNotify(ConversationNotifyCallback callback) {

        }
    }

    /**
     * 应用是否在后台
     */
    public static class AppIsBackground extends ConversationNotifyLogic.Notification {

        public AppIsBackground(Context context, Message message) {
            super(context, message);
        }

        @Override
        public boolean isNotify() {
            return !isRunningInBackground();
        }

        public boolean isAppInBackground() {
            return isRunningInBackground();
        }

        private boolean isRunningInBackground() {

            ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            String appPackageName = mContext.getPackageName();
            List<ActivityManager.RunningTaskInfo> runningTaskInfo = activityManager.getRunningTasks(1);
            String topAppPackageName = runningTaskInfo.get(0).topActivity.getPackageName();

            if (appPackageName.equals(topAppPackageName))
                return false;
            else
                return true;

        }


        @Override
        public void isNotify(ConversationNotifyLogic.ConversationNotifyCallback callback) {

        }
    }


}
