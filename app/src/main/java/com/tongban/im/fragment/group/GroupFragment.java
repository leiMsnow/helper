package com.tongban.im.fragment.group;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.tongban.corelib.TransferCenter;
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.activity.group.SearchGroupActivity;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 圈子页
 * author: chenenyu 15/7/13
 */
public class GroupFragment extends BaseApiFragment {
    // 圈子页顶部的tab
    private RadioGroup rgCircle;
    // 圈子页顶部的搜索按钮
    private ImageButton ibSearch;

    private FragmentManager fm;
    private ConversationListFragment chatFragment;
    private Fragment recommendFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initView() {
        // 圈子页的顶部tab
        rgCircle = (RadioGroup) mView.findViewById(R.id.rg_circle);
        ibSearch = (ImageButton) mView.findViewById(R.id.ib_search);

        fm = getChildFragmentManager();
        chatFragment = ConversationListFragment.getInstance();
        Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //私聊会话
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false") //群组
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false") //系统
                .build();
        chatFragment.setUri(uri);
        recommendFragment = new RecommendGroupFragment();
        fm.beginTransaction().add(R.id.fl_container, chatFragment).add(R.id.fl_container, recommendFragment)
                .hide(recommendFragment).commit();
    }

    @Override
    protected void initListener() {
        rgCircle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chat:
                        fm.beginTransaction().show(chatFragment).hide(recommendFragment).commit();
                        break;
                    case R.id.rb_recommend:
                        fm.beginTransaction().show(recommendFragment).hide(chatFragment).commit();
                        break;
                }
            }
        });

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferCenter.startGroupSearch(mContext,"圈");
//                startActivity(new Intent(mContext, SearchGroupActivity.class));
            }
        });
    }

    @Override
    protected void initData() {
    }

}
