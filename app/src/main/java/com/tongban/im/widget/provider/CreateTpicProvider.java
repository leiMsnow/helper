package com.tongban.im.widget.provider;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;

import com.tongban.im.R;

/**
 * 创建话题actionbar的provider
 * Created by zhangleilei on 15/8/15.
 */
public class CreateTpicProvider extends ActionProvider {

    private Context mContext;

    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public CreateTpicProvider(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public View onCreateActionView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_create_button, null);
        return view;
    }
}
