package com.tongban.im.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.CreateTopicImgAdapter;
import com.tongban.im.model.Topic;
import com.tongban.im.utils.CameraUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 发表话题界面
 *
 * @author fushudi
 */
public class CreateTopicActivity extends BaseToolBarActivity {

    private GridView gvTopicImg;
    private CreateTopicImgAdapter adapter;
    private List<String> smallUrls;
    private Topic topic;

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
        topic = new Topic();
        smallUrls = new ArrayList<>();
        smallUrls.add("");
//        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
//        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
//        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
//        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
//        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
//        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
//        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
//        smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
        topic.setSmallUrl(smallUrls);
        adapter = new CreateTopicImgAdapter(mContext, R.layout.item_topic_grid_img, smallUrls);
        gvTopicImg.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == CameraUtils.OPEN_CAMERA) {
            File file = CameraUtils.getImageFile();
            if (file.exists()) {
                if (file.length() > 100) {
                    String newFile = CameraUtils.saveToSD(file
                            .getAbsolutePath());
                    smallUrls.add(newFile);
                    adapter = new CreateTopicImgAdapter(mContext, R.layout.item_topic_grid_img, smallUrls);
                    gvTopicImg.setAdapter(adapter);
                }
            }
        } else if (requestCode == CameraUtils.OPEN_ALBUM) {
            String picturePath = CameraUtils.searchUriFile(mContext, data);
            if (picturePath == null) {
                picturePath = data.getData().getPath();
            }
            String newFile = CameraUtils.saveToSD(picturePath);
            smallUrls.add(newFile);
            adapter = new CreateTopicImgAdapter(mContext, R.layout.item_topic_grid_img, smallUrls);
            gvTopicImg.setAdapter(adapter);
        }
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
        int itemId = item.getItemId();
        if (itemId == R.id.publish)
            startActivity(new Intent(mContext, OfficialTopicDetailsActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
