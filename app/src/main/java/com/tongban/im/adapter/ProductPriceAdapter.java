package com.tongban.im.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.model.PriceInfo;

import java.util.List;

/**
 * 图书单品页的价格Adapter
 * Created by Cheney on 15/8/25.
 */
public class ProductPriceAdapter extends BaseAdapter {
    private Context mContext;
    private List<PriceInfo> mList;
    private LayoutInflater mInflater;

    public ProductPriceAdapter(Context context, List<PriceInfo> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_product_book_price_grid, parent, false);
            holder.platform = (TextView) convertView.findViewById(R.id.tv_platform);
            holder.price = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.platform.setText(getPlatform(mList.get(position).getPlatform()));
        holder.price.setText(mList.get(position).getPrice());
        return convertView;
    }

    static class ViewHolder {
        TextView platform;
        TextView price;
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
