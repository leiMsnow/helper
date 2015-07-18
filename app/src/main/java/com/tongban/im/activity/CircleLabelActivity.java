package com.tongban.im.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.LabelAdapter;
import com.tongban.im.model.Label;

import java.util.ArrayList;
import java.util.List;

public class CircleLabelActivity extends BaseToolBarActivity implements AdapterView.OnItemClickListener {
    private LabelAdapter mAdapter;
    private GridView mGridView;
    private List<Label> labelList;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_circle_label;
    }

    @Override
    protected void initView() {
        mGridView = (GridView) findViewById(R.id.gv_label);
    }

    @Override
    protected void initListener() {
        mGridView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        labelList = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            Label label = new Label();
            label.setLabelName("снс╬ " + i);
            labelList.add(label);
        }
        mAdapter = new LabelAdapter(mContext, R.layout.item_label_list, labelList);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Label label = labelList.get(position);
        Toast.makeText(mContext, label.getLabelName(), Toast.LENGTH_LONG).show();
    }
}
