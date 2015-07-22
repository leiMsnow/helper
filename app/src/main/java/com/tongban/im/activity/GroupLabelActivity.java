package com.tongban.im.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.LabelAdapter;
import com.tongban.im.model.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择圈子标签界面
 *
 * @author fushudi
 */
public class GroupLabelActivity extends BaseToolBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener, TextWatcher {
    private LabelAdapter mAdapter;
    private GridView mGridView;
    private List<Label> labelList;
    private LinearLayout mShowLabel;
    private TextView mAddLabel;
    private EditText etLabel;
    private String mLabel;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_group_label;
    }

    @Override
    protected void initView() {
        mGridView = (GridView) findViewById(R.id.gv_label);
        mShowLabel = (LinearLayout) findViewById(R.id.show_view);
        mAddLabel = (TextView) findViewById(R.id.tv_add);
        etLabel = (EditText) findViewById(R.id.et_label_name);
    }

    @Override
    protected void initListener() {
        mGridView.setOnItemClickListener(this);
        mAddLabel.setOnClickListener(this);
        etLabel.addTextChangedListener(this);
    }

    @Override
    protected void initData() {
        labelList = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            Label label = new Label();
            label.setLabelName("youyong" + i);
            labelList.add(label);
        }
        mAdapter = new LabelAdapter(mContext, R.layout.item_label_list, labelList);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_label, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.next_step) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Label label = labelList.get(position);
        mLabel = label.getLabelName();
        addLabel();
    }

    private void addLabel() {

        final TextView tvLabel = new TextView(this);
        tvLabel.setText(mLabel);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLabel.setLayoutParams(params);
        tvLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowLabel.removeView(tvLabel);
            }
        });
        if (mShowLabel.getChildCount() == 3) {
            Toast.makeText(mContext, getResources().getString(R.string.toast_group_label), Toast.LENGTH_LONG).show();
            return;
        } else {
            mShowLabel.addView(tvLabel);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                if (!TextUtils.isEmpty(mLabel)) {
                    addLabel();
                    etLabel.setText("");
                } else {
                    return;
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mLabel = etLabel.getText().toString();
    }
}
