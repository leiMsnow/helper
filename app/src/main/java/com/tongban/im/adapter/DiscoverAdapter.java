package com.tongban.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.im.R;
import com.tongban.im.model.Discover;

import java.util.List;

/**
 * 首页的Adapter
 * Created by Cheney on 15/8/14.
 */
public class DiscoverAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Discover> mList;

    public DiscoverAdapter(Context context, List<Discover> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
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
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(mList.get(position).getComponent_id());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 1:// 横排3图
                    convertView = mInflater.inflate(R.layout.item_discover_img3_horizontal, parent, false);
                    holder.tip = (TextView) convertView.findViewById(R.id.tv_tip);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.description = (TextView) convertView.findViewById(R.id.tv_description);
                    holder.img1 = (ImageView) convertView.findViewById(R.id.iv_left);
                    holder.img2 = (ImageView) convertView.findViewById(R.id.iv_mid);
                    holder.img3 = (ImageView) convertView.findViewById(R.id.iv_right);
                    holder.collectAmount = (TextView) convertView.findViewById(R.id.tv_collect_amount);
                    break;
                case 2:// 竖排3图
                    convertView = mInflater.inflate(R.layout.item_discover_img3_vertical, parent, false);
                    holder.tip = (TextView) convertView.findViewById(R.id.tv_tip);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.img1 = (ImageView) convertView.findViewById(R.id.iv_left);
                    holder.img2 = (ImageView) convertView.findViewById(R.id.iv_top);
                    holder.img3 = (ImageView) convertView.findViewById(R.id.iv_bottom);
                    holder.collectAmount = (TextView) convertView.findViewById(R.id.tv_collect_amount);
                    break;
                case 3:// 图文单图
                    convertView = mInflater.inflate(R.layout.item_discover_text_img, parent, false);
                    holder.tip = (TextView) convertView.findViewById(R.id.tv_tip);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.description = (TextView) convertView.findViewById(R.id.tv_description);
                    holder.img1 = (ImageView) convertView.findViewById(R.id.iv_img);
                    holder.collectAmount = (TextView) convertView.findViewById(R.id.tv_collect_amount);
                    break;
                case 4:// 单图
                    convertView = mInflater.inflate(R.layout.item_discover_img, parent, false);
                    holder.tip = (TextView) convertView.findViewById(R.id.tv_tip);
                    holder.img1 = (ImageView) convertView.findViewById(R.id.iv_img);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case 1:
                holder.tip.setText(mList.get(position).getSoft_word());
                holder.title.setText(mList.get(position).getTitle());
                holder.description.setText(mList.get(position).getDescription());
                Glide.with(mContext).load(mList.get(position).getImg_map().get(0).getImg_url()).
                        placeholder(R.drawable.rc_ic_def_rich_content).into(holder.img1);
                Glide.with(mContext).load(mList.get(position).getImg_map().get(1).getImg_url()).
                        placeholder(R.drawable.rc_ic_def_rich_content).into(holder.img2);
                Glide.with(mContext).load(mList.get(position).getImg_map().get(2).getImg_url()).
                        placeholder(R.drawable.rc_ic_def_rich_content).into(holder.img3);
                holder.collectAmount.setText(String.valueOf(mList.get(position).getCollect_amount()));
                break;
            case 2:
                holder.tip.setText(mList.get(position).getSoft_word());
                holder.title.setText(mList.get(position).getTitle());
                Glide.with(mContext).load(mList.get(position).getImg_map().get(0).getImg_url()).
                        placeholder(R.drawable.rc_ic_def_rich_content).into(holder.img1);
                Glide.with(mContext).load(mList.get(position).getImg_map().get(1).getImg_url()).
                        placeholder(R.drawable.rc_ic_def_rich_content).into(holder.img2);
                Glide.with(mContext).load(mList.get(position).getImg_map().get(2).getImg_url()).
                        placeholder(R.drawable.rc_ic_def_rich_content).into(holder.img3);
                holder.collectAmount.setText(String.valueOf(mList.get(position).getCollect_amount()));
                break;
            case 3:
                holder.tip.setText(mList.get(position).getSoft_word());
                holder.title.setText(mList.get(position).getTitle());
                holder.description.setText(mList.get(position).getDescription());
                Glide.with(mContext).load(mList.get(position).getImg_map().get(0).getImg_url()).
                        placeholder(R.drawable.rc_ic_def_rich_content).into(holder.img1);
                holder.collectAmount.setText(String.valueOf(mList.get(position).getCollect_amount()));
                break;
            case 4:
                holder.tip.setText(mList.get(position).getSoft_word());
                Glide.with(mContext).load(mList.get(position).getImg_map().get(0).getImg_url()).
                        placeholder(R.drawable.rc_ic_def_rich_content).into(holder.img1);
                break;
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView tip;
        public TextView title;
        public TextView description;
        public ImageView img1;
        public ImageView img2;
        public ImageView img3;
        public TextView collectAmount;
    }

}
