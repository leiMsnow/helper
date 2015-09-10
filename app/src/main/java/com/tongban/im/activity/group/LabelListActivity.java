package com.tongban.im.activity.group;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.ProductApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Tag;
import com.tongban.im.model.TagType;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 圈子标签页
 */
public class LabelListActivity extends BaseToolBarActivity implements View.OnClickListener {

    private FlowLayout flLabelList;
    public List<Tag> tags;

    //最大选择数量
    private int mMaxCount = 0;
    private int mGroupType = 0;
//    private List<Tag> selectedLabel = new ArrayList<>();

    private List<String> selectedTagId = new ArrayList<>();

    @Override
    protected int getLayoutRes() {
        if (getIntent().getExtras() != null) {
            mGroupType = getIntent().getExtras().getInt(Consts.KEY_GROUP_TYPE, 0);
            selectedTagId = getIntent().getExtras().getStringArrayList("selectTag");
            setToolbarTheme(mGroupType);
        }
        return R.layout.activity_lable_list;
    }

    @Override
    protected void initView() {
        flLabelList = (FlowLayout) findViewById(R.id.fl_label_list);
    }

    @Override
    protected void initData() {
        ProductApi.getInstance().fetchTags(0, 20, TagType.GROUP_TAG, this);
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
        if (item.getItemId() == R.id.menu_finish) {
            BaseEvent.LabelEvent labelEvent = new BaseEvent.LabelEvent();
            labelEvent.label = selectedTagId;
            EventBus.getDefault().post(labelEvent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_label_name) {
            if (v.isSelected()) {
                v.setSelected(false);
                mMaxCount--;
                selectedTagId.remove(v.getTag());
            } else {
                if (mMaxCount == 3) {
                    ToastUtil.getInstance(mContext).showToast(R.string.select_label_max);
                    return;
                }
                v.setSelected(true);
                selectedTagId.add(v.getTag().toString());
                mMaxCount++;
            }
        }
    }

    public void onEventMainThread(BaseEvent.FetchTags obj) {
        tags = obj.tags;
        for (int i = 0; i < tags.size(); i++) {

            TextView btnLabelName = (TextView) LayoutInflater.from(mContext).
                    inflate(R.layout.item_label_list, flLabelList, false);
            btnLabelName.setText(tags.get(i).getTag_name());
            btnLabelName.setTag(tags.get(i).getTag_name());
            btnLabelName.setOnClickListener(this);
            flLabelList.addView(btnLabelName);
            for (int j = 0; j < selectedTagId.size(); j++) {
                if (tags.get(i).getTag_name().equals(selectedTagId.get(j))) {
                    btnLabelName.setSelected(true);
                    break;
                }
            }
        }

    }

}
