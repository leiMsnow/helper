package io.rong.imkit.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.rong.imkit.ConversationConst;
import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.logic.LibException;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.ConversationTypeFilter;
import io.rong.imkit.widget.InputView;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;

/**
 * Created by DragonJ on 14/10/23.
 */
public class MessageListFragment extends UriFragment implements AbsListView.OnScrollListener {
    MessageListAdapter mAdapter;
    ListView mList;

    Conversation mConversation;

    static final int REQ_LIST = 1;
    static final int RENDER_LIST = 2;
    static final int RENDER_HISTORY = 6;
    static final int NOTIFY_LIST = 9;

    static final int REFRESH_LIST = 3;
    static final int SET_LAST = 10;
    static final int REFRESH_ITEM = 4;
    static final int REQ_HISTORY = 5;
    static final int SET_SMOOTH_SCROLL = 8;

    View mHeaderView;
    ConversationTypeFilter mFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RongContext.getInstance().getEventBus().register(this);
        mAdapter = new MessageListAdapter(getActivity());
    }


    @Override
    protected void initFragment(Uri uri) {

        String typeStr = uri.getLastPathSegment().toUpperCase();
        io.rong.imlib.model.Conversation.ConversationType type = io.rong.imlib.model.Conversation.ConversationType.valueOf(typeStr);

        String targetId = uri.getQueryParameter("targetId");

        String title = uri.getQueryParameter("title");

        if (TextUtils.isEmpty(targetId) || type == null)
            return;

        mConversation = Conversation.obtain(type, targetId, title);

        if (mAdapter != null) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        if (TextUtils.isEmpty(title)) {

            RongIM.getInstance().getRongIMClient().getConversation(mConversation.getConversationType(), mConversation.getTargetId(), new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation != null) {
                        if (!TextUtils.isEmpty(mConversation.getConversationTitle()))
                            conversation.setConversationTitle(mConversation.getConversationTitle());

                        mConversation = conversation;
                    }
                    getHandler().sendEmptyMessage(REQ_LIST);
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {
                    RLog.e(MessageListFragment.this, "fail", e.toString());
                }
            });
        } else {
            getHandler().sendEmptyMessage(REQ_LIST);
        }

        RongIM.getInstance().getRongIMClient().clearMessagesUnreadStatus(mConversation.getConversationType(), mConversation.getTargetId(), null);

        mFilter = ConversationTypeFilter.obtain(mConversation.getConversationType());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fr_messagelist, container, false);
        mList = findViewById(view, R.id.rc_list);
        mHeaderView = inflater.inflate(R.layout.rc_item_progress, null);
        mList.addHeaderView(mHeaderView);
        mList.setOnScrollListener(this);
        mList.setSelectionAfterHeaderView();

        mAdapter.setOnItemHandlerListener(new MessageListAdapter.OnItemHandlerListener() {

            @Override
            public void onWarningViewClick(int position, io.rong.imlib.model.Message data, View v) {

                MessageContent content = data.getContent();
                io.rong.imlib.model.Message message = io.rong.imlib.model.Message.obtain(data.getTargetId(), data.getConversationType(), content);
                RongIM.getInstance().getRongIMClient().deleteMessages(new int[]{data.getMessageId()});

                if (content instanceof ImageMessage) {
                    ImageMessage msg = (ImageMessage) content;
                    content = ImageMessage.obtain(msg.getLocalUri(), msg.getLocalUri());
                    message.setContent(content);
                    RongIM.getInstance().getRongIMClient().sendImageMessage(message, null, null);
                } else {
                    RongIM.getInstance().getRongIMClient().sendMessage(message, null, null);
                }

            }
        });

        return view;
    }


    boolean mIsLoading = true;
    boolean mHasMore = false;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0 && mAdapter.getCount() > 0 && !mIsLoading && mHasMore) {
            mIsLoading = true;
            getHandler().sendEmptyMessage(REQ_HISTORY);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActionBarHandler() != null) {
            getActionBarHandler().onTitleChanged(mConversation.getConversationTitle());
        }

        getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RLog.d(MessageListFragment.this, "View", "Touch");
                return false;
            }
        });

        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongContext.getInstance().getPrimaryInputProvider().onInactive(view.getContext());
                RongContext.getInstance().getSecondaryInputProvider().onInactive(view.getContext());
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    private List<io.rong.imlib.model.Message> filterMessage(List<io.rong.imlib.model.Message> srcList) {
        List<io.rong.imlib.model.Message> destList = null;
        if (mAdapter.getCount() > 0) {
            destList = new ArrayList<io.rong.imlib.model.Message>();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                for (io.rong.imlib.model.Message msg : srcList) {
                    if (destList.contains(msg))
                        continue;

                    if (msg.getMessageId() != mAdapter.getItem(i).getMessageId())
                        destList.add(msg);
                }
            }
        } else {
            destList = srcList;
        }

        return destList;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case RENDER_LIST:
                if (msg.obj instanceof List<?>) {
                    List<io.rong.imlib.model.Message> list = (List<io.rong.imlib.model.Message>) msg.obj;
                    if (list.size() < ConversationConst.CONFIG.DEF_LIST_COUNT) {
                        mHasMore = false;
                        mList.removeHeaderView(mHeaderView);
                        mHeaderView = null;
                    } else {
                        mHasMore = true;
                    }
                    mAdapter.addCollection(filterMessage(list));
                    mAdapter.notifyDataSetChanged();
                    getHandler().sendEmptyMessageDelayed(SET_LAST, 500);
                    RLog.d(MessageListFragment.this, "RENDER_LIST", "count:" + list.size());

                } else {
                    mHasMore = false;
                    mList.removeHeaderView(mHeaderView);
                    mHeaderView = null;
                }
                mIsLoading = false;
                break;
            case RENDER_HISTORY:
                RLog.d(this, "MessageListFragment", "RENDER_HISTORY");
                if (msg.obj instanceof List<?>) {
                    List<io.rong.imlib.model.Message> list = (List<io.rong.imlib.model.Message>) msg.obj;
                    if (list.size() < ConversationConst.CONFIG.DEF_LIST_COUNT) {
                        mHasMore = false;
                        mList.removeHeaderView(mHeaderView);
                        mHeaderView = null;
                    } else {
                        mHasMore = true;
                    }
                    Collections.reverse(list);

                    for (io.rong.imlib.model.Message item : list) {
                        mAdapter.add(item, 0);
                    }
                    int index = mList.getFirstVisiblePosition();

                    mAdapter.notifyDataSetChanged();

                    if (index == 0) {
                        mList.setSelection(list.size());
                    }

                } else {
                    mHasMore = false;
                    mList.removeHeaderView(mHeaderView);
                    mHeaderView = null;
                }
                mIsLoading = false;
                break;
            case REQ_LIST:
                mAdapter.clear();
                RongIM.getInstance().getRongIMClient().getLatestMessages(mConversation.getConversationType(), mConversation.getTargetId(), ConversationConst.CONFIG.DEF_LIST_COUNT, new RongIMClient.ResultCallback<List<io.rong.imlib.model.Message>>() {
                    @Override
                    public void onSuccess(List<io.rong.imlib.model.Message> messages) {
                        getHandler().obtainMessage(RENDER_LIST, messages).sendToTarget();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {
                        RLog.e(MessageListFragment.this, "IPC:getConversationMessage", e.toString());
                    }
                });
                break;
            case REFRESH_LIST:
                io.rong.imlib.model.Message model = (io.rong.imlib.model.Message) msg.obj;

                if (mAdapter.findPosition(model) < 0 && msg.obj instanceof io.rong.imlib.model.Message) {
                    mAdapter.add(model);
                }

                mAdapter.notifyDataSetChanged();
                RLog.d(MessageListFragment.this, "Handler", mList.getLastVisiblePosition() + ":" + mAdapter.getCount());
                getHandler().removeMessages(SET_LAST);
                getHandler().sendEmptyMessageDelayed(SET_LAST, 500);

                break;
            case SET_LAST:
                if (mAdapter.getCount() > 0)
                    mList.setSelection(mAdapter.getCount() - 1);

                break;
            case REFRESH_ITEM:
                int position = (Integer) msg.obj;

                if (position >= mList.getFirstVisiblePosition() && position <= mList.getLastVisiblePosition()) {
                    RLog.d(MessageListFragment.this, "REFRESH_ITEM", "Index:" + position);
                    mAdapter.getView(position, mList.getChildAt(position - mList.getFirstVisiblePosition() + mList.getHeaderViewsCount()), mList);
                }


                break;
            case REQ_HISTORY:
                RLog.d(this, "MessageListFragment", "REQ_HISTORY");

                io.rong.imlib.model.Message message = mAdapter.getItem(0);


                RongIM.getInstance().getRongIMClient().getHistoryMessages(mConversation.getConversationType(), mConversation.getTargetId(), message.getMessageId(), ConversationConst.CONFIG.DEF_LIST_COUNT, new RongIMClient.ResultCallback<List<io.rong.imlib.model.Message>>() {
                    @Override
                    public void onSuccess(List<io.rong.imlib.model.Message> messages) {
                        getHandler().obtainMessage(RENDER_HISTORY, messages).sendToTarget();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {
                        RLog.e(MessageListFragment.this, "IPC:getConversationMessage", e.toString());
                    }
                });
                break;

            case SET_SMOOTH_SCROLL:
                if (mList.getLastVisiblePosition() == mAdapter.getCount() - 2) {
                    mList.smoothScrollToPosition(mAdapter.getCount() - 1);
                }
                break;

            case NOTIFY_LIST:
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();

                break;
        }
        return false;
    }

    public void onEventMainThread(io.rong.imlib.model.Message message) {

        RLog.d(this, "onEventBackgroundThread", "message : " + message.toString());

        if (mConversation != null && mConversation.getTargetId().equals(message.getTargetId()) && mConversation.getConversationType() == message.getConversationType()) {
            int position = mAdapter.findPosition(message);
            if (position == -1) {
                RLog.d(this, "onEventBackgroundThread", "REFRESH_LIST : ");
                getHandler().obtainMessage(REFRESH_LIST, message).sendToTarget();
            } else {
                RLog.d(this, "onEventBackgroundThread", "REFRESH_ITEM : status=" + message.getSentStatus());
                mAdapter.getItem(position).setSentStatus(message.getSentStatus());
                mAdapter.getItem(position).setExtra(message.getExtra());
                getHandler().obtainMessage(REFRESH_ITEM, position).sendToTarget();
            }
        }

    }

    public void onEventMainThread(Event.OnMessageSendErrorEvent event) {
        io.rong.imlib.model.Message msg = event.getMessage();
        onEventMainThread(msg);
    }

    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        onEventMainThread(event.getMessage());
    }

    public void onEventMainThread(MessageContent messageContent) {

        if (mList != null && isResumed()) {
            int first = mList.getFirstVisiblePosition() - mList.getHeaderViewsCount();
            int last = mList.getLastVisiblePosition() - mList.getHeaderViewsCount();

            int index = first - 1;

            while (++index <= last && index >= 0 && index < mAdapter.getCount()) {
                if (mAdapter.getItem(index).getContent().equals(messageContent)) {
                    mAdapter.getView(index, mList.getChildAt(index - mList.getFirstVisiblePosition() + mList.getHeaderViewsCount()), mList);
                    break;
                }
            }
        }

    }

    public void onEventMainThread(InputView.Event event) {
        RLog.d(this, "Input_event", event.toString());
        if (event == InputView.Event.ACTION) {
            if (mAdapter == null)
                return;

            getHandler().sendEmptyMessageDelayed(SET_LAST, 500);
        }
    }

    public void onEventMainThread(UserInfo userInfo) {

        if (mList != null && isResumed()) {
            int first = mList.getFirstVisiblePosition() - mList.getHeaderViewsCount();
            int last = mList.getLastVisiblePosition() - mList.getHeaderViewsCount();

            int index = first - 1;

            while (++index <= last && index >= 0 && index < mAdapter.getCount()) {

                io.rong.imlib.model.Message message = mAdapter.getItem(index);

                if (message != null && (TextUtils.isEmpty(message.getSenderUserId()) || userInfo.getUserId().equals(message.getSenderUserId()))) {
                    mAdapter.getView(index, mList.getChildAt(index - mList.getFirstVisiblePosition() + mList.getHeaderViewsCount()), mList);
                }
            }
        }

    }

    public void onEventMainThread(PublicServiceInfo publicServiceInfo) {

        if (mList != null && isResumed() && mAdapter != null) {
            int first = mList.getFirstVisiblePosition() - mList.getHeaderViewsCount();
            int last = mList.getLastVisiblePosition() - mList.getHeaderViewsCount();

            int index = first - 1;

            while (++index <= last && index >= 0 && index < mAdapter.getCount()) {

                io.rong.imlib.model.Message message = mAdapter.getItem(index);

                if (message != null && (TextUtils.isEmpty(message.getTargetId()) || publicServiceInfo.getTargetId().equals(message.getTargetId()))) {
                    mAdapter.getView(index, mList.getChildAt(index - mList.getFirstVisiblePosition() + mList.getHeaderViewsCount()), mList);
                }
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        RongContext.getInstance().getEventBus().post(InputView.Event.INACTION);
    }

    @Override
    public void onResume() {
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

    public void onEventMainThread(LibException e) {
        showNotification(e.toString());
    }

    public void onEventMainThread(final RongIMClient.ConnectionStatusListener.ConnectionStatus status) {

        RLog.d(this, "ConnectionStatus", status.toString() + " " + this.toString());
        RLog.d(this, "ConnectionStatus", "isResume() = " + isResumed());
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

    public void onEventMainThread(Event.MessagesClearEvent clearMessagesEvent) {

        if (mConversation != null && clearMessagesEvent != null) {

            if (mConversation.getTargetId().equals(clearMessagesEvent.getTargetId())) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onEventMainThread(Event.ImageLoadSuccessEvent success) {
        if (mAdapter != null) {
            Log.d("ImageLoadSuccess","图片加载完成");
            mList.setSelection(mAdapter.getCount());
        }
    }


    public void onEventBackgroundThread(Event.MessageDeleteEvent deleteEvent) {
        if (deleteEvent.getMessageIds() != null) {
            boolean hasChanged = false;
            for (long item : deleteEvent.getMessageIds()) {
                int position = mAdapter.findPosition(item);
                if (position >= 0) {
                    mAdapter.remove(position);
                    hasChanged = true;
                }
            }

            if (hasChanged) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public void onEventBackgroundThread(Event.MessagesClearEvent clearEvent) {
        if (clearEvent.getTargetId().equals(mConversation.getTargetId()) && clearEvent.getType().equals(mConversation.getConversationType())) {
            mAdapter.removeAll();
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    public static class Builder {
        private Conversation.ConversationType conversationType;
        private String targetId;
        private Uri uri;

        public Conversation.ConversationType getConversationType() {
            return conversationType;
        }

        public void setConversationType(Conversation.ConversationType conversationType) {
            this.conversationType = conversationType;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

    }

    @Override
    public void onDestroy() {
        if (mConversation != null) {
            RongIM.getInstance().getRongIMClient().clearMessagesUnreadStatus(mConversation.getConversationType(), mConversation.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    RongContext.getInstance().getEventBus().post(new Event.ConversationUnreadEvent(mConversation.getConversationType(), mConversation.getTargetId()));
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {
                }
            });
        }
        RongContext.getInstance().getEventBus().unregister(this);
        super.onDestroy();
    }
}
