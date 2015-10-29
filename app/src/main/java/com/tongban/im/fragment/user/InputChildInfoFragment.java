package com.tongban.im.fragment.user;


import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tb.api.model.BaseEvent;
import com.tongban.im.R;
import com.tongban.im.fragment.base.AppBaseFragment;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.greenrobot.event.EventBus;

/**
 * 输入宝宝信息界面
 *
 * @author fushudi
 */

public class InputChildInfoFragment extends AppBaseFragment {

    @Bind(R.id.tv_input_birthday)
    TextView tvInputBirthday;
    @Bind(R.id.chb_boy)
    CheckBox chbBoy;
    @Bind(R.id.fl_container_boy)
    FrameLayout flContainerBoy;
    @Bind(R.id.chb_girl)
    CheckBox chbGirl;
    @Bind(R.id.fl_container_girl)
    FrameLayout flContainerGirl;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    private int mChildSex = 0;
    private String mChildBirthday;
    private DatePickerDialog mDatePickerDialog;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_input_child_info;
    }

    @Override
    protected void initData() {

    }

    @OnTextChanged({R.id.et_input_nickname, R.id.tv_input_birthday})
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @OnTextChanged({R.id.et_input_nickname, R.id.tv_input_birthday})
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @OnTextChanged({R.id.et_input_nickname, R.id.tv_input_birthday})
    public void afterTextChanged(Editable s) {
        mChildBirthday = tvInputBirthday.getText().toString().trim();
        setBtnEnabled();
    }

    @OnClick({R.id.tv_input_birthday, R.id.fl_container_boy, R.id.fl_container_girl, R.id.btn_submit})
    public void onClick(View v) {
        if (v == tvInputBirthday) {
            openDatePicker();
        }
        //选择宝宝性别 - 男
        else if (v == flContainerBoy) {
            chbBoy.setVisibility(View.VISIBLE);
            chbGirl.setVisibility(View.GONE);
            mChildSex = 1;
            setBtnEnabled();

        }
        //选择宝宝性别 - 女
        else if (v == flContainerGirl) {
            chbGirl.setVisibility(View.VISIBLE);
            chbBoy.setVisibility(View.GONE);
            mChildSex = 2;
            setBtnEnabled();
        }
        // 提交
        else if (v == btnSubmit) {
            BaseEvent.ChildCreateEvent childCreateEvent = new BaseEvent.ChildCreateEvent();
            childCreateEvent.childBirthday = mChildBirthday;
            childCreateEvent.childSex = mChildSex;
            EventBus.getDefault().post(childCreateEvent);
        }
    }

    /**
     * 设置注册按钮可以点击
     */
    private void setBtnEnabled() {
        if (mChildSex != 0 && !TextUtils.isEmpty(mChildBirthday)) {
            btnSubmit.setEnabled(true);
        }
    }

    //打开时间选择器
    private void openDatePicker() {
        if (mDatePickerDialog == null) {
            Calendar c = Calendar.getInstance();
            mDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = String.valueOf(monthOfYear + 1);
                    String day = String.valueOf(dayOfMonth);

                    if (month.length() == 1) month = "0" + month;
                    if (day.length() == 1) day = "0" + day;

                    mChildBirthday = year + "-" + month + "-" + day;
                    tvInputBirthday.setText(mChildBirthday);
                }
            }, c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
        }
        Calendar max = Calendar.getInstance();
        max.add(Calendar.YEAR, 0);
        max.add(Calendar.MONTH, 0);
        max.add(Calendar.DAY_OF_MONTH, 0);
        mDatePickerDialog.getDatePicker().setMaxDate(max.getTime().getTime());
        mDatePickerDialog.show();
    }

}
