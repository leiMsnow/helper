package com.tongban.im.activity.base;

import android.view.View;
import android.widget.ImageView;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.model.BaseEvent;

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

    protected void setCollectEnable() {
        ivCollect.setEnabled(false);
        ivCollect.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivCollect.setEnabled(true);
            }
        }, 500);
    }

    /**
     * 收藏专题成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.CollectThemeEvent event) {
        ivCollect.setSelected(true);
        ToastUtil.getInstance(mContext).showToast("收藏成功");
    }

    /**
     * 取消收藏专题成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.NoCollectThemeEvent event) {
        ivCollect.setSelected(false);
        ToastUtil.getInstance(mContext).showToast("已取消收藏");
    }

    /**
     * 收藏商品成功的Event
     *
     * @param event CollectProductEvent
     */
    public void onEventMainThread(BaseEvent.CollectProductEvent event) {
        ivCollect.setSelected(true);
        ToastUtil.getInstance(mContext).showToast("已取消收藏");
    }

    /**
     * 取消收藏商品的Event
     *
     * @param event NoCollectProductEvent
     */
    public void onEventMainThread(BaseEvent.NoCollectProductEvent event) {
        ivCollect.setSelected(false);
        ToastUtil.getInstance(mContext).showToast("收藏成功");
    }

}
