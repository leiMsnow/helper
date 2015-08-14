package com.tongban.im.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.CreateTopicImgAdapter;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.AlertView;

import java.io.File;

/**
 * 发表话题界面
 *
 * @author fushudi
 */
public class CreateTopicActivity extends BaseToolBarActivity implements View.OnClickListener {

    private GridView gvTopicImg;
    private CreateTopicImgAdapter adapter;
    private AlertView dialog;
    private LinearLayout mCamera;
    private LinearLayout mGallery;

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

    // 打开相机的提示框
    protected void createDialog() {
        if (dialog == null) {
            dialog = new AlertView(mContext);
            mCamera = (LinearLayout) dialog.findViewById(R.id.camera);
            mGallery = (LinearLayout) dialog.findViewById(R.id.gallery);

            mCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraUtils.takePhoto(mContext);
                    dialog.cancel();
                }
            });
            mGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraUtils.openPhotoAlbum(mContext);
                    dialog.cancel();
                }
            });
        }
        dialog.show();
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
        adapter.add(0, picturePath);
    }
}
