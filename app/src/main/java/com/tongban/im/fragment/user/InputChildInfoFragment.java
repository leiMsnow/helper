package com.tongban.im.fragment.user;


import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.model.BaseEvent;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * 输入宝宝信息界面
 *
 * @author fushudi
 */

public class InputChildInfoFragment extends BaseApiFragment implements TextWatcher, View.OnClickListener {
    private EditText etChildNickName;
    private TextView tvChildBirthday;
    private Button btnSubmit;
    private FrameLayout flContainerBoy, flContainerGirl;
    private CheckBox chbBoy, chbGirl;

    private String mChildBirthday;
    private int mChildSex = 0;
    private DatePickerDialog mDatePickerDialog;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_input_child_info;
    }

    @Override
    protected void initView() {
        etChildNickName = (EditText) mView.findViewById(R.id.et_input_nickname);
        tvChildBirthday = (TextView) mView.findViewById(R.id.tv_input_birthday);
        flContainerBoy = (FrameLayout) mView.findViewById(R.id.fl_container_boy);
        flContainerGirl = (FrameLayout) mView.findViewById(R.id.fl_container_girl);
        chbBoy = (CheckBox) mView.findViewById(R.id.chb_boy);
        chbGirl = (CheckBox) mView.findViewById(R.id.chb_girl);
        btnSubmit = (Button) mView.findViewById(R.id.btn_submit);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        tvChildBirthday.setOnClickListener(this);
        tvChildBirthday.addTextChangedListener(this);
        etChildNickName.addTextChangedListener(this);
        flContainerBoy.setOnClickListener(this);
        flContainerGirl.setOnClickListener(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseEvent.ChildCreateEvent childCreateEvent = new BaseEvent.ChildCreateEvent();
                childCreateEvent.childBirthday = mChildBirthday;
                childCreateEvent.childSex = mChildSex;
                EventBus.getDefault().post(childCreateEvent);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mChildBirthday = tvChildBirthday.getText().toString().trim();
        setBtnEnabled();
    }

    @Override
    public void onClick(View v) {
        if (v == tvChildBirthday) {
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
                    tvChildBirthday.setText(mChildBirthday);
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
