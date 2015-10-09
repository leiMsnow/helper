package com.tongban.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.widget.view.TopicImageView;
import com.tongban.im.widget.view.TopicInputView;

import java.util.List;

/**
 * 创建话题添加图片Adapter
 * 使用flowLayout作为父控件，此类的convert方法不再使用
 * Created by fushudi on 2015/7/16.
 */
public class CreateTopicImgAdapter extends QuickAdapter<String> {

    public int getImgCount() {
        return imgCount;
    }

    private int imgCount = 15;

    public void setImgCount(int imgCount) {
        this.imgCount = imgCount;
    }

    public CreateTopicImgAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(final BaseAdapterHelper helper, final String item) {

//        LogUtil.d("CreateTopicImgAdapter-convert:----" + helper.getPosition());

//        if (TextUtils.isEmpty(item)) {
//            helper.setImageResource(R.id.iv_topic_img, R.drawable.ic_add_img);
//        } else {
//            helper.setImageBitmap(R.id.iv_topic_img, item);
//        }
//        helper.setTag(R.id.iv_topic_img, Integer.MAX_VALUE, TextUtils.isEmpty(item));
//        helper.setTag(R.id.iv_topic_img, Integer.MIN_VALUE, helper.getPosition());
//        helper.setOnClickListener(R.id.iv_topic_img, onClickListener);

    }

    public View getChildView(int i, FlowLayout flowLayout, View.OnClickListener listener) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_create_grid_img, flowLayout, false);
        String item = getItem(i);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_topic_img);

        int width = ScreenUtils.getScreenWidth(mContext);
        int paddingLeft = flowLayout.getPaddingLeft();

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
//        int marginRight = lp.rightMargin;

//        if (imgCount == TopicImageView.IMAGE_COUNT_REPLY) {
//            lp.width = width / TopicImageView.IMAGE_COUNT_REPLY - paddingLeft - marginRight;
//        } else if (imgCount == TopicImageView.IMAGE_COUNT_CREATE) {
//            lp.width = width / 5 - paddingLeft - marginRight;
//        }

        lp.width = width / 5 - paddingLeft ;
        lp.height = lp.width;

        imageView.setLayoutParams(lp);

        if (TextUtils.isEmpty(item)) {
            imageView.setImageResource(R.drawable.ic_add_img);
        } else {

            Glide.with(mContext)
                    .load(item)
                    .error(R.mipmap.ic_default_image)
                    .into(imageView);
        }
        imageView.setTag(Integer.MAX_VALUE, TextUtils.isEmpty(item));
        imageView.setTag(Integer.MIN_VALUE, i);
        imageView.setOnClickListener(listener);

        return view;
    }

}
