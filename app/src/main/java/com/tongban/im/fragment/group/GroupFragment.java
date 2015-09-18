package com.tongban.im.fragment.group;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.group.ChooseGroupTypeActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.common.TransferPathPrefix;
import com.tongban.im.fragment.base.BaseToolBarFragment;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 圈子页
 * author: chenenyu 15/7/13
 */
public class GroupFragment extends BaseToolBarFragment implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {
    // 圈子页顶部的tab
    private RadioGroup rgCircle;
    // 圈子页顶部的搜索按钮
    private ImageButton ibSearch;
    private ImageButton ibCreate;
    private ImageView ivIndicator;
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
        rgCircle.setVisibility(View.VISIBLE);
        ibSearch = (ImageButton) mView.findViewById(R.id.ib_search);
        ibSearch.setVisibility(View.GONE);
        ibCreate = (ImageButton) mView.findViewById(R.id.ib_create);
        ivIndicator = (ImageView) mView.findViewById(R.id.iv_indicator);
        ivIndicator.setVisibility(View.VISIBLE);
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
        Bundle bundle = new Bundle();
        bundle.putBoolean(Consts.KEY_IS_MAIN, true);
        recommendFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fl_container, chatFragment)
                .commit();
        //没有登录删除聊天fragment
        if (TextUtils.isEmpty(SPUtils.get(mContext, Consts.USER_ID, "").toString())) {
            fm.beginTransaction().remove(chatFragment).commit();
        }

    }

    @Override
    protected void initListener() {
        rgCircle.setOnCheckedChangeListener(this);
        ibSearch.setOnClickListener(this);
        ibCreate.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == rgCircle) {
            switch (checkedId) {
                case R.id.rb_chat:
                    if (!TextUtils.isEmpty(SPUtils.get(mContext, Consts.USER_ID, "").toString())) {
                        fm.beginTransaction().replace(R.id.fl_container, chatFragment).commit();
                    }
                    fm.beginTransaction().replace(R.id.fl_container, chatFragment).commit();
                    ibSearch.setVisibility(View.GONE);
                    setIndicator(0);
                    break;
                case R.id.rb_recommend:
                    fm.beginTransaction().replace(R.id.fl_container, recommendFragment).commit();
                    ibSearch.setVisibility(View.VISIBLE);
                    setIndicator(1);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ibCreate) {
            if (!TransferCenter.getInstance().startLogin())
                return;

            Intent intent = new Intent(mContext, ChooseGroupTypeActivity.class);
            startActivity(intent);
        } else if (v == ibSearch) {
            TransferCenter.getInstance().startSearch(TransferPathPrefix.SEARCH_GROUP, "city1");
        }
    }

    private void setIndicator(int position) {
        int mIndicatorWidth = ivIndicator.getWidth();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivIndicator.getLayoutParams();
        lp.leftMargin = (mIndicatorWidth * (position));
    }
}
