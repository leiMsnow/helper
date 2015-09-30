package com.tongban.corelib.base.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.net.Uri;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.R;


/**
 * ViewHolder的基础类;
 * 封装了常用View的赋值，以及事件监听的方法，方便操作。并且赋值方法都有采用链式编程，更加方便书写。
 * 根据layoutId返回/创建一个view
 * Created by zhangleilei on 15/6/3.
 */
public class BaseAdapterHelper {

    private final Context mContext;
    private final SparseArray<View> mViews;


    private int mPosition;
    private View mConvertView;
    private int mLayoutId;

    private static boolean isFirstCreate = true;

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    /**
     * 是否第一次创建view
     */
    public static boolean isFirstCreate() {
        return isFirstCreate;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    //受保护的构造不允许new，只可以用get()进入
    protected BaseAdapterHelper(Context context, ViewGroup parent, int layoutId, int position) {

        this.mContext = context;
        this.mPosition = position;
        this.mLayoutId = layoutId;
        this.mViews = new SparseArray<>();
        this.mConvertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
        isFirstCreate = true;
    }

    /**
     * viewHolder唯一入口
     *
     * @param context     上下文
     * @param convertView getView的参数
     * @param parent      父控件
     * @param layoutId    布局资源ID
     * @return
     */
    public static BaseAdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId) {
        return get(context, convertView, parent, layoutId, -1);
    }

    /**
     * 1.首先如果convertView==null，我们需要去通过LayoutInflater去inflate一个布局文件，返回我们的convertView。
     * 由于我们这里并不会为每个Item的布局去编写ViewHolder，该类充当了一个万能的ViewHolder的角色，所以存储convertView子View的引用，
     * 使用了SparseArray<View>，最后将convertView与BaseAdapterHelper通过tag关联。
     * 如果convertView!=null，直接通过tag获取到我们关联的BaseAdapterHelper，更新position后返回。
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    static BaseAdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId,
                                 int position) {

        if (convertView == null) {
            return new BaseAdapterHelper(context, parent, layoutId, position);
        }
        //获取现有的viewHolder并且更新position
        BaseAdapterHelper existingHolder = (BaseAdapterHelper) convertView.getTag();

        if (existingHolder.mLayoutId != layoutId) {
            return new BaseAdapterHelper(context, parent, layoutId, position);
        }
        existingHolder.mPosition = position;
        isFirstCreate = false;
        return existingHolder;
    }

    /**
     * 通过控件的Id获取对于的控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        return retrieveView(viewId);
    }

    //通过ID检查view是否存在，如果没有则加入views
    private <T extends View> T retrieveView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    //--------------------------提供view便捷的设置值的方法-start---------------------------------------

    /**
     * 设置view文本
     *
     * @param viewId
     * @param value
     * @return
     */
    public BaseAdapterHelper setText(int viewId, CharSequence value) {
        TextView view = retrieveView(viewId);
        view.setText(value);
        return this;
    }

    public BaseAdapterHelper setText(int viewId, String value) {
        TextView view = retrieveView(viewId);
        view.setText(value);
        return this;
    }

    public BaseAdapterHelper setImageResource(int viewId, int imageResId) {
        ImageView view = retrieveView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public BaseAdapterHelper setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = retrieveView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public BaseAdapterHelper setBackgroundColor(int viewId, int bgColor) {
        View view = retrieveView(viewId);
        view.setBackgroundColor(bgColor);
        return this;
    }

    public BaseAdapterHelper setBackgroundRes(int viewId, int backgroundRes) {
        View view = retrieveView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public BaseAdapterHelper setTextColor(int viewId, int textColor) {
        TextView view = retrieveView(viewId);
        view.setTextColor(textColor);
        return this;
    }


    /**
     * 图片展示-展示绝对路径的图片：网络图片等
     *
     * @param viewId
     * @param url
     * @return
     */
    public BaseAdapterHelper setImageBitmap(int viewId, String url) {
        return setImageBitmap(viewId, url, R.mipmap.ic_default_image);
    }

    public BaseAdapterHelper setImageBitmap(int viewId, String url, int defaultResId) {
        ImageView view = retrieveView(viewId);
        Glide.with(mContext)
                .load(url)
                .error(defaultResId)
                .into(view);
        return this;
    }

    /**
     * 图片展示-用于展示res中的图片
     *
     * @param viewId
     * @param resId
     * @return
     */
    public BaseAdapterHelper setImageBitmap(int viewId, int resId) {
        ImageView view = retrieveView(viewId);
        Glide.with(mContext)
                .load(resId)
                .error(R.mipmap.ic_default_image)
                .into(view);
        return this;
    }


    public BaseAdapterHelper setColorFilter(int viewId, ColorFilter colorFilter) {
        ImageView view = retrieveView(viewId);
        view.setColorFilter(colorFilter);
        return this;
    }

    public BaseAdapterHelper setColorFilter(int viewId, int colorFilter) {
        ImageView view = retrieveView(viewId);
        view.setColorFilter(colorFilter);
        return this;
    }

    /**
     * 设置透明度
     *
     * @param viewId
     * @param value
     * @return
     */
    public BaseAdapterHelper setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            retrieveView(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            retrieveView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public BaseAdapterHelper setVisible(int viewId, int visible) {
        View view = retrieveView(viewId);
        view.setVisibility(visible);
        return this;
    }

    public BaseAdapterHelper setMax(int viewId, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        return this;
    }

    public BaseAdapterHelper setProgress(int viewId, int progress) {
        ProgressBar view = retrieveView(viewId);
        view.setProgress(progress);
        return this;
    }

    public BaseAdapterHelper setRating(int viewId, float rating) {
        RatingBar view = retrieveView(viewId);
        view.setRating(rating);
        return this;
    }

    public BaseAdapterHelper setTag(int viewId, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(tag);
        return this;
    }

    public BaseAdapterHelper setTag(int viewId, int key, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(key, tag);
        return this;
    }

    //--------------------------提供view便捷的设置值的方法-end---------------------------------------

    //--------------------------提供view便捷的设置监听的方法-start---------------------------------------


    public BaseAdapterHelper setAdapter(int viewId, BaseAdapter adapter) {
        AbsListView view = (AbsListView) retrieveView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    public BaseAdapterHelper setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) retrieveView(viewId);
        view.setChecked(checked);
        return this;
    }

    public BaseAdapterHelper setOnClickListener(int viewId,
                                                View.OnClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public BaseAdapterHelper setOnTouchListener(int viewId,
                                                View.OnTouchListener listener) {
        View view = retrieveView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public BaseAdapterHelper setOnLongClickListener(int viewId,
                                                    View.OnLongClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    //--------------------------提供view便捷的设置监听的方法-start---------------------------------------


    public View getConvertView() {
        return mConvertView;
    }

    public int getPosition() {
        if (mPosition == -1)
            throw new IllegalStateException("当前viewHolder尚未初始化。");
        return mPosition;
    }

}
