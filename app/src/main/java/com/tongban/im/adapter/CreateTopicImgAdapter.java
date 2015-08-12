package com.tongban.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.AlertView;

import java.util.List;

/**
 * 创建话题添加图片Adapter
 * Created by fushudi on 2015/7/16.
 */
public class CreateTopicImgAdapter extends QuickAdapter<String> {
    private AlertView dialog;
    private LinearLayout mCamera;
    private LinearLayout mGallery;

    public CreateTopicImgAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(final BaseAdapterHelper helper, final String item) {
        if (TextUtils.isEmpty(item)) {
            helper.setImageBitmap(R.id.iv_topic_img, R.mipmap.ic_menu_add);
        } else {
            //TODO
            if (mData.size() < 3) {
                helper.setImageBitmap(R.id.iv_topic_img, item);
            } else if (mData.size() == 3) {
                set(0, item);
            }
        }
        helper.setOnClickListener(R.id.iv_topic_img, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }

            // 打开相机的提示框
            private void createDialog() {
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
        });
    }

}
