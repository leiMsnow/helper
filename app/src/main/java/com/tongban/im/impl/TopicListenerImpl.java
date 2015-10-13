package com.tongban.im.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tongban.corelib.fragment.PhotoViewFragment;
import com.tongban.im.R;
import com.tongban.im.activity.PhotoViewPagerActivity;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.ImageUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * 公用接口实现类，只适用于话题
 * 大图查看/查看用户信息/回复评论
 * Created by zhangleilei on 8/26/15.
 */
public class TopicListenerImpl implements View.OnClickListener {


    private Context mContext;

    public TopicListenerImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_small_img_1:
                List<ImageUrl> imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                startPhotoView(mContext, setImageUrls(imageUrls), 0);
                break;
            case R.id.iv_small_img_2:
                imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                startPhotoView(mContext, setImageUrls(imageUrls), 1);
                break;
            case R.id.iv_small_img_3:
                imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                startPhotoView(mContext, setImageUrls(imageUrls), 2);
                break;
            case R.id.iv_user_portrait:
                String userId = v.getTag(Integer.MAX_VALUE).toString();
                TransferCenter.getInstance().startUserCenter(userId);
                break;

        }
    }

    //取出大图地址存入集合中
    public static ArrayList<String> setImageUrls(List<ImageUrl> imageUrls) {
        ArrayList<String> urls = new ArrayList<>();
        for (int i = 0; i < imageUrls.size(); i++) {
            urls.add(imageUrls.get(i).getMax());
        }
        return urls;
    }

    public static void startPhotoView(Context mContext, ArrayList<String> urls, int currentIndex) {
        Intent intent = new Intent(mContext, PhotoViewPagerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PhotoViewFragment.KEY_URL, urls);
        bundle.putInt(PhotoViewFragment.KEY_CURRENT_INDEX, currentIndex);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
