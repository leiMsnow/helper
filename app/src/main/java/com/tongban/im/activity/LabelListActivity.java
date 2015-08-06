package com.tongban.im.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

public class LabelListActivity extends BaseToolBarActivity implements View.OnClickListener{

    private FlowLayout flLabelList;
    private TextView tvLabelName;

    private String labelNameList[] = {"同龄圈", "达人圈", "生活圈", "男宝宝", "女宝宝",
            "混血宝宝", "宝宝知识圈", "你说什么圈",  "同龄圈","给你什么圈",
            "达人圈", "生活圈", "混血宝宝", "同龄圈", "达人圈"};

    @Override
    protected int getLayoutRes() {
        setTitle("圈子标签");
        return R.layout.activity_lable_list;
    }

    @Override
    protected void initView() {
        flLabelList = (FlowLayout) findViewById(R.id.fl_label_list);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < labelNameList.length; i++) {
            tvLabelName = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_label_list, flLabelList, false);
            tvLabelName.setText(labelNameList[i]);
            flLabelList.addView(tvLabelName);
        }
    }

    @Override
    protected void initListener() {
        flLabelList.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_label, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v==flLabelList){
            ToastUtil.getInstance(mContext).showToast("");
        }
    }
}
