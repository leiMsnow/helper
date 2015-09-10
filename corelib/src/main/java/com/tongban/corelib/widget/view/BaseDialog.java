package com.tongban.corelib.widget.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tongban.corelib.R;

/**
 * 统一样式
 * @author maxiaolong
 *
 */
public class BaseDialog extends Dialog {

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    public BaseDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    // 先调用构造方法在调用oncreate方法
    private static boolean isShow = true;
    private static boolean mCancel = true; //默认点击空白区域，隐藏dialog
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        super.show();
    }

    public static class Builder {
        private Context mContext;
        private String mTitle;
        private String mMessage;
        private String mPositiveButtonText;
        private String mNegativeButtonText;
        private String mNeutralButtonText;

        private View mContentView;
        private OnClickListener mPositiveButtonClickListener,
                mNegativeButtonClickListener, mNeutralButtonClickListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.mMessage = (String) mContext.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.mTitle = (String) mContext.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.mContentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                OnClickListener listener) {
            this.mPositiveButtonText = (String) mContext.getText(positiveButtonText);
            this.mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                OnClickListener listener) {
            this.mPositiveButtonText = positiveButtonText;
            this.mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                OnClickListener listener) {
            this.mNegativeButtonText = (String) mContext.getText(negativeButtonText);
            this.mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                OnClickListener listener) {
            this.mNegativeButtonText = negativeButtonText;
            this.mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(int neutralButtonText,
                OnClickListener listener) {
            this.mNeutralButtonText = (String) mContext.getText(neutralButtonText);
            this.mNeutralButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(String neutralButtonText,
                OnClickListener listener) {
            this.mNeutralButtonText = neutralButtonText;
            this.mNeutralButtonClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            isShow = cancelable;
            return this;
        }
        
        public Builder setCanceledOnTouchOutside(boolean cancel) {
            mCancel = cancel;
            return this;
        }

        
        
        public BaseDialog show() {
            BaseDialog dialog = create();
            dialog.show();
            initWindow(dialog);
            return dialog;
        }
        
        private BaseDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.view_dialog, null);
            
            final BaseDialog dialog = new BaseDialog(mContext);
            dialog.setCanceledOnTouchOutside(mCancel);
            dialog.setCancelable(isShow);
            
            //隐藏标题栏,必须在setContentView()前调用
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);;
            
            // 设置title
            TextView dialogTitle = (TextView) layout.findViewById(R.id.dialog_title);
            if (TextUtils.isEmpty(mTitle)) {
                dialogTitle.setVisibility(View.GONE);
            } else {
                dialogTitle.setText(mTitle);
                dialogTitle.setVisibility(View.VISIBLE);
            }

            // 设置message
            TextView dialogMessage = (TextView) layout.findViewById(R.id.dialog_message);
            if (!TextUtils.isEmpty(mMessage)) {
                dialogMessage.setText(mMessage);
                dialogMessage.setVisibility(View.VISIBLE);
            }else{
                dialogMessage.setVisibility(View.GONE);
            }

            // 设置右按钮
            TextView mPositiveBT = (TextView) layout.findViewById(R.id.right_bt);
            if (mPositiveButtonText != null) {
                mPositiveBT.setText(mPositiveButtonText);

                if (mPositiveButtonClickListener != null) {
                    mPositiveBT.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            mPositiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                mPositiveBT.setVisibility(View.GONE);
            }

            // 设置左按钮
            TextView mNegativeBT = (TextView) layout.findViewById(R.id.left_bt);
            if (mNegativeButtonText != null) {
                mNegativeBT.setText(mNegativeButtonText);
                if (mNegativeButtonClickListener != null) {
                    mNegativeBT.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            mNegativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                mNegativeBT.setVisibility(View.GONE);
            }

            // 设置中间按钮
            TextView mCenterBT = (TextView) layout.findViewById(R.id.center_bt);
            if (mNeutralButtonText != null) {
                mCenterBT.setText(mNeutralButtonText);
                if (mNeutralButtonClickListener != null) {
                    mCenterBT.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            mNeutralButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEUTRAL);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                mCenterBT.setVisibility(View.GONE);
            }
            
            // 设置按钮线显示,隐藏
            View partline1 = layout.findViewById(R.id.partline1);
            View partline2 = layout.findViewById(R.id.partline2);

            if (mNegativeBT.VISIBLE == View.VISIBLE
                    && (mCenterBT.VISIBLE == View.VISIBLE || mPositiveBT.VISIBLE == View.VISIBLE)) {
                partline1.setVisibility(View.VISIBLE);
            } else {
                partline1.setVisibility(View.GONE);
            }

            if (mPositiveBT.VISIBLE == View.VISIBLE
                    && (mNegativeBT.VISIBLE == View.VISIBLE || mCenterBT.VISIBLE == View.VISIBLE)) {
                partline2.setVisibility(View.VISIBLE);
            } else {
                partline2.setVisibility(View.GONE);
            }
            
            
            // 设置自定义布局
            if (mContentView != null) {
                dialog.setContentView(mContentView);
                return dialog;
            }

            if(mCancel){//点击透明区域是否添加取消监听
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                layout.findViewById(R.id.top_layout).setOnClickListener(new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                      //防止往下传
                    }
                });
            }
            dialog.setContentView(layout);
            return dialog;
        }

        private void initWindow(Dialog dialog) {

            /*
             * 获取框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
             * 对象,这样这可以以同样的方式改变这个Activity的属性.
             */
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();

            lp.gravity = Gravity.CENTER;
            lp.x = 0; // 新位置X坐标
            lp.y = 0; // 新位置Y坐标
            lp.height = WindowManager.LayoutParams.MATCH_PARENT; // 高度
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 高度
//            lp.alpha = 0.7f; // 透明度

            // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
            dialogWindow.setAttributes(lp);
        }
    }
}
