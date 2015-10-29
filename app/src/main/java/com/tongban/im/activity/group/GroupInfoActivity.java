package com.tongban.im.activity.group;

import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tb.api.GroupApi;
import com.tb.api.model.BaseEvent;
import com.tb.api.model.group.Group;
import com.tb.api.utils.ApiConstants;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.BaseDialog;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.adapter.MemberGridAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.widget.view.ChildGridView;

import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 群组信息/设置
 */
public class GroupInfoActivity extends AppBaseActivity {


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
            mGroupId = uri.getQueryParameter(ApiConstants.KEY_GROUP_ID);
            mAllowAdd = getIntent().getBooleanExtra(ApiConstants.KEY_IS_JOIN, false);
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
            if (getUserId().equals(mGroup.getUser_info().getUser_id())) {
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

        if (mGroup.getUser_info() != null
                && mGroup.getUser_info().getPortraitUrl() != null) {
            setUserPortrait(mGroup.getUser_info().getPortraitUrl().getMin(), ivCreator);
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
        String quitMessage[] = mContext.getResources().getStringArray(R.array.quit_group);
        Random random = new Random();
        int count = quitMessage.length;
        return quitMessage[random.nextInt(count)].toString();
    }
}
