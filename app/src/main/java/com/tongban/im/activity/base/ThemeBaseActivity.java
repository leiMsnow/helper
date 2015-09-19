package com.tongban.im.activity.base;

import android.view.View;
import android.widget.ImageView;

import com.tongban.im.R;

/**
 * 商品base页
 *
 * @author Cheney
 * @date 8/20
 */
public abstract class ThemeBaseActivity extends BaseToolBarActivity implements View.OnClickListener {

    protected ImageView ivBack, ivShare, ivCollect;

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivCollect = (ImageView) findViewById(R.id.iv_collect);
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivCollect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            finish();
        }
    }
}
