package io.rong.imkit.util;

import android.content.Context;
import android.net.Uri;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.model.Conversation;

/**
 * Created by jenny_zhou1980 on 15/2/28.
 */
public class ConversationListUtils {
    private static ConversationListUtils self;

    public static ConversationListUtils getInstance() {
        if (self == null)
            self = new ConversationListUtils();
        return self;
    }

    private ConversationListUtils() {
    }

    /**
     * @param uri the uri of every conversation type
     *            function: init the gather state for every conversation type.For ex, if set the private conversation
     *            gather state to be true in uri, then all the private conversation will be shown in one item
     *            in conversation list fragment.
     */
    public void initConversationGatherState(Uri uri) {
        String[] conversationType = {Conversation.ConversationType.PRIVATE.getName(), Conversation.ConversationType.GROUP.getName(),
                Conversation.ConversationType.DISCUSSION.getName(), Conversation.ConversationType.SYSTEM.getName(),
                Conversation.ConversationType.CUSTOMER_SERVICE.getName(), Conversation.ConversationType.CHATROOM.getName(),
                Conversation.ConversationType.PUBLIC_SERVICE.getName(), Conversation.PublicServiceType.APP_PUBLIC_SERVICE.getName()};
        for (String type : conversationType) {
            if (uri.getQueryParameter(type) != null) {
                if ("true".equals(uri.getQueryParameter(type))) {
                    RongContext.getInstance().setConversationGatherState(type, true);
                } else if ("false".equals(uri.getQueryParameter(type))) {
                    RongContext.getInstance().setConversationGatherState(type, false);
                }
            }
        }
    }

    public String setGatheredConversationTitle(Conversation.ConversationType type, Context context) {
        String title = "";
        switch (type) {
            case PRIVATE:
                title = context.getString(R.string.rc_group_list_my_private_conversation);
                break;
            case GROUP:
                title = context.getString(R.string.rc_group_list_my_group);
                break;
            case DISCUSSION:
                title = context.getString(R.string.rc_group_list_my_discussion);
                break;
            case CHATROOM:
                title = context.getString(R.string.rc_group_list_my_chatroom);
                break;
            case CUSTOMER_SERVICE:
                title = context.getString(R.string.rc_group_list_my_customer_service);
                break;
            case SYSTEM:
                title = context.getString(R.string.rc_group_list_system_conversation);
                break;
            case APP_PUBLIC_SERVICE:
                title = context.getString(R.string.rc_group_list_app_public_service);
                break;
            case PUBLIC_SERVICE:
                title = context.getString(R.string.rc_group_list_public_service);
                break;
            default:
                System.err.print("It's not the default conversation type!!");
                break;
        }
        return title;
    }


    public int findPositionForNewConversation(UIConversation uiconversation, ConversationListAdapter adapter) {
        int count = adapter.getCount();
        int i, position = 0;

        for (i = 0; i < count; i++) {
            if (uiconversation.isTop()) {
                if (adapter.getItem(i).isTop() && adapter.getItem(i).getUIConversationTime() > uiconversation.getUIConversationTime())
                    position++;
                else
                    break;
            } else {
                if (adapter.getItem(i).isTop() || adapter.getItem(i).getUIConversationTime() > uiconversation.getUIConversationTime())
                    position++;
                else
                    break;
            }
        }

        return position;
    }

    public int findPositionForSetTop(UIConversation uiconversation, ConversationListAdapter adapter) {
        int count = adapter.getCount();
        int i, position = 0;

        for (i = 0; i < count; i++) {
            if (adapter.getItem(i).isTop() && adapter.getItem(i).getUIConversationTime() > uiconversation.getUIConversationTime()) {
                position++;
            } else {
                break;
            }
        }
        return position;
    }

    /* function: 查找对某一会话取消置顶时，该会话的新位置
    * @Param index :该会话的原始位置
    * return: 该会话取消置顶后的新位置 */
    public int findPositionForCancleTop(int index, ConversationListAdapter mAdapter) {
        int count = mAdapter.getCount();
        int tap = 0;

        if (index > count) {
            throw new IllegalArgumentException("the index for the position is error!");
        }

        for (int i = index + 1; i < count; i++) {
            if (mAdapter.getItem(i).isTop()
                    || mAdapter.getItem(index).getUIConversationTime() < mAdapter.getItem(i).getUIConversationTime()) {
                tap++;
            } else {
                break;
            }
        }
        return index + tap;
    }

}
