package com.tongban.im.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tb.api.model.ImageUrl;
import com.tongban.im.R;

import java.util.List;

/**
 * 单品页顶部图片的Adapter
 * Created by Cheney on 15/8/25.
 */
public class ProductBookImgPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<ImageUrl> mImageUrls;
    private LayoutInflater mInflater;
    private ImageView mImageView;

    public ProductBookImgPagerAdapter(Context context, List<ImageUrl> imageUrls) {
        mImageUrls = imageUrls;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.item_product_book_vp, container, false);
        mImageView = (ImageView) view.findViewById(R.id.iv_img);
        // 加载网络图片
        Glide.with(mContext).load(mImageUrls.get(position).getMid()).into(mImageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 图片点击事件
            }
        });
        container.addView(view, position);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
