package com.tongban.im.widget.provider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.tongban.im.R;

import io.rong.imkit.RongContext;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.TextInputProvider;

/**
 * 话题提供者
 * Created by zhangleilei on 15/7/16.
 */
public class TopicProvider extends InputProvider.ExtendProvider {


    /**
     * 实例化适配器。
     *
     * @param context 融云IM上下文。（通过 RongContext.getInstance() 可以获取）
     */
    public TopicProvider(RongContext context) {
        super(context);
    }

    @Override
    public Drawable obtainPluginDrawable(Context context) {
        return context.getResources().getDrawable(R.mipmap.ic_menu_group_settings);
    }

    @Override
    public CharSequence obtainPluginTitle(Context context) {
        return "话题";
    }

    @Override
    public void onPluginClick(View view) {
        if (RongContext.getInstance().getPrimaryInputProvider() != null) {
            if (RongContext.getInstance().getPrimaryInputProvider() instanceof TextInputProvider) {
                ((TextInputProvider) RongContext.getInstance().getPrimaryInputProvider()).setEditTextContent("#输入要讨论的话题#", 1);
            }
        }
    }

}
