package com.tongban.im.activity.group;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.BaseDialog;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.MemberGridAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.group.Group;
import com.tongban.im.model.user.User;
import com.tongban.im.widget.view.ChildGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群组信息/设置
 */
public class GroupInfoActivity extends BaseToolBarActivity {


    @Bind(R.id.gv_members)
    ChildGridView gvMembers;
    @Bind(R.id.tv_group_name)
    TextView tvGroupName;
    @Bind(R.id.tv_group_address)
    TextView tvAddress;
    @Bind(R.id.iv_group_creator)
    CircleImageView ivCreator;
    @Bind(R.id.tv_group_creator)
    TextView tvCreator;
    @Bind(R.id.tv_group_attrs)
    TextView tvAttrs;
    @Bind(R.id.btn_quit)
    Button btnQuit;
    @Bind(R.id.ll_settings_parent)
    LinearLayout llSettingsParent;
    @Bind(R.id.sl_parent)
    ScrollView mParent;

    private MemberGridAdapter mMemberGridAdapter;
    private Group mGroup;

    private String mGroupId;
    //根据此值来判断是否显示设置圈子信息 true,未加入；false已经加入
    private boolean mAllowAdd = true;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_group_info;
    }

    @Override
    protected void initData() {
        if (getIntent().getData() != null) {
            Uri uri = getIntent().getData();
            mGroupId = uri.getQueryParameter(Consts.KEY_GROUP_ID);
            mAllowAdd = getIntent().getBooleanExtra(Consts.KEY_IS_JOIN, false);
            GroupApi.getInstance().getGroupInfo(mGroupId, this);

        }
        mMemberGridAdapter = new MemberGridAdapter(mContext, R.layout.item_member_grid, null);
        gvMembers.setAdapter(mMemberGridAdapter);
    }

    @OnClick(R.id.btn_quit)
    public void onClick(View v) {
        if (v == btnQuit) {
            if (mGroup == null) {
                return;
            }
            if (SPUtils.get(mContext, Consts.USER_ID, "").equals(mGroup.getUser_info().getUser_id())) {
                ToastUtil.getInstance(mContext).showToast(getQuitMessage());
                return;
            }
            BaseDialog.Builder dialog = new BaseDialog.Builder(mContext);
            dialog.setMessage("退出圈子?");
            dialog.setPositiveButton("我要退出",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            GroupApi.getInstance().quitGroup(mGroupId, GroupInfoActivity.this);
                        }
                    });
            dialog.setNegativeButton("还是留下吧",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
            dialog.show();
        }
    }

    public void onEventMainThread(BaseEvent.GroupInfoEvent groupInfo) {
        mGroup = groupInfo.group;
        if (mGroup != null) {
            GroupApi.getInstance().getGroupMembersList(mGroupId, 0, 15, this);
            mParent.setVisibility(View.VISIBLE);
            if (!mAllowAdd) {
                llSettingsParent.setVisibility(View.VISIBLE);
            }
        } else {
            mParent.setVisibility(View.GONE);
        }
        tvGroupName.setText(mGroup.getGroup_name());
        tvAddress.setText(mGroup.getAddress());
        tvAttrs.setText(mGroup.getGroupType());

        if (mGroup.getUser_info() != null && mGroup.getUser_info().getPortrait_url() != null) {
            setUserPortrait(mGroup.getUser_info().getPortrait_url().getMin(), ivCreator);
            tvCreator.setText(mGroup.getUser_info().getNick_name());
        }

    }

    public void onEventMainThread(BaseEvent.GroupMemberEvent obj) {
        mMemberGridAdapter.addAll(obj.users);
    }


    public void onEventMainThread(BaseEvent.QuitGroupEvent obj) {
        finish();
    }


    protected String getQuitMessage() {
        Random random = new Random();
        int count = mContext.getResources().
                getStringArray(R.array.quit_group).length;
        return mContext.getResources().getStringArray(R.array.quit_group)
                [random.nextInt(count)].toString();
    }
}
