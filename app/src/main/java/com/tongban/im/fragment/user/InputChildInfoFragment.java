package com.tongban.im.fragment.user;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.model.BaseEvent;

import de.greenrobot.event.EventBus;


public class InputChildInfoFragment extends BaseApiFragment implements TextWatcher {
    private EditText etChildNickName, etChildBirthday;
    private Button btnSubmit;

    private String mChildNickName, mChildBirthday, mChildSex;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_input_child_info;
    }

    @Override
    protected void initView() {
        etChildNickName = (EditText) mView.findViewById(R.id.et_input_nickname);
        etChildBirthday = (EditText) mView.findViewById(R.id.et_input_birthday);
        btnSubmit = (Button) mView.findViewById(R.id.btn_submit);
    }

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(mChildBirthday) || TextUtils.isEmpty(mChildNickName)) {

        }
    }

    @Override
    protected void initListener() {
        etChildBirthday.addTextChangedListener(this);
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
        mChildBirthday = etChildBirthday.getText().toString().trim();
        if (!TextUtils.isEmpty(mChildNickName) && !TextUtils.isEmpty(mChildBirthday)) {
            btnSubmit.setEnabled(true);
        }
    }
}
