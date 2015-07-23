package com.tongban.im.activity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

/**
 * 创建圈子界面
 *
 * @author fushudi
 */
public class CreateGroupActivity extends BaseToolBarActivity implements View.OnClickListener {
    private TextView tvSetGroupIcon;

    @Override
    protected int getLayoutRes() {
        setTheme(R.style.AppTheme_Blue);
        return R.layout.activity_create_group;
    }

    @Override
    protected void initView() {
        tvSetGroupIcon = (TextView) findViewById(R.id.tv_add_group_icon);
    }

    @Override
    protected void initListener() {
        tvSetGroupIcon.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == tvSetGroupIcon) {
        }
    }
}
