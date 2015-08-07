package com.tongban.im.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.activity.ChooseGroupTypeActivity;
import com.tongban.im.model.BaseEvent;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 圈子页
 * author: chenenyu 15/7/13
 */
public class CircleFragment extends BaseApiFragment {
    private FragmentManager fm;
    private ConversationListFragment chatFragment;
    private Fragment recommendFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView() {
        fm = getChildFragmentManager();
        chatFragment = ConversationListFragment.getInstance();
        Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //私聊会话
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false") //群组
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false") //系统
                .build();
        chatFragment.setUri(uri);
        recommendFragment = new RecommendCircleFragment();
        fm.beginTransaction().add(R.id.fl_container, chatFragment).add(R.id.fl_container, recommendFragment)
                .hide(recommendFragment).commit();
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_circle_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_group) {
            Intent intent = new Intent(mContext, ChooseGroupTypeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 切换顶部tab的Event
     *
     * @param type see{@link BaseEvent.SwitchCircleTabEvent}
     */
    public void onEventMainThread(BaseEvent.SwitchCircleTabEvent type) {
        if (type == BaseEvent.SwitchCircleTabEvent.CHAT) {
            fm.beginTransaction().show(chatFragment).hide(recommendFragment).commit();
        } else if (type == BaseEvent.SwitchCircleTabEvent.RECOMMEND) {
            fm.beginTransaction().show(recommendFragment).hide(chatFragment).commit();
        }
    }

}
