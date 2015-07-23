package com.tongban.im.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.GroupType;

/**
 * 创建圈子界面
 *
 * @author fushudi
 */
public class CreateGroupActivity extends BaseToolBarActivity implements View.OnClickListener {
    private TextView tvSetGroupIcon;
    private Button btnSubmit;
    private EditText etGroupName;

    private int mGroupType;

    @Override
    protected int getLayoutRes() {
        mGroupType = getIntent().getExtras().getInt(Consts.KEY_GROUP_TYPE,0);
        switch (mGroupType) {
            case GroupType.CITY:
                setTheme(R.style.AppTheme_Blue_Base);
                break;
            case GroupType.AGE:
                setTheme(R.style.AppTheme_Red_Base);
                break;
        }
        return R.layout.activity_create_group;
    }

    @Override
    protected void initView() {
        tvSetGroupIcon = (TextView) findViewById(R.id.tv_group_icon);
        btnSubmit = (Button) findViewById(R.id.btn_create);
        etGroupName = (EditText) findViewById(R.id.et_group_name);
    }

    @Override
    protected void initListener() {
        tvSetGroupIcon.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == tvSetGroupIcon) {
        } else if (v == btnSubmit) {
            String groupName=etGroupName.getText().toString().trim();
            if (TextUtils.isEmpty(groupName)){
                ToastUtil.getInstance(mContext).showToast("请输入圈子名称");
                return;
            }
            GroupApi.getInstance().createGroup(groupName,mGroupType, this);
        }
    }
}
