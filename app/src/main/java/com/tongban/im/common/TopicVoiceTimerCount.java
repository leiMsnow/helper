package com.tongban.im.common;

import android.os.CountDownTimer;
import android.widget.Button;

import com.tongban.im.App;
import com.tongban.im.R;

/**
 * 语音试听计时
 * Created by zhangleilei on 8/18/15.
 */
public class TopicVoiceTimerCount extends CountDownTimer {

    private Button btnCode;

    public TopicVoiceTimerCount(Button btnCode, long millisInFuture) {
        //参数依次为总时长,和计时的时间间隔
        super(millisInFuture, 1000);
        this.btnCode = btnCode;
    }

    @Override
    public void onFinish() {
        //计时完毕时触发
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //计时过程显示
        btnCode.setSelected(true);
        btnCode.setText((millisUntilFinished / 1000) + "\'\'");
    }
}


