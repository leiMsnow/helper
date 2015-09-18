package com.tongban.im.activity.group;

import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.BaseDialog;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.MemberGridAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import java.util.Random;

/**
 * 群组信息/设置
 */
public class GroupInfoActivity extends BaseToolBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private GridView gvMembers;
    //圈子信息
    private TextView tvGroupName, tvAddress, tvCreator, tvAttrs, tvDesc;
    private ImageView ivCreator;
    //圈子设置
    private CheckBox chbRemind, chbTop;
    //各条目父控件
    private View vGroupName, vAddress, vDesc, vClear, vInform, vSettings;
    private Button btnQuit;

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
    protected void initView() {

        gvMembers = (GridView) findViewById(R.id.gv_members);

        tvGroupName = (TextView) findViewById(R.id.tv_group_name);
        tvAddress = (TextView) findViewById(R.id.tv_group_address);
        tvCreator = (TextView) findViewById(R.id.tv_group_creator);
        tvAttrs = (TextView) findViewById(R.id.tv_group_attrs);
        tvDesc = (TextView) findViewById(R.id.tv_group_desc);

        ivCreator = (ImageView) findViewById(R.id.iv_group_creator);

        chbRemind = (CheckBox) findViewById(R.id.chb_remind);
        chbTop = (CheckBox) findViewById(R.id.chb_top);

        vGroupName = findViewById(R.id.ll_name);
        vAddress = findViewById(R.id.ll_address);
        vDesc = findViewById(R.id.ll_desc);
        vClear = findViewById(R.id.ll_clear);
        vInform = findViewById(R.id.ll_inform);
        vSettings = findViewById(R.id.ll_settings_parent);

        btnQuit = (Button) findViewById(R.id.btn_quit);
    }

    @Override
    protected void initListener() {

        gvMembers.setOnItemClickListener(this);
        btnQuit.setOnClickListener(this);

        vGroupName.setOnClickListener(this);
        vAddress.setOnClickListener(this);
        vDesc.setOnClickListener(this);
        vClear.setOnClickListener(this);
        vInform.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        if (getIntent().getData() != null) {
            Uri uri = getIntent().getData();
            mGroupId = uri.getQueryParameter(Consts.KEY_GROUP_ID);
            mAllowAdd = getIntent().getBooleanExtra(Consts.KEY_IS_JOIN, false);
            if (!mAllowAdd) {
                vSettings.setVisibility(View.VISIBLE);
            }
            GroupApi.getInstance().getGroupInfo(mGroupId, this);
            GroupApi.getInstance().getGroupMembersList(mGroupId, 0, 15, this);
        }
        mMemberGridAdapter = new MemberGridAdapter(mContext, R.layout.item_member_grid, null);
        gvMembers.setAdapter(mMemberGridAdapter);
    }

    @Override
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void onEventMainThread(BaseEvent.GroupInfoEvent groupInfo) {
        mGroup = groupInfo.group;

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
