package com.tongban.im.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.TopicImgAdapter;
import com.tongban.im.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * 发表话题界面
 *
 * @author fushudi
 */
public class CreateTopicActivity extends BaseToolBarActivity {

    private GridView gvTopicImg;
    private TopicImgAdapter adapter;

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
        Topic topic = new Topic();
        List<String> smallUrls = new ArrayList<>();
        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
        topic.setSmallUrl(smallUrls);
        adapter = new TopicImgAdapter(mContext, R.layout.item_topic_grid_img, null);
        adapter.replaceAll(topic.getSmallUrl());
        gvTopicImg.setAdapter(adapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topic_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        if (itemId==R.id.publish)
            startActivity(new Intent(mContext, AuthorityTopicDetailActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
