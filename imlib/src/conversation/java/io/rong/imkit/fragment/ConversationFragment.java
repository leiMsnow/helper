package io.rong.imkit.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.ConversationContext;
import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationInfo;
import io.rong.imkit.model.Event;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.SuspendMessage;

/**
 * Created by DragonJ on 14-8-2.
 */
public class ConversationFragment extends DispatchResultFragment {


    UriFragment mListFragment, mInputFragment;

    Conversation.ConversationType mConversationType;
    String mTargetId;
    ConversationInfo mCurrentConversationInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (ConversationContext.getInstance() == null) {
            ConversationContext.init(RongContext.getInstance());
        }

        RongContext.getInstance().getEventBus().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fr_conversation, container, false);
        return view;
    }

    @Override
    public void onResume() {

        RongContext.getInstance().getPushNotificationMng().onRemoveNotification();
        super.onResume();
    }

    @Override
    protected void initFragment(final Uri uri) {

        if (uri == null)
            return;

        List<String> paths = uri.getPathSegments();

        String typeStr = uri.getLastPathSegment().toUpperCase();
        mConversationType = io.rong.imlib.model.Conversation.ConversationType.valueOf(typeStr);
        mTargetId = uri.getQueryParameter("targetId");

        mCurrentConversationInfo = ConversationInfo.obtain(mConversationType, mTargetId);
        RongContext.getInstance().registerConversationInfo(mCurrentConversationInfo);


        mListFragment = (UriFragment) getChildFragmentManager().findFragmentById(android.R.id.list);
        mInputFragment = (UriFragment) getChildFragmentManager().findFragmentById(android.R.id.toggle);


        if(mListFragment.getUri()==null||!mListFragment.getUri().equals(uri))
            mListFragment.setUri(uri);

        if(mInputFragment.getUri()==null||!mInputFragment.getUri().equals(uri))
            mInputFragment.setUri(uri);

        if (paths.get(1).toLowerCase().equals("discussion") && !TextUtils.isEmpty(uri.getQueryParameter("targetIds"))) {
            String[] userIds = uri.getQueryParameter("targetIds").split(uri.getQueryParameter("delimiter"));


            if (userIds != null && userIds.length > 0) {
                final List<String> list = new ArrayList<String>();
                for (String item : userIds)
                    list.add(item);

                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        showNotification(getResources().getColor(R.color.rc_notice_normal), R.drawable.rc_ic_notice_loading, getResources().getString(R.string.rc_notice_create_discussion));
                        RongIM.getInstance().getRongIMClient().createDiscussion(uri.getQueryParameter("title"), list, new RongIMClient.CreateDiscussionCallback() {
                            @Override
                            public void onSuccess(String discussionId) {
                                Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().processName).buildUpon()
                                        .appendPath("conversation").appendPath(Conversation.ConversationType.DISCUSSION.getName().toLowerCase())
                                        .appendQueryParameter("targetId", discussionId).build();

                                RLog.i(this, "createDiscussion", discussionId);

                                setUri(uri);

                                if (mListFragment != null)
                                    mListFragment.setUri(uri);
                                if (mInputFragment != null)
                                    mInputFragment.setUri(uri);
                                hiddenNotification();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                hiddenNotification();
                                showNotification("创建失败");
                            }
                        });
                    }
                });
            }
        } else if (paths.get(1).toLowerCase().equals("chatroom")) {
            final String targetId = uri.getQueryParameter("targetId");

            if (TextUtils.isEmpty(targetId))
                return;

            getHandler().post(new Runnable() {

                @Override
                public void run() {

                    showNotification(getResources().getColor(R.color.rc_notice_normal), R.drawable.rc_ic_notice_loading, getResources().getString(R.string.rc_notice_enter_chatroom));

                    int pullCount = getResources().getInteger(R.integer.rc_chatroom_first_pull_message_count);

                    RongIM.getInstance().getRongIMClient().joinChatRoom(targetId, pullCount, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            hiddenNotification();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            hiddenNotification();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        RongContext.getInstance().unregisterConversationInfo(mCurrentConversationInfo);
        RongContext.getInstance().getEventBus().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (mConversationType == Conversation.ConversationType.CHATROOM)
            RongContext.getInstance().executorBackground(new Runnable() {
                @Override
                public void run() {
                    RongIM.getInstance().getRongIMClient().quitChatRoom(mTargetId, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            hiddenNotification();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            hiddenNotification();
                        }
                    });
                }
            });
        if(mConversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {
            RongContext.getInstance().executorBackground(new Runnable() {

                @Override
                public void run() {
                    SuspendMessage msg = new SuspendMessage();
                    RongIM.getInstance().getRongIMClient().sendMessage(mConversationType, mTargetId, msg, null, null);
                }
            });
        }

        super.onDestroy();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    public void onEventMainThread(Event.PublicServiceFollowableEvent event) {
        RLog.d(this, "onEventBackgroundThread", "PublicAccountIsFollowEvent, follow=" + event.isFollow());
        if(event != null && event.isFollow() == false) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(ConversationFragment.this).commitAllowingStateLoss();
            getActivity().finish();
        }
    }
}
