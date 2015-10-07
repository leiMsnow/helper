package com.tongban.im.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.widget.view.ClipImageLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 图片裁剪界面
 *
 * @author fushudi
 */
public class ClipImageBorderViewActivity extends BaseToolBarActivity {

    @Bind(R.id.id_clip_image_layout)
    ClipImageLayout mClipImageLayout;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_clip_image_border_view;
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.icon_clip));
        String newFile = getIntent().getStringExtra("newFile");
        mClipImageLayout.setZoomBitmap(getLocalBitmap(newFile));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clip:
                Bitmap bitmap = mClipImageLayout.clip();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] datas = baos.toByteArray();

                Intent intent = new Intent();
                intent.putExtra("bitmap", datas);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bitmap getLocalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
