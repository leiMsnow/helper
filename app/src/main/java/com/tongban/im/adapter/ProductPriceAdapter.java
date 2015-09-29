package com.tongban.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.discover.PriceInfo;

import java.util.List;

/**
 * 图书单品页的价格Adapter
 * Created by Cheney on 15/8/25.
 */
public class ProductPriceAdapter extends QuickAdapter<PriceInfo> {

    public ProductPriceAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, PriceInfo item) {
        helper.setText(R.id.tv_platform, getPlatform(item.getPlatform()) + "￥" + item.getPrice());
    }


    private String getPlatform(String platform) {
        String str;
        if ("tmall".equals(platform)) {
            str = "天猫";
        } else if ("suning".equals(platform)) {
            str = "苏宁";
        } else if ("amazon".equals(platform)) {
            str = "亚马逊";
        } else if ("dangdang".equals(platform)) {
            str = "当当";
        } else if ("jingdong".equals(platform)) {
            str = "京东";
        } else {
            str = platform;
        }
        return str;
    }

//    private SpannableStringBuilder getPrice(String price) {
//        SpannableStringBuilder sb = new SpannableStringBuilder(price);
//        int index = price.indexOf(".");
//        if (index != -1) {
//            sb.setSpan(new RelativeSizeSpan(1.5f), 0, index,
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        sb.insert(0, "￥");
//        return sb;
//    }
}
