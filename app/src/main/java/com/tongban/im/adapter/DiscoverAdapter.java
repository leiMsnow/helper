package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.discover.Discover;

import java.util.List;

/**
 * 首页的Adapter
 * Created by Cheney on 15/8/14.
 */
public class DiscoverAdapter extends QuickAdapter<Discover> {

    private ThemeImageOnCliCkListener onCliCkListener;

    public DiscoverAdapter(Context context, List data, IMultiItemTypeSupport multiItemTypeSupport) {
        super(context, data, multiItemTypeSupport);
        onCliCkListener = new ThemeImageOnCliCkListener();
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Discover item) {
        switch (Integer.parseInt(item.getComponent_id())) {
            case 1:// 横排3图
                helper.setText(R.id.tv_tip, item.getSoft_word());
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_collect_count, String.valueOf(item.getCollect_amount()));

                helper.setText(R.id.tv_description, item.getDescription());

                helper.setImageBitmap(R.id.iv_left, item.getImg_map().get(0).getImg_url(),
                        R.drawable.rc_ic_def_rich_content);
                helper.setImageBitmap(R.id.iv_mid, item.getImg_map().get(1).getImg_url(),
                        R.drawable.rc_ic_def_rich_content);
                helper.setImageBitmap(R.id.iv_right, item.getImg_map().get(2).getImg_url(),
                        R.drawable.rc_ic_def_rich_content);

                break;
            case 2:// 左1右2图
                helper.setText(R.id.tv_tip, item.getSoft_word());
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_collect_count, String.valueOf(item.getCollect_amount()));

                helper.setImageBitmap(R.id.iv_left, item.getImg_map().get(0).getImg_url(),
                        R.drawable.rc_ic_def_rich_content);
                helper.setImageBitmap(R.id.iv_top, item.getImg_map().get(1).getImg_url(),
                        R.drawable.rc_ic_def_rich_content);
                helper.setImageBitmap(R.id.iv_bottom, item.getImg_map().get(2).getImg_url(),
                        R.drawable.rc_ic_def_rich_content);

                helper.setTag(R.id.iv_left, Integer.MAX_VALUE, item.getImg_map().get(0).getLink_url());
                helper.setOnClickListener(R.id.iv_left, onCliCkListener);
                helper.setTag(R.id.iv_top, Integer.MAX_VALUE, item.getImg_map().get(1).getLink_url());
                helper.setOnClickListener(R.id.iv_left, onCliCkListener);
                helper.setTag(R.id.iv_bottom, Integer.MAX_VALUE, item.getImg_map().get(2).getLink_url());
                helper.setOnClickListener(R.id.iv_left, onCliCkListener);
                break;
            case 3:// 图文单图
                helper.setText(R.id.tv_tip, item.getSoft_word());
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_collect_count, String.valueOf(item.getCollect_amount()));

                helper.setText(R.id.tv_description, item.getDescription());

                helper.setImageBitmap(R.id.iv_img, item.getImg_map().get(0).getImg_url(),
                        R.drawable.rc_ic_def_rich_content);
                break;
            case 4:// 单图
                helper.setText(R.id.tv_tip, item.getSoft_word());
                helper.setImageBitmap(R.id.iv_img, item.getImg_map().get(0).getImg_url(),
                        R.drawable.rc_ic_def_rich_content);
                break;

        }

    }

    class ThemeImageOnCliCkListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String item = v.getTag(Integer.MAX_VALUE).toString();
            switch (v.getId()) {
                case R.id.iv_left:
                    TransferCenter.getInstance().startLinkUrl(item);
                    break;
                case R.id.iv_top:
                    TransferCenter.getInstance().startLinkUrl(item);
                    break;
                case R.id.iv_bottom:
                    TransferCenter.getInstance().startLinkUrl(item);
                    break;

            }
        }
    }

}
