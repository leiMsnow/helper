package com.tongban.im.widget.view;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tongban.im.R;

/***
 * 提示弹出框
 * 
 * @author fushudi
 * 
 */
public class AlertView extends Dialog implements
		View.OnClickListener {
	private TextView mContent;
	private TextView tvLeft;
	private TextView tvRight;
	private View mVerticalBar;
	private TextView mTitle;
	private OnClickListener onClickListener;
	private Context mContext;

	/**
	 * constructor
	 * 
	 * @param context
	 *            context
	 */
	public AlertView(Context context) {
		this(context, true);
	}

	public AlertView(Context context, boolean canceble) {
		super(context, R.style.process_dialog);
		mContext = context;
		init(context);
		setCancelable(canceble);
	}

	private void init(Context context) {
		setContentView(R.layout.view_alertview);

	}

	public void setTitleVisivility(int resID) {
		mTitle.setVisibility(resID);
	}

	public void setContent(int resid) {
		mContent.setText(mContext.getResources().getString(resid));
	}

	public void setContent(String content) {
		mContent.setText(content);
	}

	public void setTitle(int resid) {
		mTitle.setText(mContext.getResources().getString(resid));
	}

	public void setTitle(String title) {
		mTitle.setText(title);
	}

	public void setRightText(int resid) {
		tvLeft.setText(mContext.getResources().getString(resid));
	}

	public void setRightText(String okText) {
		tvLeft.setText(okText);
	}

	public void setLeftText(int resid) {
		tvRight.setText(mContext.getResources().getString(resid));
	}

	public void setLeftText(String text) {
		tvRight.setText(text);
	}

	public void setOnClickListener(OnClickListener listener) {
		this.onClickListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (v == tvLeft && onClickListener != null) {
			onClickListener.onClick(this, 0);
		}
		// 取消
		else if (v == tvRight && onClickListener != null) {
			onClickListener.onClick(this, 1);
		}
		// 默认
		dismiss();
	}

	/**
	 * 隐藏取消按钮和竖线
	 */
	public void hideCancel() {
		tvRight.setVisibility(View.GONE);
		mVerticalBar.setVisibility(View.GONE);
	}
}
