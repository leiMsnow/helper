package com.tongban.im.activity.user;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.AddChildInfo;
import com.tongban.im.model.EditUser;
import com.tongban.im.model.UpdateChildInfo;
import com.tongban.im.widget.view.ClearEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改个人信息界面
 *
 * @author fushudi
 */
public class UpdatePersonalInfoActivity extends BaseToolBarActivity implements TextWatcher,
        View.OnClickListener {
    private ClearEditText cetUpdateNickName;
    private RelativeLayout rlUpdateSexBoy;
    private RelativeLayout rlUpdateSexGirl;
    private ImageView ivSelectBoy, ivSelectGirl;

    private String mNickName;
    private int mChildSex = 0;
    private EditUser editUser = new EditUser();


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_update_personal_info;
    }

    @Override
    protected void initView() {
        cetUpdateNickName = (ClearEditText) findViewById(R.id.et_update_nickname);
        rlUpdateSexBoy = (RelativeLayout) findViewById(R.id.rl_update_sex_boy);
        rlUpdateSexGirl = (RelativeLayout) findViewById(R.id.rl_update_sex_girl);
        ivSelectBoy = (ImageView) findViewById(R.id.iv_select_boy);
        ivSelectGirl = (ImageView) findViewById(R.id.iv_select_girl);
        //修改昵称
        if (getIntent().getStringExtra(Consts.KEY_UPDATE_PERSONAL_INFO).equals(Consts.KEY_UPDATE_NICKNAME)) {
            setTitle("昵称修改");
            cetUpdateNickName.setVisibility(View.VISIBLE);
        }
        //修改性别
        else if (getIntent().getStringExtra(Consts.KEY_UPDATE_PERSONAL_INFO).equals(Consts.KEY_UPDATE_SEX)) {
            setTitle("宝宝性别");
            rlUpdateSexBoy.setVisibility(View.VISIBLE);
            rlUpdateSexGirl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        cetUpdateNickName.addTextChangedListener(this);
        rlUpdateSexBoy.setOnClickListener(this);
        rlUpdateSexGirl.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_save) {
            //修改昵称
            if (getIntent().getStringExtra(Consts.KEY_UPDATE_PERSONAL_INFO).equals(Consts.KEY_UPDATE_NICKNAME)) {
                if (!TextUtils.isEmpty(mNickName)) {
                    editUser.setNick_name(mNickName);
                    UserCenterApi.getInstance().updateUserInfo(editUser, UpdatePersonalInfoActivity.this);
                    finish();
                }
            }
            //修改性别
            else if (getIntent().getStringExtra(Consts.KEY_UPDATE_PERSONAL_INFO).equals(Consts.KEY_UPDATE_SEX)) {
                if (mChildSex!=0) {
                    String childBirthday = SPUtils.get(mContext, SPUtils.VISIT_FILE,
                            Consts.CHILD_BIRTHDAY, "").toString();
                    AddChildInfo childInfo = new AddChildInfo();
                    childInfo.setBirthday(childBirthday);
                    childInfo.setSex(mChildSex);
                    List<AddChildInfo> children = new ArrayList<>();
                    children.add(childInfo);
                    UserCenterApi.getInstance().setChildInfo(SPUtils.get(mContext, Consts.USER_ID, "")
                            .toString(), children, null);
                    finish();
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mNickName = cetUpdateNickName.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        //修改性别为：男
        if (v == rlUpdateSexBoy) {
            ivSelectBoy.setVisibility(View.VISIBLE);
            ivSelectGirl.setVisibility(View.GONE);
            mChildSex = 1;
        }
        //修改性别为：女
        else if (v == rlUpdateSexGirl) {
            ivSelectGirl.setVisibility(View.VISIBLE);
            ivSelectBoy.setVisibility(View.GONE);
            mChildSex = 2;
        }
    }
}
