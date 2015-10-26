package com.tongban.im.activity.base;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.model.BaseEvent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品base页
 *
 * @author Cheney
 * @date 8/20
 */
public abstract class ThemeBaseActivity extends AppBaseActivity {

    @Nullable
    @Bind(R.id.sl_parent)
    protected ScrollView slParent;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_collect)
    protected ImageView ivCollect;
    @Bind(R.id.iv_share)
    protected ImageView ivShare;

    @OnClick({R.id.iv_back})
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
        ToastUtil.getInstance(mContext).showToast("收藏成功");
    }

    /**
     * 取消收藏商品的Event
     *
     * @param event NoCollectProductEvent
     */
    public void onEventMainThread(BaseEvent.NoCollectProductEvent event) {
        ivCollect.setSelected(false);
        ToastUtil.getInstance(mContext).showToast("已取消收藏");
    }

}
