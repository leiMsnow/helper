package com.tongban.im.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.tongban.im.R;
import com.tongban.im.adapter.CreateTopicImgAdapter;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.AlertView;

/**
 * 发表话题界面
 *
 * @author fushudi
 */
public class CreateTopicActivity extends CommonImageResultActivity implements View.OnClickListener {

    private GridView gvTopicImg;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_topic;
    }

    @Override
    protected void initView() {
        setTitle("发表话题");
        gvTopicImg = (GridView) findViewById(R.id.gv_add_img);
    }

    @Override
    protected void initData() {
        adapter = new CreateTopicImgAdapter(mContext, R.layout.item_topic_grid_img, null);
        adapter.add("");
        gvTopicImg.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        adapter.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topic_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.publish)
            startActivity(new Intent(mContext, OfficialTopicDetailsActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.iv_topic_img:
                createDialog();
                break;
        }
    }


}
