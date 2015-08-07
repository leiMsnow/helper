package com.tongban.im.activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

public class LabelListActivity extends BaseToolBarActivity implements View.OnClickListener {
    private FlowLayout flLabelList;
    private String labelNameList[] = {"同龄圈1", "达人圈2", "生活圈3", "男宝宝4", "女宝宝5",
            "混血宝宝6", "宝宝知识圈7", "你说什么圈8", "同龄圈9", "给你什么圈10",
            "达人圈11", "生活圈12", "混血宝宝13", "同龄圈14", "达人圈15"};

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
            TextView btnLabelName = (TextView) LayoutInflater.from(mContext).
                    inflate(R.layout.item_label_list, flLabelList, false);
            btnLabelName.setText(labelNameList[i]);
            btnLabelName.setTag(labelNameList[i]);
            btnLabelName.setOnClickListener(this);
            flLabelList.addView(btnLabelName);
        }
    }

    @Override
    protected void initListener() {
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
        if (v.getId() == R.id.btn_label_name) {
            ToastUtil.getInstance(mContext).showToast(v.getTag().toString());
        }
        for (int i = 0; i < labelNameList.length; i++) {
            if (v.getTag().equals(labelNameList[i]) && !v.isSelected()) {
                Log.d("onClick", "!isSelected");
                v.setSelected(true);

            } else if (v.getTag().equals(labelNameList[i]) && v.isSelected()) {
                Log.d("onClick", "isSelected");
                v.setSelected(false);
            }
        }
    }
}
