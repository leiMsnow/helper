package io.rong.imkit;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.Window;

import io.rong.imkit.common.RongConst;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.fragment.SubConversationListFragment;


/**
 * Created by DragonJ on 14-7-22.
 */
public class RongActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.rc_activity);
        Intent intent = getIntent();

        if (intent != null && arg0 == null) {
            Fragment fragment = null;

            if (intent.getExtras() != null && intent.getExtras().containsKey(RongConst.EXTRA.CONTENT)) {
                String fragmentName = intent.getExtras().getString(RongConst.EXTRA.CONTENT);
                fragment = Fragment.instantiate(this, fragmentName);
            } else if (intent.getData() != null) {
                if (intent.getData().getPathSegments().get(0).equals("conversation")) {
                    String fragmentName = ConversationFragment.class.getCanonicalName();
                    fragment = Fragment.instantiate(this, fragmentName);
                } else if (intent.getData().getLastPathSegment().equals("conversationlist")) {
                    String fragmentName = ConversationListFragment.class.getCanonicalName();
                    fragment = Fragment.instantiate(this, fragmentName);
                }else if(intent.getData().getLastPathSegment().equals("subconversationlist")){
                    String fragmentName = SubConversationListFragment.class.getCanonicalName();
                    fragment = Fragment.instantiate(this,fragmentName);
                }
            }

            if (fragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.rc_content, fragment);
                transaction.addToBackStack(null).commitAllowingStateLoss();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        setIntent(intent);

        Fragment fragment = null;

        if (intent.getExtras() != null && intent.getExtras().containsKey(RongConst.EXTRA.CONTENT)) {
            String fragmentName = intent.getExtras().getString(RongConst.EXTRA.CONTENT);
            fragment = Fragment.instantiate(this, fragmentName);
        } else if (intent.getData() != null) {
            if (intent.getData().getPathSegments().get(0).equals("conversation")) {
                String fragmentName = ConversationFragment.class.getCanonicalName();
                fragment = Fragment.instantiate(this, fragmentName);
            } else if (intent.getData().getLastPathSegment().equals("conversationlist")) {
                String fragmentName = ConversationListFragment.class.getCanonicalName();
                fragment = Fragment.instantiate(this, fragmentName);
            } else if(intent.getData().getLastPathSegment().equals("subconversationlist")){
                String fragmentName = SubConversationListFragment.class.getCanonicalName();
                fragment = Fragment.instantiate(this,fragmentName);
            }
        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.rc_content, fragment);
            transaction.addToBackStack(null).commitAllowingStateLoss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            super.onBackPressed();
            this.finish();
        } else {
            super.onBackPressed();
        }

    }
}
