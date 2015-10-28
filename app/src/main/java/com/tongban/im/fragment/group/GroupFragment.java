package com.tongban.im.fragment.group;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.tb.api.utils.TransferCenter;
import com.tb.api.utils.TransferPathPrefix;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.group.ChooseGroupTypeActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.base.BaseToolBarFragment;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 圈子页
 * author: chenenyu 15/7/13
 */
public class GroupFragment extends BaseToolBarFragment {

    @Bind(R.id.ib_search)
    ImageButton ibSearch;
    @Bind(R.id.ib_create)
    ImageButton ibCreate;
    @Bind(R.id.rg_parent)
    RadioGroup rgParent;
    @Bind(R.id.iv_indicator)
    ImageView ivIndicator;

    private ConversationListFragment chatFragment;
    private Fragment recommendFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initData() {

        rgParent.setVisibility(View.VISIBLE);
//        ivIndicator.setVisibility(View.VISIBLE);
        ibCreate.setVisibility(View.VISIBLE);

        //聊天界面
        chatFragment = ConversationListFragment.getInstance();
        Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                        //私聊
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                        //群组
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                        //系统
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")
                .build();
        chatFragment.setUri(uri);
        //推荐界面
        recommendFragment = RecommendGroupFragment.getInstance(true);
        getChildFragmentManager().beginTransaction()
                .add(R.id.fl_container, chatFragment)
                .add(R.id.fl_container, recommendFragment)
                .hide(recommendFragment)
                .commit();
        //没有登录删除聊天fragment
        if (TextUtils.isEmpty(SPUtils.get(mContext, Consts.USER_ID, "").toString())) {
            getChildFragmentManager().beginTransaction()
                    .hide(chatFragment)
                    .commit();
        }
        setIndicator(0);
    }

    @OnClick({R.id.ib_create, R.id.ib_search})
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

    @OnCheckedChanged({R.id.rb_chat, R.id.rb_recommend})
    public void onCheckedChanged(CompoundButton checkedBtn, boolean isChecked) {
        if (isChecked && checkedBtn.getId() == R.id.rb_chat) {
            if (!TextUtils.isEmpty(SPUtils.get(mContext, Consts.USER_ID, "").toString())) {
                getChildFragmentManager().beginTransaction()
                        .show(chatFragment)
                        .hide(recommendFragment)
                        .commit();
            }
            ibSearch.setVisibility(View.GONE);
            setIndicator(0);
        } else if (isChecked && checkedBtn.getId() == R.id.rb_recommend) {
            getChildFragmentManager().beginTransaction()
                    .show(recommendFragment)
                    .hide(chatFragment)
                    .commit();
            ibSearch.setVisibility(View.VISIBLE);
            setIndicator(1);
        }
    }

    // 设置指示器位置
    private void setIndicator(int position) {
        int mIndicatorWidth = ivIndicator.getWidth();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivIndicator.getLayoutParams();
        lp.leftMargin = (mIndicatorWidth * (position));
    }

}
