package com.tongban.im.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.CreateTopicImgAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.CameraView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 发表话题界面
 *
 * @author fushudi
 */
public class CreateTopicActivity extends BaseToolBarActivity implements View.OnClickListener {

    private GridView gvTopicImg;
    private TextView tvTitle;
    private TextView tvContent;

    private CreateTopicImgAdapter adapter;
    private CameraView mCameraView;


    private Map<String, String[]> mUrls = new HashMap<>();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_topic;
    }

    @Override
    protected void initView() {
        setTitle(R.string.create_topic);
        tvTitle = (TextView) findViewById(R.id.tv_topic_name);
        tvContent = (TextView) findViewById(R.id.tv_topic_content);
        gvTopicImg = (GridView) findViewById(R.id.gv_add_img);
    }

    @Override
    protected void initData() {
        adapter = new CreateTopicImgAdapter(mContext, R.layout.item_topic_grid_img, null);
        adapter.setImgCount(15);
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
        if (itemId == R.id.publish) {
            TopicApi.getInstance().createTopic(tvTitle.getText().toString().trim(),
                    tvContent.getText().toString().trim(), mUrls, this);
        }

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

    // 打开相机的提示框
    protected void createDialog() {
        if (mCameraView == null) {
            mCameraView = new CameraView(mContext);
        }
        mCameraView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == CameraUtils.OPEN_CAMERA) {
            File file = CameraUtils.getImageFile();
            if (file.exists()) {
                if (file.length() > 100) {
                    String newFile = CameraUtils.saveToSD(file
                            .getAbsolutePath());
                    notifyChange(newFile);
                }
            }
        } else if (requestCode == CameraUtils.OPEN_ALBUM) {
            String picturePath = CameraUtils.searchUriFile(mContext, data);
            if (picturePath == null) {
                picturePath = data.getData().getPath();
            }
            String newFile = CameraUtils.saveToSD(picturePath);
            notifyChange(newFile);
        }
    }

    //刷新图片Adapter
    public void notifyChange(String picturePath) {
        if (adapter == null) {
            return;
        }
        if (adapter.getCount() == adapter.getImgCount()) {
            adapter.remove(adapter.getCount() - 1, false);
        }
        String[] minUrl = new String[adapter.getCount()];
        String[] maxUrl = new String[adapter.getCount()];
        String[] midUrl = new String[adapter.getCount()];

        for (int i = 0; i < adapter.getCount(); i++) {
            minUrl[i] = "http://pic1.nipic.com/2008-12-15/20081215211851562_2.jpg";
            maxUrl[i] = "http://pic1.nipic.com/2008-12-15/20081215211851562_2.jpg";
            midUrl[i] = "http://pic1.nipic.com/2008-12-15/20081215211851562_2.jpg";
        }
        mUrls.put("min", minUrl);
        mUrls.put("max", maxUrl);
        mUrls.put("mid", midUrl);
        adapter.add(0, picturePath);
    }
}
