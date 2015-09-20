package com.tongban.corelib.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tongban.corelib.R;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.utils.ToastUtil;

public class PhotoAlbumAdapter extends QuickAdapter<String> {

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public ArrayList<String> mSelectedImage = new ArrayList<>();
    /**
     * 文件夹路径
     */
    private String mDirPath;

    public void setmDirPath(String mDirPath) {
        this.mDirPath = mDirPath;
    }

    private ISelectIMGListener selectIMGListener;

    public void setSelectIMGListener(ISelectIMGListener selectIMGListener) {
        this.selectIMGListener = selectIMGListener;
    }

    public PhotoAlbumAdapter(Context context, List<String> mData, int itemLayoutId) {
        super(context, itemLayoutId, mData);
    }

    private int currentCount;
    private int maxCount;

    public void setCurrentCount(int current) {
        this.currentCount = current;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final String item) {
        // 设置no_pic
        helper.setImageResource(R.id.id_item_image, R.mipmap.ic_default_image);
        // 设置no_selected
        helper.setImageResource(R.id.id_item_select, 0);
        // 设置图片
        helper.setImageBitmap(R.id.id_item_image, mDirPath + "/" + item);

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        mImageView.setColorFilter(null);
        // 设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
            // 选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                // 已经选择过该图片
                if (mSelectedImage.contains(mDirPath + "/" + item)) {
                    mSelectedImage.remove(mDirPath + "/" + item);
                    mSelect.setImageResource(0);
                    mImageView.setColorFilter(null);
                    currentCount -= 1;
                }
                // 未选择该图片
                else {
                    if (maxCount > currentCount) {
                        mSelectedImage.add(mDirPath + "/" + item);
                        mSelect.setImageResource(R.mipmap.ic_pic_selected);
                        mImageView.setColorFilter(Color.parseColor("#77000000"));
                        currentCount += 1;
                    }
                }
                if (selectIMGListener != null) {
                    selectIMGListener.selectChanged(currentCount);
                }
            }
        });

//        /**
//         * 已经选择过的图片，显示出选择过的效果
//         */
//        if (mSelectedImage.contains(mDirPath + "/" + item)) {
//            mSelect.setImageResource(R.mipmap.ic_pic_selected);
//            mImageView.setColorFilter(Color.parseColor("#77000000"));
//        }
    }

    public interface ISelectIMGListener {
        void selectChanged(int count);
    }

    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
        int width = ScreenUtils.getScreenWidth(mContext);
        ViewGroup.LayoutParams lp = helper.getView(R.id.id_item_image).getLayoutParams();
        lp.height = width / 3 - DensityUtils.dp2px(mContext, 16);
        helper.getView(R.id.id_item_image).setLayoutParams(lp);
    }
}
