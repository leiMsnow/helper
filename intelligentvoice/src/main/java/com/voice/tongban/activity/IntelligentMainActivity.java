package com.voice.tongban.activity;

import android.view.View;

import com.tongban.corelib.base.activity.BaseToolBarActivity;
import com.voice.tongban.R;
import com.voice.tongban.fragment.VoiceInputFragment;
import com.voice.tongban.fragment.VoiceResultFragment;
import com.voice.tongban.model.VoiceTransfer;

public class IntelligentMainActivity extends BaseToolBarActivity {

    private VoiceInputFragment inputFragment;
    private VoiceResultFragment resultFragment;

    private View mClose;

    private boolean isResult = false;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_voice_layout;
    }

    @Override
    protected void initData() {
        setTitle("语音助手");
        mClose = findViewById(R.id.iv_close);
        if (getIntent() != null)
            isResult = getIntent().getBooleanExtra("result", false);
        inputFragment = VoiceInputFragment.getInstance();
        resultFragment = VoiceResultFragment.getInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, inputFragment)
                .add(R.id.fl_content, resultFragment)
                .commit();

        voiceTransfer(isResult);


        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void onEventMainThread(VoiceTransfer obj) {
        voiceTransfer(obj.isResult);
    }

    private void voiceTransfer(boolean isResult) {
        if (!isResult) {
            getSupportFragmentManager().beginTransaction()
                    .show(inputFragment)
                    .hide(resultFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .hide(inputFragment)
                    .show(resultFragment)
                    .commit();
        }
    }
}
