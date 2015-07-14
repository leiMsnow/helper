package io.rong.imkit.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


/**
 * Created by DragonJ on 14-8-1.
 */
public abstract class BaseFragment extends Fragment implements Handler.Callback {
    public static final String TOKEN = "RONG_TOKEN";
    public static final int UI_RESTORE = 1;
    private Handler mHandler;
    Thread mThread;

    private LayoutInflater mInflater;

    private static final Configuration CONFIGURATION_INFINITE = new Configuration.Builder()
            .setDuration(Configuration.DURATION_INFINITE)
            .build();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String token = null;

        mHandler = new Handler(this);
        mThread = Thread.currentThread();

        if (savedInstanceState != null) {
            token = savedInstanceState.getString(TOKEN);
        }
        if (token != null && (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null)) {
            RLog.i(this, "BaseFragment", "auto reconnect");
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String s) {
                    mHandler.sendEmptyMessage(UI_RESTORE);
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "connect_auto_fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onTokenIncorrect() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "connect_auto_fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mInflater = LayoutInflater.from(view.getContext());

        super.onViewCreated(view, savedInstanceState);
    }


    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(TOKEN, RongContext.getInstance().getToken());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected Handler getHandler() {
        return mHandler;
    }

    public abstract boolean onBackPressed();

    public abstract void onRestoreUI();

    public void showNotification(final CharSequence notice) {
        boolean isNotice = getResources().getBoolean(R.bool.rc_is_show_warning_notification);
        if (!isNotice) return;

        showNotification(getActivity().getResources().getColor(R.color.rc_notice_warning), R.drawable.rc_ic_notice_wraning, notice);
    }

    public void showNotification(int color, int icon, final CharSequence notice) {
        boolean isNotice = getResources().getBoolean(R.bool.rc_is_show_warning_notification);
        if (!isNotice) return;

        Crouton.cancelAllCroutons();
        Crouton.make(getActivity(), obtainView(mInflater, color, icon, notice), (ViewGroup) getView()).setConfiguration(CONFIGURATION_INFINITE).show();
    }

    public void showNotification(int color, Drawable drawable, final CharSequence notice) {
        boolean isNotice = getResources().getBoolean(R.bool.rc_is_show_warning_notification);
        if (!isNotice) return;

        Crouton.cancelAllCroutons();
        Crouton.make(getActivity(), obtainView(mInflater, color, drawable, notice), (ViewGroup) getView()).setConfiguration(CONFIGURATION_INFINITE).show();
//        Crouton.make(getActivity(), obtainView(mInflater, color, drawable, notice),getId() == 0 ? ((View)getView().getParent()).getId():getId(), CONFIGURATION_INFINITE).show();
    }

    private View obtainView(LayoutInflater inflater, int color, Drawable drawable, final CharSequence notice) {
        View view = inflater.inflate(R.layout.rc_wi_notice, null);
        ((TextView) view.findViewById(android.R.id.message)).setText(notice);
        ((ImageView) view.findViewById(android.R.id.icon)).setImageDrawable(drawable);
        if (color > 0)
            view.setBackgroundColor(color);

        return view;
    }

    private View obtainView(LayoutInflater inflater, int color, int res, final CharSequence notice) {
        View view = inflater.inflate(R.layout.rc_wi_notice, null);
        ((TextView) view.findViewById(android.R.id.message)).setText(notice);
        ((ImageView) view.findViewById(android.R.id.icon)).setImageResource(res);

        view.setBackgroundColor(color);
        return view;
    }

    public void hiddenNotification() {
        Crouton.cancelAllCroutons();
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {

        switch (msg.what) {
            case UI_RESTORE:
                onRestoreUI();
                break;
            default:
                break;
        }
        return true;
    }
}
