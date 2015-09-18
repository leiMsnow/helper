package com.tongban.im.common;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.tongban.im.App;
import com.tongban.im.R;

/**
 * 验证码倒计时
 * Created by zhangleilei on 8/18/15.
 */
public class VerifyTimerCount extends CountDownTimer {

    private Button btnCode;
    //倒计时时间，毫秒
    private static long millisInFuture = 6 * 10 * 1000;
    //倒计时隔间，毫秒
    private static long countDownInterval = 1 * 1000;

    public VerifyTimerCount(Button btnCode) {
        //参数依次为总时长,和计时的时间间隔
        super(millisInFuture, countDownInterval);
        this.btnCode = btnCode;
    }

    @Override
    public void onFinish() {
        //计时完毕时触发
        btnCode.setText(App.getInstance().getString(R.string.get_verify_code));
        btnCode.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //计时过程显示
        btnCode.setEnabled(false);
        btnCode.setText(millisUntilFinished / 1000 + "秒");
    }
}


