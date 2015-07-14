package io.rong.imkit;

import android.text.Spannable;

import java.util.concurrent.ConcurrentHashMap;

import io.rong.imkit.model.ConversationKey;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.notification.PushNotificationMessage;

/**
 * Created by jenny_zhou1980 on 15/3/19.
 */
public class PushNotificationManager extends io.rong.notification.PushNotificationManager {
    private static PushNotificationManager sS;

    ConcurrentHashMap<String, Message> messageMap = new ConcurrentHashMap<>();

    private PushNotificationManager(RongContext context) {
        super(context);
        context.getEventBus().register(this);
    }

    public static void init(RongContext context) {
        if (sS == null)
            sS = new PushNotificationManager(context);
    }

    public static PushNotificationManager getInstance() {
        return sS;
    }

    public void onReceiveMessageFromApp(Message message) {
        Conversation.ConversationType type = message.getConversationType();
        String targetUserName = null;
        PushNotificationMessage pushMsg;
        Spannable content = RongContext.getInstance().getMessageTemplate(message.getContent().getClass())
                .getContentSummary(message.getContent());

        ConversationKey key = ConversationKey.obtain(message.getTargetId(), message.getConversationType());

        RLog.i(PushNotificationManager.this, "onReceiveMessageFromApp", "start");

        if(content == null){
            RLog.i(PushNotificationManager.this, "onReceiveMessageFromApp", "Content is null. Return directly.");
            return;
        }

        if (type.equals(Conversation.ConversationType.PRIVATE) || type.equals(Conversation.ConversationType.CUSTOMER_SERVICE)
                || type.equals(Conversation.ConversationType.CHATROOM)) {
            UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(message.getTargetId());
            if (userInfo != null)
                targetUserName = userInfo.getName();
        } else if (type.equals(Conversation.ConversationType.GROUP)) {
            Group groupInfo = RongContext.getInstance().getGroupInfoFromCache(message.getTargetId());
            if (groupInfo != null) {
                targetUserName = groupInfo.getName();
            }
        } else if (type.equals(Conversation.ConversationType.DISCUSSION)) {
            Discussion discussionInfo = RongContext.getInstance().getDiscussionInfoFromCache(message.getTargetId());
            if (discussionInfo != null) {
                targetUserName = discussionInfo.getName();
            }
        } else if (type.equals(Conversation.ConversationType.PUBLIC_SERVICE) ||
                type.equals(Conversation.PublicServiceType.APP_PUBLIC_SERVICE)) {
            PublicServiceInfo info = RongContext.getInstance().getPublicServiceInfoFromCache(key.getKey());
            if (info != null) {
                targetUserName = info.getName();
            }
        }

        if (targetUserName != null) {
            pushMsg = PushNotificationMessage.obtain(content.toString(), message.getConversationType(), message.getTargetId(), targetUserName);
            onReceiveMessage(pushMsg);
        } else {
            messageMap.put(key.getKey(), message);
        }
    }


    public void onRemoveNotification() {
        messageMap.clear();
        onRemoveNotificationMsgFromCache();
    }

    public void onEventMainThread(UserInfo userInfo) {
        int typeCount = 3;
        Message message;
        PushNotificationMessage pushMsg;
        Conversation.ConversationType type;

        for (int i = 0; i < typeCount; i++) {
            if (i == 0)
                type = Conversation.ConversationType.PRIVATE;
            else if (i == 1)
                type = Conversation.ConversationType.CUSTOMER_SERVICE;
            else
                type = Conversation.ConversationType.CHATROOM;

            String key = ConversationKey.obtain(userInfo.getUserId(), type).getKey();

            if (messageMap.containsKey(key)) {
                message = messageMap.get(key);
                Spannable content = RongContext.getInstance().getMessageTemplate(message.getContent().getClass())
                        .getContentSummary(message.getContent());
                if(userInfo.getName() == null){
                    pushMsg = PushNotificationMessage.obtain(content.toString(), type, userInfo.getUserId(), userInfo.getUserId());
                }else {
                    pushMsg = PushNotificationMessage.obtain(content.toString(), type, userInfo.getUserId(), userInfo.getName());
                }
                onReceiveMessage(pushMsg);
                messageMap.remove(key);
            }
        }
    }

    public void onEventMainThread(Group groupInfo) {
        Message message;
        PushNotificationMessage pushMsg;
        String key = ConversationKey.obtain(groupInfo.getId(), Conversation.ConversationType.GROUP).getKey();

        if (messageMap.containsKey(key)) {
            message = messageMap.get(key);
            Spannable content = RongContext.getInstance().getMessageTemplate(message.getContent().getClass())
                    .getContentSummary(message.getContent());
            if (groupInfo.getName() == null) {
                pushMsg = PushNotificationMessage.obtain(content.toString(), Conversation.ConversationType.GROUP, groupInfo.getId(), groupInfo.getId());
            } else {
                pushMsg = PushNotificationMessage.obtain(content.toString(), Conversation.ConversationType.GROUP, groupInfo.getId(), groupInfo.getName());
            }
            onReceiveMessage(pushMsg);
            messageMap.remove(key);
        }

    }

    public void onEventMainThread(Discussion discussion) {
        Message message;
        PushNotificationMessage pushMsg;
        String key = ConversationKey.obtain(discussion.getId(), Conversation.ConversationType.DISCUSSION).getKey();

        if (messageMap.containsKey(key)) {
            message = messageMap.get(key);
            Spannable content = RongContext.getInstance().getMessageTemplate(message.getContent().getClass())
                    .getContentSummary(message.getContent());

            if(discussion.getName() == null){
                pushMsg = PushNotificationMessage.obtain(content.toString(), Conversation.ConversationType.DISCUSSION, discussion.getId(), discussion.getId());
            }else {
                pushMsg = PushNotificationMessage.obtain(content.toString(), Conversation.ConversationType.DISCUSSION, discussion.getId(), discussion.getName());
            }
            onReceiveMessage(pushMsg);
            messageMap.remove(key);
        }
    }

    public void onEventMainThread(PublicServiceInfo info) {
        Message message;
        PushNotificationMessage pushMsg;
        String key = ConversationKey.obtain(info.getTargetId(), info.getConversationType()).getKey();

        if (messageMap.containsKey(key)) {
            message = messageMap.get(key);
            Spannable content = RongContext.getInstance().getMessageTemplate(message.getContent().getClass())
                    .getContentSummary(message.getContent());
            if(info.getName() == null){
                pushMsg = PushNotificationMessage.obtain(content.toString(), info.getConversationType(), info.getTargetId(), info.getTargetId());
            }else {
                pushMsg = PushNotificationMessage.obtain(content.toString(), info.getConversationType(), info.getTargetId(), info.getName());
            }
            onReceiveMessage(pushMsg);
            messageMap.remove(key);
        }
    }
}
