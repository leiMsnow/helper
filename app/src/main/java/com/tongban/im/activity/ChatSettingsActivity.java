package com.tongban.im.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.MemberGridAdapter;
import com.tongban.im.model.Group;
import com.tongban.im.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天设置
 */
public class ChatSettingsActivity extends BaseToolBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private GridView gvMembers;
    //圈子信息
    private TextView tvGroupName, tvAddress, tvCreator, tvAttrs, tvDesc;
    private ImageView ivCreator;
    //圈子设置
    private CheckBox chbRemind, chbTop;
    //各条目父控件
    private View vGroupName, vAddress, vDesc, vClear, vInform;
    private Button btnQuit;

    private MemberGridAdapter mMemberGridAdapter;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat_settings;
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

        Glide.with(ChatSettingsActivity.this).
                load("http://www.qjis.com/uploads/allimg/120918/11305V125-21.jpg").into(ivCreator);

        List<User> users = new ArrayList<>();
        int i = 0;
        while (true) {
            if (i > 10)
                break;

            User user = new User();
            user.setNick_name("张三" + i);
            user.setPortrait_url("http://www.qjis.com/uploads/allimg/120918/11305V125-21.jpg");
            if (i % 2 == 0) {
                user.setPortrait_url("http://diy.qqjay.com/u2/2012/0601/caafdf4fe2eee3b7f397922b575f46af.jpg");
            }
            users.add(user);
            i++;
        }
        mMemberGridAdapter = new MemberGridAdapter(mContext, R.layout.item_member_grid, users);
        gvMembers.setAdapter(mMemberGridAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == btnQuit) {
            ToastUtil.getInstance(mContext).showToast("就不退出");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
