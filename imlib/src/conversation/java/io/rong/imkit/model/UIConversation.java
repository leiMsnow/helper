package io.rong.imkit.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.VoiceMessage;

/**
 * Created by jenny_zhou1980 on 15/1/24.
 */
public class UIConversation implements Parcelable {
    private String conversationTitle;
    private Uri uri;
    private Spannable conversationContent;
    private MessageContent messageContent;
    private long conversationTime;
    private int unReadMessageCount;
    private boolean isTop;
    private Conversation.ConversationType conversationType;
    private Message.SentStatus sentStatus;
    private String targetId;
    private String senderId;
    private boolean isGathered; //该会话是否处于聚合显示的状态
    private boolean notificationBlockStatus;
    private String draft;
    private boolean isShowDraft;
    private int latestMessageId;

    public UIConversation() {
    }

    public void setUIConversationTitle(String title) {
        conversationTitle = title;
    }

    public String getUIConversationTitle() {
        return conversationTitle;
    }

    public void setIconUrl(Uri iconUrl) {
        uri = iconUrl;
    }

    public Uri getIconUrl() {
        return uri;
    }

    public void setConversationContent(Spannable content) {
        conversationContent = content;
    }

    public Spannable getConversationContent() {
        return conversationContent;
    }

    public void setMessageContent(MessageContent content) {
        messageContent = content;
    }

    public MessageContent getMessageContent() {
        return messageContent;
    }

    public void setUIConversationTime(long time) {
        conversationTime = time;
    }

    public long getUIConversationTime() {
        return conversationTime;
    }

    public void setUnReadMessageCount(int count) {
        unReadMessageCount = count;
    }

    public int getUnReadMessageCount() {
        return unReadMessageCount;
    }

    public void setTop(boolean value) {
        isTop = value;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setConversationType(Conversation.ConversationType type) {
        conversationType = type;
    }

    public Conversation.ConversationType getConversationType() {
        return conversationType;
    }

    public void setSentStatus(Message.SentStatus status) {
        sentStatus = status;
    }

    public Message.SentStatus getSentStatus() {
        return sentStatus;
    }

    public void setConversationTargetId(String id) {
        targetId = id;
    }

    public String getConversationTargetId() {
        return targetId;
    }

    public void setConversationSenderId(String id) {
        senderId = id;
    }

    public String getConversationSenderId() {
        return senderId;
    }

    public void setConversationGatherState(boolean state) {
        isGathered = state;
    }

    public boolean getConversationGatherState() {
        return isGathered;
    }

    public void setNotificationBlockStatus(boolean status) {
        notificationBlockStatus = status;
    }

    public boolean getNotificationBlockStatus() {
        return notificationBlockStatus;
    }

    public void setDraft(String content) {
        draft = content;
    }

    public String getDraft() {
        return draft;
    }

    public void setShowDraftFlag(boolean flag) {
        isShowDraft = flag;
    }

    public boolean getShowDraftFlag() {
        return isShowDraft;
    }

    public void setLatestMessageId(int id) {
        this.latestMessageId = id;
    }

    public int getLatestMessageId() {
        return latestMessageId;
    }

    public static UIConversation obtain(Conversation conversation, boolean gatherState) {
        if(RongContext.getInstance() == null)
            throw new ExceptionInInitializerError("RongContext hasn't been initialized !!");

        if(RongContext.getInstance().getConversationTemplate(conversation.getConversationType().getName()) == null){
            throw new IllegalArgumentException("the conversation type hasn't been registered! type:" + conversation.getConversationType());
        }

        String title = RongContext.getInstance().getConversationTemplate(conversation.getConversationType().getName())
                .getTitle(conversation.getTargetId());

        Uri uri = RongContext.getInstance().getConversationTemplate(conversation.getConversationType().getName())
                .getPortraitUri(conversation.getTargetId());

        MessageContent msgContent = conversation.getLatestMessage();

        UIConversation uiConversation = new UIConversation();

        uiConversation.setMessageContent(msgContent);
        uiConversation.setUnReadMessageCount(conversation.getUnreadMessageCount());
        uiConversation.setUIConversationTime(conversation.getSentTime());
        uiConversation.isGathered = gatherState;
        if (!gatherState) {
            uiConversation.uri = uri;
            uiConversation.conversationTitle = title;
        }
        uiConversation.setConversationType(conversation.getConversationType());
        uiConversation.setTop(conversation.isTop());

        if (conversation.getLatestMessageId() == -1)
            uiConversation.setSentStatus(null);
        else
            uiConversation.setSentStatus(conversation.getSentStatus());

        uiConversation.setConversationTargetId(conversation.getTargetId());
        uiConversation.setConversationSenderId(conversation.getSenderUserId());
        uiConversation.setLatestMessageId(conversation.getLatestMessageId());

        uiConversation.setConversationContent(buildConversationContent(uiConversation));//进一步根据聚合信息等，完善会话内容显示

        return uiConversation;
    }

    public static UIConversation obtain(Message message) {
        String title = "";
        Uri iconUri = null;

        if (RongContext.getInstance() != null) {
            title = RongContext.getInstance().getConversationTemplate(message.getConversationType().getName())
                    .getTitle(message.getTargetId());
            iconUri = RongContext.getInstance().getConversationTemplate(message.getConversationType().getName())
                    .getPortraitUri(message.getTargetId());
        }
        MessageTag tag = message.getContent().getClass().getAnnotation(MessageTag.class);

        UIConversation tempUIConversation = new UIConversation();

        if ((tag.flag() & MessageTag.ISCOUNTED) != 0)
            tempUIConversation.setUnReadMessageCount(1);
        tempUIConversation.setMessageContent(message.getContent());
        if (message.getMessageDirection() == Message.MessageDirection.SEND)
            tempUIConversation.setUIConversationTime(message.getSentTime());
        else
            tempUIConversation.setUIConversationTime(message.getSentTime());
        tempUIConversation.setIconUrl(iconUri);
        tempUIConversation.setUIConversationTitle(title);
        tempUIConversation.setConversationType(message.getConversationType());
        tempUIConversation.setConversationTargetId(message.getTargetId());
        if (message.getMessageDirection() == Message.MessageDirection.SEND) {
            if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null)
                tempUIConversation.setConversationSenderId(RongIM.getInstance().getRongIMClient().getCurrentUserId());
        } else
            tempUIConversation.setConversationSenderId(message.getSenderUserId());
        tempUIConversation.setSentStatus(message.getSentStatus());

        tempUIConversation.setConversationContent(buildConversationContent(tempUIConversation));
        tempUIConversation.setLatestMessageId(message.getMessageId());

        return tempUIConversation;
    }

