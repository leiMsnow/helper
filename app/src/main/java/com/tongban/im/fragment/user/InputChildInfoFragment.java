package com.tongban.im.fragment.user;


import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.model.BaseEvent;

import java.util.Calendar;

import de.greenrobot.event.EventBus;


public class InputChildInfoFragment extends BaseApiFragment implements TextWatcher, View.OnClickListener {
    private EditText etChildNickName;
    private TextView tvChildBirthday;
    private Button btnSubmit;

    private String mChildNickName, mChildBirthday, mChildSex;
    private DatePickerDialog mDatePickerDialog;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_input_child_info;
    }

    @Override
    protected void initView() {
        etChildNickName = (EditText) mView.findViewById(R.id.et_input_nickname);
        tvChildBirthday = (TextView) mView.findViewById(R.id.tv_input_birthday);
        btnSubmit = (Button) mView.findViewById(R.id.btn_submit);
    }

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(mChildBirthday) || TextUtils.isEmpty(mChildNickName)) {

        }
    }

    @Override
    protected void initListener() {
        tvChildBirthday.setOnClickListener(this);
        tvChildBirthday.addTextChangedListener(this);
        etChildNickName.addTextChangedListener(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseEvent.ChildCreateEvent childCreateEvent = new BaseEvent.ChildCreateEvent();
                childCreateEvent.childName = mChildNickName;
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
        mChildNickName = etChildNickName.getText().toString().trim();
        mChildBirthday = tvChildBirthday.getText().toString().trim();
        if (!TextUtils.isEmpty(mChildNickName) && !TextUtils.isEmpty(mChildBirthday)) {
            btnSubmit.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvChildBirthday) {
            openDatePicker();
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
        mDatePickerDialog.show();
    }
}
