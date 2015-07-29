package io.rong.imkit.fragment;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.rong.database.ConversationDatabase;
import io.rong.database.Draft;
import io.rong.database.DraftDao;
import io.rong.imkit.ConversationContext;
import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationInfo;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.util.ConversationListUtils;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.message.VoiceMessage;

public class ConversationListFragment extends UriFragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    static final int REORDER_LIST = 1;
    static final int REFRESH_LIST = 2;
    static final int REFRESH_ITEM = 3;
    static final int REFRESH_GROUP = 4;
    static final int REFRESH_MSG = 5;

    private ConversationListAdapter mAdapter;
    private ListView mList;
    private ArrayList<ConversationType> mSupportConversationList = new ArrayList<>();
    private ArrayList<Message> mMessageCache = new ArrayList<Message>();

    public static ConversationListFragment getInstance() {
        return new ConversationListFragment();
    }

    RongIMClient.ResultCallback<List<Conversation>> mCallback = new RongIMClient.ResultCallback<List<Conversation>>() {
        @Override
        public void onSuccess(List<Conversation> conversations) {
            RLog.d(this, "ConversationListFragment", "initFragment onSuccess callback");

            if (mAdapter != null && mAdapter.getCount() != 0) {
                mAdapter.clear();
            }

            if (conversations == null || (conversations != null && conversations.size() == 0)) {
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                return;
            }

            makeUiConversationList(conversations, getActivity());

            for (int i = 0; i < mAdapter.getCount(); i++) {
                RongContext.getInstance().executorBackground(new getDraftRunnable(mAdapter.getItem(i), i));
            }

            if (mList != null && mList.getAdapter() != null) {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(RongIMClient.ErrorCode e) {
            RLog.d(this, "ConversationListFragment", "initFragment onError callback, e=" + e);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(this, "ConversationListFragment", "onCreate");
        if (ConversationContext.getInstance() == null) {
            ConversationContext.init(RongContext.getInstance());
        }

        super.onCreate(savedInstanceState);

        mSupportConversationList.clear();

        RongContext.getInstance().getEventBus().register(this);

        mAdapter = new ConversationListAdapter(getActivity());
    }

    @Override
    public void onAttach(Activity activity) {
        RLog.d(this, "ConversationListFragment", "onAttach");
        super.onAttach(activity);
    }

    /**
     * function: parse uri send from app
     */
    @Override
    protected void initFragment(Uri uri) {
        ConversationType[] conversationType = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP,
                Conversation.ConversationType.DISCUSSION, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.CUSTOMER_SERVICE, Conversation.ConversationType.CHATROOM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE};

        RLog.d(this, "ConversationListFragment", "initFragment");

        if (uri == null) {
            RongIM.getInstance().getRongIMClient().getConversationList(mCallback);
            return;
        }

        mSupportConversationList = new ArrayList<ConversationType>();

        for (ConversationType type : conversationType) {
            if (uri.getQueryParameter(type.getName()) != null) {
                mSupportConversationList.add(type);

                if ("true".equals(uri.getQueryParameter(type.getName()))) {
                    RongContext.getInstance().setConversationGatherState(type.getName(), true);
                } else if ("false".equals(uri.getQueryParameter(type.getName()))) {
                    RongContext.getInstance().setConversationGatherState(type.getName(), false);
                }
            }
        }

        if (mSupportConversationList.size() > 0)
            RongIM.getInstance().getRongIMClient().getConversationList(mCallback, mSupportConversationList.toArray(new ConversationType[mSupportConversationList.size()]));
        else
            RongIM.getInstance().getRongIMClient().getConversationList(mCallback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(this, "ConversationListFragment", "onCreateView");
        View view = inflater.inflate(R.layout.rc_fr_conversationlist, null);
        mList = findViewById(view, R.id.rc_list);

        TextView mEmptyView = findViewById(view, android.R.id.empty);
        mList.setEmptyView(mEmptyView);

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
        super.onResume();

        if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
            throw new ExceptionInInitializerError("RongCloud SDK hasn't been initialized!");
        }
        RLog.d(ConversationListFragment.this, "onResume", "current connect status is:" + RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus());
        RongContext.getInstance().getPushNotificationMng().onRemoveNotification();

        RongIMClient.ConnectionStatusListener.ConnectionStatus status = RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus();
        if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
            showNotification(getResources().getString(R.string.rc_notice_network_unavailable));
            RongIM.getInstance().getRongIMClient().reconnect(null);
        } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
            showNotification(getResources().getString(R.string.rc_notice_tick));
        } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            hiddenNotification();
        } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            showNotification(getResources().getString(R.string.rc_notice_network_unavailable));
        }
    }


    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        onEventMainThread(event.getMessage());
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
                uiConversation.setConversationContent(UIConversation.buildConversationContent(uiConversation));
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
                }
            }
        } else {
            //没有对应记录，则新建一条记录插入列表。
            uiConversation = UIConversation.obtain(message);

            //如果是聚合显示，更新为聚合显示时的标题和内容
            boolean isGather = RongContext.getInstance().getConversationGatherState(message.getConversationType().getName());
            uiConversation.setConversationGatherState(isGather);
            if (isGather) {
                String name = ConversationListUtils.getInstance().setGatheredConversationTitle(message.getConversationType(), getActivity());
                uiConversation.setUIConversationTitle(name);
            }
        }
        return uiConversation;
    }

    public void onEventMainThread(Message message) {
        RLog.d(this, "onEventMainThread", "Receive Message: name=" + message.getObjectName() +
                ", type=" + message.getConversationType());

        if ((mSupportConversationList.size() != 0 && !mSupportConversationList.contains(message.getConversationType()))
                || (mSupportConversationList.size() == 0 && (message.getConversationType() == ConversationType.CHATROOM
                || message.getConversationType() == ConversationType.CUSTOMER_SERVICE))) {
            RLog.d(this, "onEventBackgroundThread", "Not included in conversation list. Return directly!");
            return;
        }

        if (mAdapter == null) {
            RLog.d(this, "onEventMainThread(Message)", "the conversation list adapter is null. Cache the received message firstly!!!");
            mMessageCache.add(message);
            return;
        }

        int originalIndex = mAdapter.findPosition(message.getConversationType(), message.getTargetId());
        RLog.d(this, "onEventBackgroundThread", "Event.Message, originalIndex is:" + originalIndex);

        UIConversation uiConversation = makeUiConversation(message, originalIndex);

        int newPosition = ConversationListUtils.getInstance().findPositionForNewConversation(uiConversation, mAdapter);

        if (originalIndex >= 0) {
            if (newPosition == originalIndex) {
                android.os.Message msg = new android.os.Message();
                msg.what = REFRESH_ITEM;
                msg.obj = newPosition;

                getHandler().removeMessages(REFRESH_ITEM, newPosition);
                getHandler().sendMessage(msg);
            } else {
                mAdapter.remove(originalIndex);
                mAdapter.add(uiConversation, newPosition);
                getHandler().sendEmptyMessage(REFRESH_LIST);
            }
        } else {
            mAdapter.add(uiConversation, newPosition);
            getHandler().sendEmptyMessage(REFRESH_LIST);
        }
    }

    public void onEventMainThread(MessageContent content) {
        RLog.d(ConversationListFragment.this, "onEventMainThread:", "MessageContent");

        for (int index = 0; index < mAdapter.getCount(); index++) {
            UIConversation tempUIConversation = mAdapter.getItem(index);
            if (tempUIConversation.getMessageContent().equals(content)) {
                tempUIConversation.setMessageContent(content);
                tempUIConversation.setConversationContent(tempUIConversation.buildConversationContent(tempUIConversation));
                tempUIConversation.setShowDraftFlag(false);

                android.os.Message msg = new android.os.Message();
                msg.what = REFRESH_ITEM;
                msg.obj = index;
                getHandler().sendMessage(msg);
            }
        }
    }

    public void onEvent(final RongIMClient.ConnectionStatusListener.ConnectionStatus status) {

        RLog.d(this, "ConnectionStatus", status.toString());

        if (isResumed())
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (status == RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE) {
                        showNotification(getResources().getString(R.string.rc_notice_network_unavailable));
                    } else if (status == RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) {
                        showNotification(getResources().getString(R.string.rc_notice_tick));
                    } else if (status == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
                        hiddenNotification();
                        initFragment(getUri());
                    } else if (status == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED) {
                        showNotification(getResources().getString(R.string.rc_notice_disconnect));
                    }
                }
            });
    }

    public void onEventMainThread(Event.CreateDiscussionEvent createDiscussionEvent) {
        RLog.d(ConversationListFragment.this, "onEventBackgroundThread:", "createDiscussionEvent");
        UIConversation conversation = new UIConversation();
        conversation.setConversationType(ConversationType.DISCUSSION);
        if (createDiscussionEvent.getDiscussionName() != null)
            conversation.setUIConversationTitle(createDiscussionEvent.getDiscussionName());
        else
            conversation.setUIConversationTitle("");

        conversation.setConversationTargetId(createDiscussionEvent.getDiscussionId());
        conversation.setUIConversationTime(System.currentTimeMillis());

        boolean isGather = RongContext.getInstance().getConversationGatherState(ConversationType.DISCUSSION.getName());
        conversation.setConversationGatherState(isGather);

        //如果是聚合显示，更新为聚合显示时的标题和内容
        if (isGather) {
            String name = ConversationListUtils.getInstance().setGatheredConversationTitle(ConversationType.DISCUSSION, getActivity());
            conversation.setUIConversationTitle(name);
        }
        mAdapter.add(conversation, ConversationListUtils.getInstance().findPositionForNewConversation(conversation, mAdapter));
        getHandler().sendEmptyMessage(REFRESH_LIST);
    }

    public void onEventMainThread(Draft draft) {
        ConversationType curType = ConversationType.setValue(draft.getType());
        if (curType == null) {
            throw new IllegalArgumentException("the type of the draft is unknown!");
        }
        RLog.i(ConversationListFragment.this, "onEventMainThread(draft)", curType.getName());
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
        RLog.d(ConversationListFragment.this, "onEventMainThread", "Group: name=" + groupInfo.getName() + ", id=" + groupInfo.getId());
        if(groupInfo.getName() == null){
            return;
        }

        for (int i = 0; i < count; i++) {
            UIConversation item = mAdapter.getItem(i);
            if (item != null && item.getConversationType().equals(ConversationType.GROUP) &&
                    item.getConversationTargetId().equals(groupInfo.getId())) {
                boolean gatherState = RongContext.getInstance().getConversationGatherState(item.getConversationType().getName());
                if (gatherState) {
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    Spannable messageData = RongContext.getInstance()
                            .getMessageTemplate(item.getMessageContent().getClass())
                            .getContentSummary(item.getMessageContent());
                    if (item.getMessageContent() instanceof VoiceMessage) {
                        boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(item.getConversationType(), item.getConversationTargetId())
                                .getReceivedStatus().isListened();
                        if (isListened) {
                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_text_color_secondary)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_voice_color)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                    builder.append(groupInfo.getName()).append(" : ").append(messageData);
                    item.setConversationContent(builder);
                    if (groupInfo.getPortraitUri() != null)
                        item.setIconUrl(groupInfo.getPortraitUri());
                } else {
                    item.setUIConversationTitle(groupInfo.getName());
                    if (groupInfo.getPortraitUri() != null)
                        item.setIconUrl(groupInfo.getPortraitUri());
                }
                mAdapter.getView(i, mList.getChildAt(i - mList.getFirstVisiblePosition()), mList);
            }
        }
    }

    public void onEventMainThread(Discussion discussion) {
        int count = mAdapter.getCount();
        RLog.d(ConversationListFragment.this, "onEventMainThread", "Discussion: name=" + discussion.getName() + ", id=" + discussion.getId());

        for (int i = 0; i < count; i++) {
            UIConversation item = mAdapter.getItem(i);
            if (item != null && item.getConversationType().equals(ConversationType.DISCUSSION) &&
                    item.getConversationTargetId().equals(discussion.getId())) {
                boolean gatherState = RongContext.getInstance().getConversationGatherState(item.getConversationType().getName());
                if (gatherState) {
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    Spannable messageData = RongContext.getInstance()
                            .getMessageTemplate(item.getMessageContent().getClass())
                            .getContentSummary(item.getMessageContent());

                    if (messageData != null) {
                        if (item.getMessageContent() instanceof VoiceMessage) {
                            boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(item.getConversationType(), item.getConversationTargetId())
                                    .getReceivedStatus().isListened();
                            if (isListened) {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_text_color_secondary)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(R.color.rc_voice_color)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                        builder.append(discussion.getName()).append(" : ").append(messageData);
                    } else {
                        builder.append(discussion.getName());
                    }

                    item.setConversationContent(builder);
                } else {
                    item.setUIConversationTitle(discussion.getName());
                }
                mAdapter.getView(i, mList.getChildAt(i - mList.getFirstVisiblePosition()), mList);
            }
        }
    }

    /* function: 异步获取到用户信息后，在此进行相应刷新*/
    public void onEventMainThread(UserInfo userInfo) {
        int count = mAdapter.getCount();
        boolean isShowName;

        if(userInfo.getName() == null){
            return;
        }

        for (int i = 0; i < count; i++) {
            UIConversation temp = mAdapter.getItem(i);
            String type = temp.getConversationType().getName();
            boolean gatherState = RongContext.getInstance().getConversationGatherState(temp.getConversationType().getName());

            if (temp.getMessageContent() == null) {
                isShowName = false;
            } else {
                isShowName = RongContext.getInstance().getMessageProviderTag(temp.getMessageContent().getClass()).showSummaryWithName();
            }
            //群组或讨论组非聚合显示情况，需要比较该会话的senderId.因为此时的targetId为群组或讨论组名字。
            if (!gatherState && isShowName && (type.equals("group") || type.equals("discussion"))
                    && (temp.getConversationSenderId().equals(userInfo.getUserId()))) {
                Spannable messageData = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
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
                mAdapter.getView(i, mList.getChildAt(i - mList.getFirstVisiblePosition()), mList);
            } else if (temp.getConversationTargetId().equals(userInfo.getUserId())) {
                if (!gatherState && (type == "private" || type == "system")) {
                    temp.setUIConversationTitle(userInfo.getName());
                    temp.setIconUrl(userInfo.getPortraitUri());
                } else if (isShowName) {
                    Spannable messageData = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
                    SpannableStringBuilder builder = new SpannableStringBuilder();

                    if (messageData != null) {
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
                    } else {
                        builder.append(userInfo.getName());
                    }
                    temp.setConversationContent(builder);
                    temp.setIconUrl(userInfo.getPortraitUri());
                }
                mAdapter.getView(i, mList.getChildAt(i - mList.getFirstVisiblePosition()), mList);
            }
        }
    }

    public void onEventMainThread(PublicServiceInfo accountInfo) {
        int count = mAdapter.getCount();
        boolean gatherState = RongContext.getInstance().getConversationGatherState(accountInfo.getConversationType().getName());
        for (int i = 0; i < count; i++) {
            if (mAdapter.getItem(i).getConversationType().equals(accountInfo.getConversationType())
                    && mAdapter.getItem(i).getConversationTargetId().equals(accountInfo.getTargetId())
                    && !gatherState) {
                mAdapter.getItem(i).setUIConversationTitle(accountInfo.getName());
                mAdapter.getItem(i).setIconUrl(accountInfo.getPortraitUri());
                mAdapter.getView(i, mList.getChildAt(i - mList.getFirstVisiblePosition()), mList);
                break;
            }
        }

    }

    public void onEventMainThread(Event.PublicServiceFollowableEvent event) {
        if (event != null) {
            if (event.isFollow() == false) {
                int originalIndex = mAdapter.findPosition(event.getConversationType(), event.getTargetId());
                if (originalIndex >= 0) {
                    mAdapter.remove(originalIndex);
                    getHandler().sendEmptyMessage(REFRESH_LIST);
                }
            }
        }
    }

    public void onEventMainThread(Event.ConversationUnreadEvent unreadEvent) {
        final int targetIndex = mAdapter.findPosition(unreadEvent.getType(), unreadEvent.getTargetId());

        if (targetIndex >= 0) {
            final UIConversation temp = mAdapter.getItem(targetIndex);
            boolean gatherState = temp.getConversationGatherState();
            if (gatherState) {
                RongIM.getInstance().getRongIMClient().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer count) {
                        temp.setUnReadMessageCount(count);
                        mAdapter.getView(targetIndex, mList.getChildAt(targetIndex - mList.getFirstVisiblePosition()), mList);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {
                        System.err.print("Throw exception when get unread message count from ipc remote side!");
                    }
                }, unreadEvent.getType());
            } else {
                temp.setUnReadMessageCount(0);
                mAdapter.getView(targetIndex, mList.getChildAt(targetIndex - mList.getFirstVisiblePosition()), mList);
            }
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
                newIndex = ConversationListUtils.getInstance().findPositionForCancleTop(originalIndex, mAdapter);
                Toast.makeText(getActivity(), getString(R.string.rc_group_list_popup_cancel_top), Toast.LENGTH_SHORT).show();
            } else {
                temp.setTop(true);
                newIndex = ConversationListUtils.getInstance().findPositionForSetTop(temp, mAdapter);
                Toast.makeText(getActivity(), getString(R.string.rc_group_list_dialog_set_top), Toast.LENGTH_SHORT).show();
            }
            if (originalIndex == newIndex)
                mAdapter.getView(newIndex, mList.getChildAt(newIndex - mList.getFirstVisiblePosition()), mList);
            else {
                mAdapter.remove(originalIndex);
                mAdapter.add(temp, newIndex);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            throw new IllegalAccessException("the item has already been deleted!");
        }
    }

    public void onEventMainThread(Event.ConversationRemoveEvent removeEvent) {
        final int removedIndex = mAdapter.findPosition(removeEvent.getType(), removeEvent.getTargetId());

        boolean gatherState = RongContext.getInstance().getConversationGatherState(removeEvent.getType().getName());

        if (!gatherState) {
            if (removedIndex >= 0) {
                mAdapter.remove(removedIndex);
                getHandler().sendEmptyMessage(REFRESH_LIST);
            }
        } else {
            if (removedIndex >= 0) {
                RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversationList) {
                        android.os.Message msg = new android.os.Message();
                        msg.what = REFRESH_GROUP;
                        msg.obj = conversationList;
                        msg.arg1 = removedIndex;
                        getHandler().sendMessage(msg);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {

                    }
                }, removeEvent.getType());
            }
        }

    }

    public void onEventMainThread(Event.MessageDeleteEvent event) {
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (event.getMessageIds().contains(mAdapter.getItem(i).getLatestMessageId())) {
                final boolean gatherState = mAdapter.getItem(i).getConversationGatherState();
                final int index = i;

                if (gatherState) {
                    RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                        @Override
                        public void onSuccess(List<Conversation> conversationList) {
                            android.os.Message msg = new android.os.Message();
                            msg.what = REFRESH_GROUP;
                            msg.obj = conversationList;
                            msg.arg1 = index;
                            getHandler().sendMessage(msg);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {

                        }
                    }, mAdapter.getItem(index).getConversationType());

                } else {
                    RongIM.getInstance().getRongIMClient().getConversation(mAdapter.getItem(index).getConversationType(), mAdapter.getItem(index).getConversationTargetId(),
                            new RongIMClient.ResultCallback<Conversation>() {
                                @Override
                                public void onSuccess(Conversation conversation) {
                                    if(conversation == null) {
                                        RLog.d(this, "onEventMainThread", "getConversation : onSuccess, conversation = null");
                                        return;
                                    }

                                    android.os.Message msg = new android.os.Message();
                                    msg.what = REFRESH_MSG;
                                    msg.obj = conversation;
                                    msg.arg1 = index;
                                    getHandler().sendMessage(msg);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode e) {

                                }
                            });
                }

                break;
            }

        }

    }

    public void onEventMainThread(Event.ConversationNotificationEvent notificationEvent) {
        final int originalIndex = mAdapter.findPosition(notificationEvent.getConversationType(), notificationEvent.getTargetId());

        if (originalIndex >= 0) {
            mAdapter.getView(originalIndex, mList.getChildAt(originalIndex - mList.getFirstVisiblePosition()), mList);
        }
    }


    public void onEventMainThread(Event.MessagesClearEvent clearMessagesEvent) {
        int originalIndex = mAdapter.findPosition(clearMessagesEvent.getType(), clearMessagesEvent.getTargetId());

        if (clearMessagesEvent != null && originalIndex >= 0) {
            mAdapter.getItem(originalIndex).setMessageContent(null);
            mAdapter.getItem(originalIndex).setConversationContent(null);
            mAdapter.getItem(originalIndex).setSentStatus(null);
            mAdapter.getView(originalIndex, mList.getChildAt(originalIndex - mList.getFirstVisiblePosition()), mList);
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
        int index = mAdapter.findPosition(ConversationType.DISCUSSION, event.getDiscussionId());

        if (index >= 0) {
            mAdapter.remove(index);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(Event.QuitGroupEvent event) {
        final int index = mAdapter.findPosition(ConversationType.GROUP, event.getGroupId());
        boolean gatherState = RongContext.getInstance().getConversationGatherState(ConversationType.GROUP.getName());

        if (index >= 0 && gatherState) {
            RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                @Override
                public void onSuccess(List<Conversation> conversationList) {
                    android.os.Message msg = new android.os.Message();
                    msg.what = REFRESH_GROUP;
                    msg.obj = conversationList;
                    msg.arg1 = index;
                    getHandler().sendMessage(msg);
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            }, ConversationType.GROUP);
        } else {
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
        RLog.d("ConversationListFragment", "onEvent", "handleMessage，thread is:" + Thread.currentThread().getName());
        switch (msg.what) {
            case REFRESH_LIST:
                mAdapter.notifyDataSetChanged();
                break;
            case REFRESH_ITEM:
                if (msg.obj != null) {
                    int position = (int) msg.obj;
                    if (position >= mList.getFirstVisiblePosition())
                        mAdapter.getView(position, mList.getChildAt(position - mList.getFirstVisiblePosition()), mList);
                }
                break;
            case REFRESH_GROUP:
                List<Conversation> conversations = (List<Conversation>) msg.obj;
                int position = msg.arg1;
                if (conversations == null || conversations.size() == 0) {
                    mAdapter.remove(position);
                    mAdapter.notifyDataSetChanged();
                } else {
                    UIConversation temp = mAdapter.getItem(position);
                    int unreadCount = 0;
                    Conversation newest = conversations.get(0);
                    temp.setTop(false);
                    
                    for (Conversation conversation : conversations) {
                        if(conversation.isTop())
                            temp.setTop(true);
                        unreadCount = unreadCount + conversation.getUnreadMessageCount();
                        if (conversation.getReceivedTime() > newest.getReceivedTime())
                            newest = conversation;
                    }

                    temp.setMessageContent(newest.getLatestMessage());
                    temp.setUnReadMessageCount(unreadCount);

                    temp.setConversationSenderId(newest.getSenderUserId());
                    temp.setConversationTargetId(newest.getTargetId());
                    temp.setLatestMessageId(newest.getLatestMessageId());

                    temp.setConversationContent(temp.buildConversationContent(temp));

                    temp.setUIConversationTime(newest.getSentTime());
                    temp.setSentStatus(newest.getSentStatus());
                    temp.setShowDraftFlag(false);

                    mAdapter.getView(position, mList.getChildAt(position - mList.getFirstVisiblePosition()), mList);
                }
                break;
            case REFRESH_MSG:
                Conversation conversation = (Conversation) msg.obj;
                int index = msg.arg1;
                UIConversation temp = UIConversation.obtain(conversation, false);

                mAdapter.remove(index);

                int newPosition = ConversationListUtils.getInstance().findPositionForNewConversation(temp, mAdapter);

                mAdapter.add(temp, newPosition);
                mAdapter.notifyDataSetChanged();

                break;

        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiconversation = mAdapter.getItem(position);
        ConversationType type = uiconversation.getConversationType();
        if (RongContext.getInstance().getConversationGatherState(type.getName())) {
            RongIM.getInstance().startSubConversationList(getActivity(), type);
        } else {
            if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
                boolean isDefault = RongContext.getInstance().getConversationListBehaviorListener().onConversationClick(getActivity(), view, uiconversation);
                if (isDefault == true)
                    return;
            }
            uiconversation.setUnReadMessageCount(0);
            RongIM.getInstance().startConversation(getActivity(), type, uiconversation.getConversationTargetId(), uiconversation.getUIConversationTitle());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation UIConversation = mAdapter.getItem(position);
        String type = UIConversation.getConversationType().getName();

        if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationLongClick(view.getContext(), view, UIConversation);
            if (isDealt)
                return true;
        }
        if (RongContext.getInstance().getConversationGatherState(type) == false) {
            buildMultiDialog(UIConversation);
            return true;
        } else {
            buildSingleDialog(UIConversation);
            return true;
        }
    }

    private void buildMultiDialog(final UIConversation uiConversation) {

        String[] items = new String[2];

        if (uiConversation.isTop())
            items[0] = getActivity().getString(R.string.rc_group_list_dialog_cancel_top);
        else
            items[0] = getActivity().getString(R.string.rc_group_list_dialog_set_top);

        items[1] = getActivity().getString(R.string.rc_group_list_dialog_remove);

        ArraysDialogFragment.newInstance(uiConversation.getUIConversationTitle(), items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
            @Override
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    RongIM.getInstance().getRongIMClient().
                            setConversationToTop(uiConversation.getConversationType()
                                    , uiConversation.getConversationTargetId(), !uiConversation.isTop());
                } else if (which == 1) {
                    RongIM.getInstance().getRongIMClient().removeConversation(uiConversation.getConversationType()
                            , uiConversation.getConversationTargetId());

                }
            }
        }).show(getFragmentManager());

    }

    private void buildSingleDialog(final UIConversation uiConversation) {

        String[] items = new String[1];
        items[0] = getActivity().getString(R.string.rc_group_list_dialog_remove);

        ArraysDialogFragment.newInstance(uiConversation.getUIConversationTitle(), items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {

            @Override
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {

                RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        for (Conversation conversation : conversations) {
                            RongIM.getInstance().getRongIMClient().removeConversation(conversation.getConversationType(), conversation.getTargetId());
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }

                }, uiConversation.getConversationType());

            }

        }).show(getFragmentManager());

    }

    /*根据各会话类型的聚合属性，重组会话列表*/
    public void makeUiConversationList(List<Conversation> conversationList, Context context) {
        UIConversation uiCon = null;

        if (mMessageCache.size() != 0) {
            for (int i = 0; i < mMessageCache.size(); i++) {
                Message message = mMessageCache.get(i);
                int originalIndex = mAdapter.findPosition(message.getConversationType(), message.getTargetId());

                UIConversation uiConversation = makeUiConversation(message, originalIndex);

                int newPosition = ConversationListUtils.getInstance().findPositionForNewConversation(uiConversation, mAdapter);

                if (originalIndex >= 0) {
                    mAdapter.remove(originalIndex);
                }
                mAdapter.add(uiConversation, newPosition);
            }
            mMessageCache.clear();
        }
        //获取到的conversationList排序规律：首先是top会话，按时间顺序排列。然后非top会话也是按时间排列。
        for (Conversation conversation : conversationList) {
            Conversation.ConversationType conversationType = conversation.getConversationType();
            boolean gatherState = RongContext.getInstance().getConversationGatherState(conversationType.getName());
            int originalIndex = mAdapter.findPosition(conversationType, conversation.getTargetId());//判断该条会话是否已经在会话列表里建立。

            //如果聚合显示
            if (gatherState) {
                if (originalIndex < 0) {
                    //该会话类型的item没有建立
                    uiCon = UIConversation.obtain(conversation, true);
                    String title = ConversationListUtils.getInstance().setGatheredConversationTitle(conversationType, context);
                    uiCon.setUIConversationTitle(title);
                    uiCon.setIconUrl(null);
                    mAdapter.add(uiCon);
                } else {
                    //如果该会话类型的item已经存在，并且当前消息数据比item里存储的数据新，则更新该条item的数据
                    uiCon = mAdapter.getItem(originalIndex);
                    int newUnReadMsgCount = conversation.getUnreadMessageCount();
                    int oldUnReadMsgCount = uiCon.getUnReadMessageCount();
                    uiCon.setUnReadMessageCount(newUnReadMsgCount + oldUnReadMsgCount);

                    if (conversation.getReceivedTime() > uiCon.getUIConversationTime()) {
                        uiCon.setMessageContent(conversation.getLatestMessage());
                        uiCon.setConversationTargetId(conversation.getTargetId());
                        uiCon.setConversationSenderId(conversation.getSenderUserId());
                        uiCon.setUIConversationTime(conversation.getSentTime());
                        uiCon.setConversationContent(UIConversation.buildConversationContent(uiCon));
                    }
                }
            } else {
                if (originalIndex < 0) {
                    //会话不存在
                    uiCon = UIConversation.obtain(conversation, false);
                    mAdapter.add(uiCon);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        RLog.d(this, "ConversationListFragment", "onDestroy");
        RongContext.getInstance().getEventBus().unregister(this);
        getHandler().removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        RLog.d(this, "ConversationListFragment", "onPause");
        super.onPause();
    }


    @Override
    public boolean onBackPressed() {
        return false;
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
