package com.tongban.im.activity.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.api.UserApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.GroupType;

/**
 * Created by zhangleilei on 15/7/8.
 */
public abstract class BaseToolBarActivity extends BaseApiActivity {

    protected Toolbar mToolbar;
    private View mEmptyView;

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            return;
        }
        mToolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onComplete(Object obj) {
        super.onComplete(obj);
        if (mEmptyView != null) {
            if (mEmptyView.getVisibility() == View.VISIBLE) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(getEmptyView(), "alpha", 1.0f, 0.0f).setDuration(500);
                objectAnimator.start();
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        getEmptyView().setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    @Override
    public void onFailure(DisplayType displayType, Object errorObj) {
        super.onFailure(displayType, errorObj);
        String errorMsg = "";

        if (errorObj instanceof ApiResult) {
            errorMsg = ((ApiResult) errorObj).getStatusDesc();
        } else if (errorObj instanceof String) {
            errorMsg = errorObj.toString();
        }
        if (TextUtils.isEmpty(errorMsg) || errorMsg.contains("volley")) {
            errorMsg = "网络异常，请稍后重试";
        }
        if (displayType == DisplayType.Toast) {
            ToastUtil.getInstance(mContext).showToast(errorMsg);
        } else if (displayType == DisplayType.View) {
            createEmptyView(errorMsg);
        } else if (displayType == DisplayType.ALL) {
            ToastUtil.getInstance(mContext).showToast(errorMsg);
            createEmptyView(errorMsg);
        }
    }

    /**
     * 创建空数据布局
     *
     * @param msg 提示信息
     */
    private void createEmptyView(final String msg) {
        mEmptyView = this.findViewById(com.tongban.corelib.R.id.rl_empty_view);
        if (mEmptyView != null) {
            mEmptyView.setVisibility(mEmptyView.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0.0f, 1.0f).setDuration(500);
            objectAnimator.start();
        } else {
            mEmptyView = LayoutInflater.from(mContext).inflate(com.tongban.corelib.R.layout.view_empty, null);
            TextView tvMsg = (TextView) mEmptyView.findViewById(com.tongban.corelib.R.id.tv_empty_msg);
            tvMsg.setText(msg);
            tvMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.getInstance(mContext).showToast(msg);
                    // 将失败请求队列里的请求重新加入Volley队列
                    if (getFailedRequest().size() > 0) {
                        for (Request request : getFailedRequest()) {
                            UserApi.getInstance().getRequestQueue().add(request);
                        }
                    }
                }
            });

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0.0f, 1.0f).setDuration(500);
            objectAnimator.start();
            this.addContentView(mEmptyView, layoutParams);
        }
    }


    protected void setToolbarTheme(int type) {
        switch (type) {
            case GroupType.CITY:
                setTheme(R.style.AppTheme_DeepPurple_Base);
                break;
            case GroupType.AGE:
                setTheme(R.style.AppTheme_Pink_Base);
                break;
            case GroupType.LIFE:
                setTheme(R.style.AppTheme_LightGreen_Base);
                break;
            case GroupType.CLASSMATE:
                setTheme(R.style.AppTheme_LightBlue_Base);
                break;
            case GroupType.ACTIVITY:
                setTheme(R.style.AppTheme_Yellow_Base);
                break;
        }
    }

}
