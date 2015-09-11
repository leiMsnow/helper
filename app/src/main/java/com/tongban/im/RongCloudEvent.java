package com.tongban.im;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;

import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.BaseDialog;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.AccountApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.db.bean.GroupTable;
import com.tongban.im.db.bean.UserTable;
import com.tongban.im.db.helper.GroupDaoHelper;
import com.tongban.im.db.helper.UserDaoHelper;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.widget.provider.ContactsProvider;
import com.tongban.im.widget.provider.TopicProvider;

import io.rong.imkit.PushNotificationManager;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imkit.widget.provider.TextInputProvider;
import io.rong.imkit.widget.provider.VoIPInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.notification.PushNotificationMessage;

/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。
 * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有：
 * 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。
 * 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。
 * 5、群组信息提供者：GetGroupInfoProvider。
 * 蓉c
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
 * 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */
public final class RongCloudEvent implements RongIMClient.OnReceiveMessageListener, RongIM.OnSendMessageListener,
        RongIM.UserInfoProvider, RongIM.GroupInfoProvider, RongIM.ConversationBehaviorListener,
        ConnectionStatusListener, RongIM.LocationProvider, RongIMClient.OnReceivePushMessageListener, RongIM.ConversationListBehaviorListener {

    private static final String TAG = RongCloudEvent.class.getSimpleName();

    private static RongCloudEvent mRongCloudInstance;

    private Context mContext;

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        initDefaultListener();
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {
        RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
//        RongIM.setPushMessageBehaviorListener(this);//自定义 push 通知。
    }

    /*
     * 连接成功注册。
     * <p/>
     * 在RongIM-connect-onSuccess后调用。
     */
    public void setOtherListener() {
        RongIM.getInstance().setSendMessageListener(this);//设置发出消息接收监听器.
        RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(this);//设置消息接收监听器。
        RongIM.getInstance().getRongIMClient().setConnectionStatusListener(this);//设置连接状态监听器。

        //扩展功能自定义
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                new LocationInputProvider(RongContext.getInstance()),//地理位置
                new VoIPInputProvider(RongContext.getInstance()),// 语音通话
                new ContactsProvider(RongContext.getInstance()),// 语音通话
                new TopicProvider(RongContext.getInstance())//话题
        };
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider);
    }

    /**
     * 自定义 push 通知。
     *
     * @param msg
     * @return
     */
    @Override
    public boolean onReceivePushMessage(PushNotificationMessage msg) {
        LogUtil.d(TAG, "onReceived-onPushMessageArrive:" + msg.getContent());

        PushNotificationManager.getInstance().onReceivePush(msg);

//        Intent intent = new Intent();
//        Uri uri;
//
//        intent.setAction(Intent.ACTION_VIEW);
//
//        Conversation.ConversationType conversationType = msg.getConversationType();
//
//        uri = Uri.parse("rong://" + RongContext.getInstance().getPackageName()).buildUpon().appendPath("conversationlist").build();
//        intent.setData(uri);
//        LogUtil.d(TAG, "onPushMessageArrive-url:" + uri.toString());
//
//        Notification notification=null;
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(RongContext.getInstance(), 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        if (android.os.Build.VERSION.SDK_INT < 11) {
//            notification = new Notification(RongContext.getInstance().getApplicationInfo().icon, "自定义 notification", System.currentTimeMillis());
//
//            notification.setLatestEventInfo(RongContext.getInstance(), "自定义 title", "这是 Content:"+msg.getObjectName(), pendingIntent);
//            notification.flags = Notification.FLAG_AUTO_CANCEL;
//            notification.defaults = Notification.DEFAULT_SOUND;
//        } else {
//
//             notification = new Notification.Builder(RongContext.getInstance())
//                    .setLargeIcon(getAppIcon())
//                    .setSmallIcon(R.drawable.ic_launcher)
//                    .setTicker("自定义 notification")
//                    .setContentTitle("自定义 title")
//                    .setContentText("这是 Content:"+msg.getObjectName())
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true)
//                    .setDefaults(Notification.DEFAULT_ALL).build();
//
//        }
//
//        NotificationManager nm = (NotificationManager) RongContext.getInstance().getSystemService(RongContext.getInstance().NOTIFICATION_SERVICE);
//
//        nm.notify(0, notification);

        return true;
    }

    private Bitmap getAppIcon() {
        BitmapDrawable bitmapDrawable;
        Bitmap appIcon;
        bitmapDrawable = (BitmapDrawable) RongContext.getInstance().getApplicationInfo().loadIcon(RongContext.getInstance().getPackageManager());
        appIcon = bitmapDrawable.getBitmap();
        return appIcon;
    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
     *
     * @param message 接收到的消息的实体信息。
     * @param left    剩余未拉取消息数目。
     */
    @Override
    public boolean onReceived(Message message, int left) {

        MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            LogUtil.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
        } else {
            LogUtil.d(TAG, "onReceived-其他消息，自己来判断处理");
        }

        return false;

    }


    @Override
    public Message onSend(Message message) {
        return message;
    }

    /**
     * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
     *
     * @param message 消息。
     */
    @Override
    public void onSent(Message message) {

        MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            LogUtil.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            LogUtil.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            LogUtil.d(TAG, "onSent-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            LogUtil.d(TAG, "onSent-RichContentMessage:" + richContentMessage.getContent());
        } else {
            LogUtil.d(TAG, "onSent-其他消息，自己来判断处理");
        }
    }

    /**
     * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
     *
     * @param userId 用户 Id。
     * @return 用户信息，（注：由开发者提供用户信息）。
     */
    @Override
    public UserInfo getUserInfo(String userId) {
        UserTable userTable = UserDaoHelper.get(mContext).getDataById(userId);
        if (userTable != null) {

            return new UserInfo(userId, userTable.getNick_name(),
                    userTable.getPortrait_url() == null ? null :
                            Uri.parse(userTable.getPortrait_url()));
        } else {
            UserCenterApi.getInstance().fetchUserCenterInfo(userId, new ApiCallback() {
                @Override
                public void onStartApi() {

                }

                @Override
                public void onComplete(Object obj) {
                    if (obj instanceof BaseEvent.UserCenterEvent) {
                        BaseEvent.UserCenterEvent userEvent = (BaseEvent.UserCenterEvent) obj;

                        UserTable userTable = UserDaoHelper.get(mContext).
                                getDataById(userEvent.user.getUser_id());

                        RongIM.getInstance().refreshUserInfoCache(
                                new UserInfo(userTable.getUser_id(), userTable.getNick_name(),
                                        userTable.getPortrait_url() == null ? null :
                                                Uri.parse(userTable.getPortrait_url())));
                    }
                }

                @Override
                public void onFailure(DisplayType displayType, Object errorObj) {

                }
            });
        }
        return null;
    }


    /**
     * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
     *
     * @param groupId 群组 Id.
     * @return 群组信息，（注：由开发者提供群组信息）。
     */
    @Override
    public Group getGroupInfo(String groupId) {
        // 先从本地数据库获取
        GroupTable groupTable = GroupDaoHelper.get(mContext).getDataById(groupId);
        if (groupTable != null) {
            return new Group(groupTable.getGroup_id(), groupTable.getGroup_name(),
                    groupTable.getGroup_avatar() == null ? null : Uri.parse(groupTable.getGroup_avatar()));
        }
        // 没有将从服务器获取
        else {
            GroupApi.getInstance().getGroupInfo(groupId, new ApiCallback() {
                @Override
                public void onStartApi() {

                }

                @Override
                public void onComplete(Object obj) {
                    if (obj instanceof BaseEvent.GroupInfoEvent) {
                        BaseEvent.GroupInfoEvent groupEvent = (BaseEvent.GroupInfoEvent) obj;

                        GroupTable groupTable = GroupDaoHelper.
                                get(mContext).getDataById((groupEvent.group.getGroup_id()));

                        RongIM.getInstance().refreshGroupInfoCache(
                                new Group(groupTable.getGroup_id(), groupTable.getGroup_name(),
                                        groupTable.getGroup_avatar() == null ? null :
                                                Uri.parse(groupTable.getGroup_avatar())));
                    }

                }

                @Override
                public void onFailure(DisplayType displayType, Object errorObj) {

                }
            });
        }
        return null;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
     *
     * @param context          应用当前上下文。
     * @param conversationType 会话类型。
     * @param userInfo         被点击的用户的信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType
            conversationType, UserInfo userInfo) {
        if (userInfo != null) {
            TransferCenter.getInstance().startUserCenter(userInfo.getUserId());
            LogUtil.d(TAG, "onUserPortraitClick: " + userInfo.getUserId());
        }

        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType
            conversationType, UserInfo userInfo) {
        if (userInfo != null) {
            LogUtil.d(TAG, "onUserPortraitLongClick: " + userInfo.getUserId());
            if (conversationType == Conversation.ConversationType.GROUP) {
                TextInputProvider textInputProvider = (TextInputProvider) RongContext.getInstance().getPrimaryInputProvider();
                //重置文本框数据
                textInputProvider.setCallContent("@" + userInfo.getName(), userInfo.getUserId());
            }
        }
        return false;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
     *
     * @param context 应用当前上下文。
     * @param message 被点击的消息的实体信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        LogUtil.d(TAG, "onMessageClick");

//        /**
//         * demo 代码  开发者需替换成自己的代码。
//         */
//        if (message.getContent() instanceof LocationMessage) {
//            Intent intent = new Intent(context, SOSOLocationActivity.class);
//            intent.putExtra("location", message.getContent());
//            context.startActivity(intent);
//        } else if (message.getContent() instanceof RichContentMessage) {
//            RichContentMessage mRichContentMessage = (RichContentMessage) message.getContent();
//            LogUtil.d("Begavior", "extra:" + mRichContentMessage.getExtra());
//
//        } else if (message.getContent() instanceof ImageMessage) {
//            ImageMessage imageMessage = (ImageMessage) message.getContent();
//            Intent intent = new Intent(context, PhotoActivity.class);
//
//            intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
//            if (imageMessage.getThumUri() != null)
//                intent.putExtra("thumbnail", imageMessage.getThumUri());
//
//            context.startActivity(intent);
//        }

        LogUtil.d("Begavior", message.getObjectName() + ":" + message.getMessageId());

        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }

    /**
     * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
     *
     * @param status 网络状态。
     */
    @Override
    public void onChanged(ConnectionStatus status) {
        LogUtil.d(TAG, "onChanged:" + status);
        if (status.getMessage().equals(ConnectionStatus.DISCONNECTED.getMessage())) {

        } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
            BaseDialog.Builder dialog = new BaseDialog.Builder(mContext);
            dialog.setMessage("您的账号已在别的设备登录");
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            SPUtils.clear(mContext);
                            TransferCenter.getInstance().startLogin(true);
                        }
                    });
            dialog.show();
        }
    }


    /**
     * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
     *
     * @param context  上下文
     * @param callback 回调
     */
    @Override
    public void onStartLocation(Context context, LocationCallback callback) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
//        DemoContext.getInstance().setLastLocationCallback(callback);
//        context.startActivity(new Intent(context, SOSOLocationActivity.class));//SOSO地图
    }

    /**
     * 点击会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationClick(Context context, View view, UIConversation conversation) {
        return false;
    }

    /**
     * 长按会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 长按会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation
            conversation) {
        return false;
    }
}
