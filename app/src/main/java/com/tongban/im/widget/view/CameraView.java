package com.tongban.im.widget.view;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.utils.CameraUtils;

/***
 * 拍照-提示弹出框
 *
 * @author fushudi
 */
public class CameraView extends Dialog {

    private Context mContext;
    private TextView tvCamera;
    private TextView tvGallery;

    private int current = 0;
    private int maxSelect = 9;

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setMaxSelect(int maxSelect) {
        this.maxSelect = maxSelect;
    }

    /**
     * constructor
     *
     * @param context mContext
     */
    public CameraView(Context context) {
        this(context, true);
    }

    public CameraView(Context context, boolean cancelable) {
        super(context, R.style.ProcessDialog);
        setCancelable(cancelable);
        mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.view_alertview);
        tvCamera = (TextView) findViewById(R.id.tv_camera);
        tvGallery = (TextView) findViewById(R.id.tv_gallery);

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.takePhoto(mContext);
                cancel();
            }
        });
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.openPhotoAlbum(mContext,current,maxSelect);
                cancel();
            }
        });
    }
}
