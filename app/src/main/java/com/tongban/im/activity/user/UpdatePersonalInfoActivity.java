package com.tongban.im.activity.user;

import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.ClearEditText;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.user.AddChildInfo;
import com.tongban.im.model.user.EditUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 修改个人信息界面
 *
 * @author fushudi
 */
public class UpdatePersonalInfoActivity extends AppBaseActivity {

    @Bind(R.id.et_update_nickname)
    ClearEditText etUpdateNickName;
    @Bind(R.id.tv_update_sex_boy)
    TextView tvUpdateSexBoy;
    @Bind(R.id.iv_select_boy)
    ImageView ivSelectBoy;
    @Bind(R.id.rl_update_sex_boy)
    RelativeLayout rlUpdateSexBoy;
    @Bind(R.id.tv_update_sex_gir)
    TextView tvUpdateSexGir;
    @Bind(R.id.iv_select_girl)
    ImageView ivSelectGirl;
    @Bind(R.id.rl_update_sex_girl)
    RelativeLayout rlUpdateSexGirl;

    private String mNickName;
    private int mChildSex = 0;
    private EditUser editUser = new EditUser();


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_update_personal_info;
    }

    @Override
    protected void initData() {
        //修改昵称
        if (getIntent().getStringExtra(Consts.KEY_UPDATE_PERSONAL_INFO)
                .equals(Consts.KEY_UPDATE_NICKNAME)) {
            setTitle("昵称修改");
            etUpdateNickName.setVisibility(View.VISIBLE);
        }
        //修改性别
        else if (getIntent().getStringExtra(Consts.KEY_UPDATE_PERSONAL_INFO)
                .equals(Consts.KEY_UPDATE_SEX)) {
            setTitle("宝宝性别");
            rlUpdateSexBoy.setVisibility(View.VISIBLE);
            rlUpdateSexGirl.setVisibility(View.VISIBLE);
        }
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
            if (getIntent().getStringExtra(Consts.KEY_UPDATE_PERSONAL_INFO)
                    .equals(Consts.KEY_UPDATE_NICKNAME)) {
                if (!TextUtils.isEmpty(mNickName)) {
                    editUser.setNick_name(mNickName);
                    UserCenterApi.getInstance().updateUserInfo(editUser, UpdatePersonalInfoActivity.this);
                    finish();
                }
            }
            //修改性别
            else if (getIntent().getStringExtra(Consts.KEY_UPDATE_PERSONAL_INFO)
                    .equals(Consts.KEY_UPDATE_SEX)) {
                if (mChildSex != 0) {
                    String childBirthday = SPUtils.get(mContext, SPUtils.NO_CLEAR_FILE,
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

    @OnTextChanged(R.id.et_update_nickname)
    public void afterTextChanged(Editable s) {
        mNickName = etUpdateNickName.getText().toString().trim();
    }

    @OnClick({R.id.rl_update_sex_boy,R.id.rl_update_sex_girl})
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
