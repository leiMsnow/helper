package com.tongban.corelib.widget.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import com.tongban.corelib.R;

public class BaseProgressDialog extends Dialog {

    private TextView mMessage;

    public BaseProgressDialog(Context context) {
        this(context, context.getString(R.string.pd_loading), true);
    }

    public BaseProgressDialog(Context context, String message, boolean cancalble) {
        super(context, R.style.ProgressDialog);
        init();
        setMessage(message);
        setCancelable(cancalble);
    }

    private void init() {
        this.setContentView(R.layout.view_process_dialog);
        this.mMessage = (TextView) findViewById(R.id.tv_msg);
        this.setCanceledOnTouchOutside(false);
    }

    public void setMessage(String string) {
        this.mMessage.setText(string);
    }

}