    public static SpannableStringBuilder buildConversationContent(UIConversation uiConversation) {
        boolean isGathered = uiConversation.getConversationGatherState();
        String type = uiConversation.getConversationType().getName();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        boolean isShowName;
        Spannable messageData;

        if (uiConversation.getMessageContent() == null) {
            builder.append("");
            return builder;
        }

        isShowName = RongContext.getInstance().getMessageProviderTag(uiConversation.getMessageContent().getClass()).showSummaryWithName();

        messageData = RongContext.getInstance().getMessageTemplate(uiConversation.getMessageContent().getClass()).getContentSummary(uiConversation.getMessageContent());

        if (messageData == null) {
            builder.append("");
            return builder;
        }

        if (uiConversation.getMessageContent() instanceof VoiceMessage) {
            boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId())
                    .getReceivedStatus().isListened();
            if (isListened) {
                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_text_color_secondary)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_voice_color)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        if (isShowName == false) {
            builder.append(messageData);
            return builder;
        }

        if (isGathered) {
            String targetName = RongContext.getInstance().getConversationTemplate(type)
                    .getTitle(uiConversation.getConversationTargetId());
            builder.append(targetName == null ? uiConversation.getConversationTargetId() : targetName)
                    .append(" : ")
                    .append(messageData);
        } else {
            if (Conversation.ConversationType.GROUP.getName().equals(type)
                    || Conversation.ConversationType.DISCUSSION.getName().equals(type)) {
                String senderName = RongContext.getInstance()
                        .getConversationTemplate(Conversation.ConversationType.PRIVATE.getName())
                        .getTitle(uiConversation.getConversationSenderId());
                builder.append(senderName == null ? uiConversation.getConversationSenderId() : senderName)
                        .append(" : ")
                        .append(messageData);
            } else {
                return builder.append(messageData);
            }
        }
        return builder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
