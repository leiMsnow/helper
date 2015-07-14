package io.rong.imkit.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.rong.imkit.R;
import io.rong.imkit.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.widget.InputView;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.PublicServiceInfo;

/**
 * Created by DragonJ on 14/10/23.
 */
public class MessageInputFragment extends UriFragment implements View.OnClickListener {

    Conversation mConversation;
    InputView mInput;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fr_messageinput, container, false);
        mInput = (InputView) view.findViewById(R.id.rc_input);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (RongContext.getInstance().getPrimaryInputProvider() == null) {
            throw new RuntimeException("MainInputProvider must not be null.");
        }

    }

    private void setCurrentConversation(Conversation conversation) {

        RongContext.getInstance().getPrimaryInputProvider().setCurrentConversation(conversation);

        if (RongContext.getInstance().getSecondaryInputProvider() != null) {
            RongContext.getInstance().getSecondaryInputProvider().setCurrentConversation(conversation);
        }

        if(RongContext.getInstance().getMenuInputProvider() != null) {
            RongContext.getInstance().getMenuInputProvider().setCurrentConversation(conversation);
        }

        for (InputProvider provider : RongContext.getInstance().getRegisteredExtendProviderList(mConversation.getConversationType())) {
            provider.setCurrentConversation(conversation);
        }

        mInput.setExtendProvider(RongContext.getInstance().getRegisteredExtendProviderList(mConversation.getConversationType()));

        for (InputProvider provider : RongContext.getInstance().getRegisteredExtendProviderList(mConversation.getConversationType())) {
            provider.onAttached(this, mInput);
        }

        if(conversation.getConversationType().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE) ||
                conversation.getConversationType().equals(Conversation.ConversationType.PUBLIC_SERVICE)) {

            ConversationKey key = ConversationKey.obtain(conversation.getTargetId(), conversation.getConversationType());
            PublicServiceInfo info = RongContext.getInstance().getPublicServiceInfoFromCache(key.getKey());

            if(info != null && info.getMenu() != null &&
               info.getMenu().getMenuItems() != null &&
               info.getMenu().getMenuItems().size() > 0) {

                mInput.setInputProviderEx(RongContext.getInstance().getPrimaryInputProvider(),
                        RongContext.getInstance().getSecondaryInputProvider(),
                        RongContext.getInstance().getMenuInputProvider());

            } else {
                mInput.setInputProvider(RongContext.getInstance().getPrimaryInputProvider(),
                        RongContext.getInstance().getSecondaryInputProvider());
            }
        } else {
            mInput.setInputProvider(RongContext.getInstance().getPrimaryInputProvider(),
                                    RongContext.getInstance().getSecondaryInputProvider());
        }

        RongContext.getInstance().getPrimaryInputProvider().onAttached(this, mInput);

        if (RongContext.getInstance().getSecondaryInputProvider() != null) {
            RongContext.getInstance().getSecondaryInputProvider().onAttached(this, mInput);
        }
    }


    @Override
    protected void initFragment(Uri uri) {
        String typeStr = uri.getLastPathSegment().toUpperCase();
        io.rong.imlib.model.Conversation.ConversationType type = io.rong.imlib.model.Conversation.ConversationType.valueOf(typeStr);

        String targetId = uri.getQueryParameter("targetId");

        String title = uri.getQueryParameter("title");

        if (type == null)
            return;

        mConversation = Conversation.obtain(type, targetId, title);

        if (mConversation != null) {
            setCurrentConversation(mConversation);
        }

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        RLog.d(this, "onDestroyView", "the primary input provider is:" + RongContext.getInstance().getPrimaryInputProvider());

        RongContext.getInstance().getPrimaryInputProvider().onDetached();

        if (RongContext.getInstance().getSecondaryInputProvider() != null) {
            RongContext.getInstance().getSecondaryInputProvider().onDetached();
        }
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    private DispatchResultFragment getDispatchFragment(Fragment fragment) {
        if (fragment instanceof DispatchResultFragment)
            return (DispatchResultFragment) fragment;

        if (fragment.getParentFragment() == null)
            throw new RuntimeException(fragment.getClass().getName() + " must has a parent fragment instance of DispatchFragment.");

        return getDispatchFragment(fragment.getParentFragment());
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        return false;
    }

    public void startActivityFromProvider(InputProvider provider, Intent intent, int requestCode) {
        if (requestCode == -1) {
            startActivityForResult(intent, -1);
            return;
        }
        if ((requestCode & 0xffffff80) != 0) {
            throw new IllegalArgumentException("Can only use lower 7 bits for requestCode");
        }

        getDispatchFragment(this).startActivityForResult(this, intent, ((provider.getIndex() + 1) << 7) + (requestCode & 0x7f));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        int index = requestCode >> 7;
        if (index != 0) {
            index--;
            if (index > RongContext.getInstance().getRegisteredExtendProviderList(mConversation.getConversationType()).size() + 1) {
                RLog.w(this, "onActivityResult", "Activity result provider index out of range: 0x"
                        + Integer.toHexString(requestCode));
                return;
            }

            if (index == 0) {
                RongContext.getInstance().getPrimaryInputProvider().onActivityResult(requestCode & 0x7f, resultCode, data);
            } else if (index == 1) {
                RongContext.getInstance().getSecondaryInputProvider().onActivityResult(requestCode & 0x7f, resultCode, data);
            } else {
                RongContext.getInstance().getRegisteredExtendProviderList(mConversation.getConversationType()).get(index - 2).onActivityResult(requestCode & 0x7f, resultCode, data);
            }

            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
