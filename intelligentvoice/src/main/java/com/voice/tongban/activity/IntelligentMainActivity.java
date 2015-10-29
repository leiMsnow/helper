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

    private boolean isResult = false;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_voice_layout;
    }

    @Override
    protected void initData() {
        setTitle("语音助手");
        if (getIntent() != null)
            isResult = getIntent().getBooleanExtra("result", false);
        inputFragment = VoiceInputFragment.getInstance();
        resultFragment = VoiceResultFragment.getInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, inputFragment)
                .add(R.id.fl_content, resultFragment)
                .commit();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        mToolbar.setVisibility(View.GONE);
        voiceTransfer(isResult);
    }

    public void onEventMainThread(VoiceTransfer obj) {
        voiceTransfer(obj.isResult);
    }

    private void voiceTransfer(boolean isResult) {
        if (!isResult) {
            mToolbar.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .show(inputFragment)
                    .hide(resultFragment)
                    .commit();
        } else {
            mToolbar.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .hide(inputFragment)
                    .show(resultFragment)
                    .commit();
        }
    }
}
