package io.rong.imkit.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.rong.database.ConversationDatabase;
import io.rong.database.Draft;
import io.rong.database.DraftDao;
import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationInfo;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imkit.widget.adapter.SubConversationListAdapter;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.message.VoiceMessage;

/**
 * Created by jenny_zhou1980 on 15/2/4.
 */
public class SubConversationListFragment extends UriFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    static final int REQ_LIST = 1;
    static final int REORDER_LIST = 2;

    static final int REFRESH_LIST = 3;
    static final int REFRESH_ITEM = 4;
    static final int REFRESH_MSG = 5;

    private SubConversationListAdapter mAdapter;
    private Conversation.ConversationType currentType;
    private ListView mList;

    RongIMClient.ResultCallback<List<Conversation>> mCallback = new RongIMClient.ResultCallback<List<Conversation>>() {
        @Override
        public void onSuccess(List<Conversation> conversations) {
            RLog.d(this, "SubConversationListFragment", "initFragment onSuccess callback");
            if (conversations == null || (conversations != null && conversations.size() == 0))
                return;

            List<UIConversation> uiConversationList = new ArrayList<UIConversation>();
            for (Conversation conversation : conversations) {
                if (mAdapter.getCount() > 0) {
                    int pos = mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                    if (pos < 0) {
                        UIConversation uiConversation = UIConversation.obtain(conversation, false);
                        uiConversationList.add(uiConversation);
                    }
                } else {
                    UIConversation uiConversation = UIConversation.obtain(conversation, false);
                    uiConversationList.add(uiConversation);
                }
            }

            for (int i = 0; i < uiConversationList.size(); i++) {
                RongContext.getInstance().executorBackground(new getDraftRunnable(uiConversationList.get(i), i));
            }

            mAdapter.addCollection(uiConversationList);

            if (mList != null && mList.getAdapter() != null) {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(RongIMClient.ErrorCode e) {
            //TODO show notice
            RLog.d(this, "SubConversationListFragment", "initFragment onError callback, e=" + e);
        }
    };

    public static ConversationListFragment getInstance() {
        return new ConversationListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RongContext.getInstance().getEventBus().register(this);

        if (getActivity().getIntent() == null || getActivity().getIntent().getData() == null)
            throw new IllegalArgumentException();
        mAdapter = new SubConversationListAdapter(getActivity());
    }


    public void initFragment(Uri uri) {
        String type = uri.getQueryParameter("type");
        Conversation.ConversationType value = null;

        RLog.d(this, "initFragment", "uri=" + uri);

        currentType = null;

        Conversation.ConversationType[] defaultTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.CHATROOM, Conversation.ConversationType.CUSTOMER_SERVICE,
                Conversation.ConversationType.SYSTEM, Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE};
        for (Conversation.ConversationType conversationType : defaultTypes) {
            if (conversationType.getName().equals(type)) {
                currentType = conversationType;
                value = conversationType;
                break;
            }
        }
        if (value != null)
            RongIM.getInstance().getRongIMClient().getConversationList(mCallback, value);
        else
            throw new IllegalArgumentException("Unknown conversation type!!");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fr_conversationlist, null);
        mList = findViewById(view, R.id.rc_list);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
        mList.setOnItemLongClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        RLog.d(this, "SubConversationListFragment", "onResume");
        super.onResume();
        RongIMClient.ConnectionStatusListener.ConnectionStatus status = RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus();
        if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
            showNotification(getResources().getString(R.string.rc_notice_network_unavailable));
            RongIM.getInstance().getRongIMClient().reconnect(null);
        } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
            showNotification(getResources().getString(R.string.rc_notice_tick));
        } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            hiddenNotification();
        } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            showNotification(getResources().getString(R.string.rc_notice_disconnect));
        }
    }

    public int findPositionForNewConversation(UIConversation uiConversation) {
        int count = mAdapter.getCount();
        int i, position = 0;

        for (i = 0; i < count; i++) {
            if (uiConversation.isTop()) {
                if (mAdapter.getItem(i).isTop() && mAdapter.getItem(i).getUIConversationTime() > uiConversation.getUIConversationTime())
                    position++;
                else
                    break;
            } else {
                if (mAdapter.getItem(i).isTop() || mAdapter.getItem(i).getUIConversationTime() > uiConversation.getUIConversationTime())
                    position++;
                else
                    break;
            }
        }

        return position;
    }

    private UIConversation makeUiConversation(Message message, int pos) {
        UIConversation uiConversation = null;

        //如果找到对应记录，则更新该条记录的未读消息数，并判断记录位置以及需要更新的item,进行局部刷新。
        if (pos >= 0) {
            uiConversation = mAdapter.getItem(pos);
            if (uiConversation != null) {
                uiConversation.setMessageContent(message.getContent());
                if (message.getMessageDirection() == Message.MessageDirection.SEND) {
                    uiConversation.setUIConversationTime(message.getSentTime());
                    if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null)
                        uiConversation.setConversationSenderId(RongIM.getInstance().getRongIMClient().getCurrentUserId());
                } else {
                    uiConversation.setUIConversationTime(message.getSentTime());
                    uiConversation.setConversationSenderId(message.getSenderUserId());
                }
                uiConversation.setConversationTargetId(message.getTargetId());
                uiConversation.setConversationContent(uiConversation.buildConversationContent(uiConversation));
                uiConversation.setSentStatus(message.getSentStatus());
                uiConversation.setShowDraftFlag(false);
                uiConversation.setLatestMessageId(message.getMessageId());

                MessageTag tag = message.getContent().getClass().getAnnotation(MessageTag.class);
                if (message.getMessageDirection() == Message.MessageDirection.RECEIVE && (tag.flag() & MessageTag.ISCOUNTED) != 0) {
                    uiConversation.setUnReadMessageCount(uiConversation.getUnReadMessageCount() + 1);
                    List<ConversationInfo> infoList = RongContext.getInstance().getCurrentConversationList();
                    for (ConversationInfo info : infoList) {
                        if (info != null && info.getConversationType().equals(message.getConversationType()) && info.getTargetId().equals(message.getTargetId()))
                            uiConversation.setUnReadMessageCount(0);
                    }
                } else {
                    uiConversation.setUnReadMessageCount(0);
                }
            }
        }
        return uiConversation;
    }

    public void onEventMainThread(Message message) {

        RLog.d(SubConversationListFragment.this, "onEventMainThread", "Message");

        int originalIndex = mAdapter.findPosition(message.getConversationType(), message.getTargetId());

        if (!(message.getConversationType().equals(currentType)))
            return;//如果该条消息类型和当前列表的消息类型不符合，则忽略不处理。

        UIConversation uiConversation = null;
        /*如果找到对应记录，则更新该条记录的未读消息数，并判断记录位置以及需要更新的item,进行局部刷新。*/
        if (originalIndex >= 0) {
            uiConversation = makeUiConversation(message, originalIndex);

            int newPosition = findPositionForNewConversation(uiConversation);
            if (newPosition == originalIndex) {
                android.os.Message msg = new android.os.Message();
                msg.what = REFRESH_ITEM;
                msg.obj = newPosition - mList.getFirstVisiblePosition();

                getHandler().removeMessages(REFRESH_ITEM, newPosition - mList.getFirstVisiblePosition());
                getHandler().sendMessageDelayed(msg, 200);
            } else {
                mAdapter.remove(originalIndex);
                mAdapter.add(uiConversation, newPosition);
                getHandler().sendEmptyMessage(REFRESH_LIST);
            }
        } else {
            //没有对应记录，则新建一条记录插入列表。
            uiConversation = UIConversation.obtain(message);
            mAdapter.add(uiConversation, findPositionForNewConversation(uiConversation));
            getHandler().sendEmptyMessage(REFRESH_LIST);
        }
    }

    public void onEventMainThread(Event.OnReceiveMessageEvent onReceiveMessageEvent) {
        onEventMainThread(onReceiveMessageEvent.getMessage());
    }

    public void onEventMainThread(MessageContent content) {

        RLog.d(SubConversationListFragment.this, "onEventBackgroundThread::MessageContent", "MessageContent");

        for (int index = mList.getFirstVisiblePosition(); index < mList.getLastVisiblePosition(); index++) {
            UIConversation tempUIConversation = mAdapter.getItem(index);
            if (tempUIConversation.getMessageContent().equals(content)) {
                tempUIConversation.setMessageContent(content);

                Spannable messageData = RongContext.getInstance().getMessageTemplate(content.getClass()).getContentSummary(content);

                if (tempUIConversation.getMessageContent() instanceof VoiceMessage) {
                    boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(tempUIConversation.getConversationType(), tempUIConversation.getConversationTargetId())
                            .getReceivedStatus().isListened();
                    if (isListened) {
                        messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_text_color_secondary)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_voice_color)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                tempUIConversation.setConversationContent(messageData);
                tempUIConversation.setShowDraftFlag(false);

                android.os.Message msg = new android.os.Message();
                msg.what = REFRESH_ITEM;
                msg.obj = index;
                getHandler().sendMessage(msg);
            }
        }
    }

    public void onEventMainThread(Draft draft) {
        Conversation.ConversationType curType = Conversation.ConversationType.setValue(draft.getType());
        if (curType == null) {
            throw new IllegalArgumentException("the type of the draft is unknown!");
        }

        int position = mAdapter.findPosition(curType, draft.getId());

        if (position >= 0) {
            UIConversation conversation = mAdapter.getItem(position);
            if (draft.getContent() == null) {
                conversation.setShowDraftFlag(false);
            } else {
                conversation.setShowDraftFlag(true);
                conversation.setDraft(draft.getContent());
            }
            mAdapter.getView(position, mList.getChildAt(position - mList.getFirstVisiblePosition()), mList);
        }

    }

    /* function: 异步获取到群组信息后，在此进行相应刷新*/
    public void onEventMainThread(Group groupInfo) {

        int count = mAdapter.getCount();

        if(groupInfo.getName() == null){
            return;
        }

        for (int i = 0; i < count; i++) {
            UIConversation temp = mAdapter.getItem(i);
            if (temp.getConversationTargetId().equals(groupInfo.getId())) {
                temp.setUIConversationTitle(groupInfo.getName());
                if (groupInfo.getPortraitUri() != null)
                    temp.setIconUrl(groupInfo.getPortraitUri());
                mAdapter.getView(i, mList.getChildAt(i - mList.getFirstVisiblePosition()), mList);
            }
        }
    }

    /* function: 异步获取到用户信息后，在此进行相应刷新*/
    public void onEventMainThread(UserInfo userInfo) {
        int count = mAdapter.getCount();

        RLog.d(this, "onEvent", "update userInfo");

        if(userInfo.getName() == null){
            return;
        }

        for (int i = 0; i < count; i++) {
            UIConversation temp = mAdapter.getItem(i);
            String type = temp.getConversationType().getName();
            Spannable messageData = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());

            //群组或讨论组非聚合显示情况，需要比较该会话的senderId.因为此时的targetId为群组或讨论组名字。
            if ((type.equals(Conversation.ConversationType.GROUP.getName()) ||
                    type.equals(Conversation.ConversationType.DISCUSSION.getName()))
                    && (temp.getConversationSenderId().equals(userInfo.getUserId()))) {
                SpannableStringBuilder builder = new SpannableStringBuilder();

                if (temp.getMessageContent() instanceof VoiceMessage) {
                    boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(temp.getConversationType(), temp.getConversationTargetId())
                            .getReceivedStatus().isListened();
                    if (isListened) {
                        messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_text_color_secondary)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_voice_color)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                builder.append(userInfo.getName()).append(" : ").append(messageData);
                temp.setConversationContent(builder);
            } else if (temp.getConversationTargetId().equals(userInfo.getUserId())) {
                if (type.equals(Conversation.ConversationType.PRIVATE.getName())) {
                    temp.setUIConversationTitle(userInfo.getName());
                } else {
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    if (temp.getMessageContent() instanceof VoiceMessage) {
                        boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(temp.getConversationType(), temp.getConversationTargetId())
                                .getReceivedStatus().isListened();
                        if (isListened) {
                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_text_color_secondary)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_voice_color)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                    builder.append(userInfo.getName()).append(" : ").append(messageData);
                    temp.setConversationContent(builder);
                    temp.setIconUrl(userInfo.getPortraitUri());
                }
            }
            mAdapter.getView(i, mList.getChildAt(i - mList.getFirstVisiblePosition()), mList);
        }
    }

    public void onEvent(final RongIMClient.ConnectionStatusListener.ConnectionStatus status) {

        RLog.d(SubConversationListFragment.this, "ConnectionStatus", status.toString());

        if (isResumed())

            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (status == RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE) {
                        showNotification(getResources().getString(R.string.rc_notice_network_unavailable));
                    } else if (status == RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE) {
                        showNotification(getResources().getString(R.string.rc_notice_tick));
                    } else if (status == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
                        hiddenNotification();
                    } else if (status == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED) {
                        showNotification(getResources().getString(R.string.rc_notice_disconnect));
                    }
                }
            });
    }

    public void onEventMainThread(Discussion discussion) {
        int count = mAdapter.getCount();

        for (int i = 0; i < count; i++) {
            UIConversation temp = mAdapter.getItem(i);
            boolean gatherState = RongContext.getInstance().getConversationGatherState(temp.getConversationType().getName());
            if (temp.getConversationTargetId().equals(discussion.getId())) {
                temp.setUIConversationTitle(discussion.getName());
                mAdapter.getView(i, mList.getChildAt(i - mList.getFirstVisiblePosition()), mList);
                break;
            }
        }
    }

    public void onEventMainThread(PublicServiceInfo accountInfo) {
        int count = mAdapter.getCount();

        for (int i = 0; i < count; i++) {
            if (mAdapter.getItem(i).getConversationTargetId().equals(accountInfo.getTargetId())) {
                mAdapter.getItem(i).setIconUrl(accountInfo.getPortraitUri());
                mAdapter.getItem(i).setUIConversationTitle(accountInfo.getName());
                mAdapter.getView(i, mList.getChildAt(i - mList.getFirstVisiblePosition()), mList);
                break;
            }
        }
    }

    public void onEventMainThread(Event.ConversationUnreadEvent unreadEvent) {
        final int targetIndex = mAdapter.findPosition(unreadEvent.getType(), unreadEvent.getTargetId());

        if (targetIndex >= 0) {
            final UIConversation temp = mAdapter.getItem(targetIndex);
            temp.setUnReadMessageCount(0);
            mAdapter.getView(targetIndex, mList.getChildAt(targetIndex - mList.getFirstVisiblePosition()), mList);
        }
    }

    public void onEventMainThread(Event.ConversationTopEvent setTopEvent) throws IllegalAccessException {
        final int originalIndex = mAdapter.findPosition(setTopEvent.getConversationType(), setTopEvent.getTargetId());

        if (originalIndex >= 0) {
            UIConversation temp = mAdapter.getItem(originalIndex);
            boolean originalValue = temp.isTop();
            int newIndex;

            if (originalValue == true) {
                temp.setTop(false);
                newIndex = findPositionForCancleTop(originalIndex);
                Toast.makeText(getActivity(), getString(R.string.rc_group_list_popup_cancel_top), Toast.LENGTH_SHORT).show();
            } else {
                temp.setTop(true);
                newIndex = findPositionForSetTop(temp);
                Toast.makeText(getActivity(), getString(R.string.rc_group_list_dialog_set_top), Toast.LENGTH_SHORT).show();
            }
            if (originalIndex == newIndex)
                mAdapter.getView(newIndex, mList.getChildAt(newIndex - mList.getFirstVisiblePosition()), mList);
            else {
                getHandler().obtainMessage(REORDER_LIST, originalIndex, newIndex).sendToTarget();
            }
        } else {
            throw new IllegalAccessException("the item has already been deleted!");
        }
    }

    public void onEventMainThread(Event.ConversationRemoveEvent removeEvent) {
        int originalIndex = mAdapter.findPosition(removeEvent.getType(), removeEvent.getTargetId());
        if (originalIndex >= 0) {
            mAdapter.remove(originalIndex);
            getHandler().sendEmptyMessage(REFRESH_LIST);
        }
    }

    public void onEventMainThread(Event.ConversationNotificationEvent notificationEvent) {
        final int originalIndex = mAdapter.findPosition(notificationEvent.getConversationType(), notificationEvent.getTargetId());

        if (originalIndex >= 0) {
            mAdapter.getView(originalIndex, mList.getChildAt(originalIndex - mList.getFirstVisiblePosition()), mList);
        }
    }

    public void onEventMainThread(Event.MessagesClearEvent clearMessagesEvent) {
        final int originalIndex = mAdapter.findPosition(clearMessagesEvent.getType(), clearMessagesEvent.getTargetId());

        if (clearMessagesEvent != null && originalIndex >= 0) {
            mAdapter.getItem(originalIndex).setMessageContent(null);
            mAdapter.getItem(originalIndex).setConversationContent(null);
            mAdapter.getItem(originalIndex).setSentStatus(null);
            mAdapter.getView(originalIndex, mList.getChildAt(originalIndex - mList.getFirstVisiblePosition()), mList);
        }

    }

    public void onEventMainThread(Event.MessageDeleteEvent event) {
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (event.getMessageIds().contains(mAdapter.getItem(i).getLatestMessageId())) {
                final int index = i;
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Conversation temp = RongIM.getInstance().getRongIMClient().getConversation(mAdapter.getItem(index).getConversationType()
                                , mAdapter.getItem(index).getConversationTargetId());
                        android.os.Message msg = new android.os.Message();
                        msg.what = REFRESH_MSG;
                        msg.obj = temp;
                        msg.arg1 = index;
                        getHandler().sendMessage(msg);
                    }
                }, 200);
            }
        }

    }

    public void onEventMainThread(Event.OnMessageSendErrorEvent sendErrorEvent) {
        int index = mAdapter.findPosition(sendErrorEvent.getMessage().getConversationType(), sendErrorEvent.getMessage().getTargetId());

        if (index >= 0) {
            mAdapter.getItem(index).setSentStatus(Message.SentStatus.FAILED);
            mAdapter.getView(index, mList.getChildAt(index - mList.getFirstVisiblePosition()), mList);
        }
    }

    public void onEventMainThread(Event.QuitDiscussionEvent event) {
        int index = mAdapter.findPosition(Conversation.ConversationType.DISCUSSION, event.getDiscussionId());

        if (index >= 0) {
            mAdapter.remove(index);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(Event.QuitGroupEvent event) {
        int index = mAdapter.findPosition(Conversation.ConversationType.GROUP, event.getGroupId());

        if (index >= 0) {
            mAdapter.remove(index);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(Event.MessageListenedEvent event) {
        final int originalIndex = mAdapter.findPosition(event.getConversationType(), event.getTargetId());

        if (originalIndex >= 0) {
            UIConversation temp = mAdapter.getItem(originalIndex);
            if (temp.getLatestMessageId() == event.getLatestMessageId()) {
                Spannable content = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
                boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(event.getConversationType(), event.getTargetId()).getReceivedStatus().isListened();
                if (isListened) {
                    content.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_text_color_secondary)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    content.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_voice_color)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                temp.setConversationContent(content);
            }
            mAdapter.getView(originalIndex, mList.getChildAt(originalIndex - mList.getFirstVisiblePosition()), mList);
        }
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case REFRESH_LIST:
                mAdapter.notifyDataSetChanged();
                break;
            case REFRESH_ITEM:
                int position = (Integer) msg.obj;
                mAdapter.getView(position, mList.getChildAt(position - mList.getFirstVisiblePosition()), mList);
                break;
            case REORDER_LIST:
                int fromPos = msg.arg1;
                int targetPos = msg.arg2;

                UIConversation tempUIConversationFrom = mAdapter.getItem(fromPos);
                mAdapter.remove(fromPos);
                mAdapter.add(tempUIConversationFrom, targetPos);
                mAdapter.notifyDataSetChanged();
                break;
            case REFRESH_MSG:
                Conversation conversation = (Conversation) msg.obj;
                int index = msg.arg1;
                UIConversation temp = UIConversation.obtain(conversation, false);
                int newPosition = 0;

                mAdapter.remove(index);

                for (int i = 0; i < mAdapter.getCount(); i++) {
                    if (temp.isTop()) {
                        if (mAdapter.getItem(i).isTop() && mAdapter.getItem(i).getUIConversationTime() > temp.getUIConversationTime())
                            newPosition++;
                        else
                            break;
                    } else {
                        if (mAdapter.getItem(i).isTop() || mAdapter.getItem(i).getUIConversationTime() > temp.getUIConversationTime())
                            newPosition++;
                        else
                            break;
                    }
                }

                mAdapter.add(temp, newPosition);
                mAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiconversation = mAdapter.getItem(position);
        Conversation.ConversationType type = uiconversation.getConversationType();
        uiconversation.setUnReadMessageCount(0);

        RongIM.getInstance().startConversation(getActivity(), type, uiconversation.getConversationTargetId(), uiconversation.getUIConversationTitle());

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiConversation = mAdapter.getItem(position);
        String type = uiConversation.getConversationType().getName();
        String title = uiConversation.getUIConversationTitle();
        String[] mItems;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        buildMultiDialog(uiConversation);
        return true;
    }

    private void buildMultiDialog(final UIConversation UIConversation) {
        String[] items = new String[2];
        if (UIConversation.isTop())
            items[0] = getActivity().getString(R.string.rc_group_list_dialog_cancel_top);
        else
            items[0] = getActivity().getString(R.string.rc_group_list_dialog_set_top);
        items[1] = getActivity().getString(R.string.rc_group_list_dialog_remove);

        ArraysDialogFragment.newInstance(UIConversation.getUIConversationTitle(), items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
            @Override
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    RongIM.getInstance().getRongIMClient().
                            setConversationToTop(UIConversation.getConversationType()
                                    , UIConversation.getConversationTargetId(), !UIConversation.isTop());
                } else if (which == 1) {
                    RongIM.getInstance().getRongIMClient().removeConversation(UIConversation.getConversationType()
                            , UIConversation.getConversationTargetId());

                }
            }
        }).show(getFragmentManager());
    }

    public int findPositionForSetTop(UIConversation uiconversation) {
        int count = mAdapter.getCount();
        int i, position = 0;

        for (i = 0; i < count; i++) {
            if (mAdapter.getItem(i).isTop() && mAdapter.getItem(i).getUIConversationTime() > uiconversation.getUIConversationTime()) {
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
    public int findPositionForCancleTop(int index) {
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

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        RLog.d(this, "SubConversationListFragment", "onDestroy");
        RongContext.getInstance().getEventBus().unregister(this);
        getHandler().removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        RLog.d(this, "SubConversationListFragment", "onPause");
        super.onPause();
    }

    class getDraftRunnable implements Runnable {
        UIConversation conversation;
        int index;

        getDraftRunnable(UIConversation conversation, int index) {
            this.conversation = conversation;
            this.index = index;
        }

        @Override
        public void run() {
            int type = conversation.getConversationType().getValue();
            String targetId = conversation.getConversationTargetId();
            Draft draft = ConversationDatabase.getDraftDao().queryBuilder()
                    .where(DraftDao.Properties.Type.eq(type), DraftDao.Properties.Id.eq(targetId)).unique();

            if (draft == null) {
                conversation.setShowDraftFlag(false);
                return;
            }

            if (draft != null && draft.getContent() == null) {
                conversation.setShowDraftFlag(false);
            } else {
                conversation.setShowDraftFlag(true);
                conversation.setDraft(draft.getContent());
            }

            android.os.Message msg = new android.os.Message();
            msg.what = REFRESH_ITEM;
            msg.obj = index;
            getHandler().sendMessage(msg);
        }
    }
}
