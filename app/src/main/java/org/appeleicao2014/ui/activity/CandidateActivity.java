package org.appeleicao2014.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.common.view.SlidingTabLayout;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.ui.adapter.AdapterSectionsCandidate;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.Util;

/**
 * Created by thaleslima on 8/21/14.
 */
public class CandidateActivity extends ActionBarActivity {
    public static String KEY_CANDIDATE = "Candidate";

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private MenuItem itemFavorite;
    private boolean mFavorite;
    private Candidate mCandidate;

    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if ((exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title)
                    .setMessage(R.string.no_candidates)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        } if (state == SessionState.OPENED_TOKEN_UPDATED) {
            //handlePendingAction();
        }
        //updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        loadData();
        initListener();

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.bt_blue));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadData()
    {
        mCandidate = (Candidate) getIntent().getSerializableExtra(KEY_CANDIDATE);

        if(mCandidate != null) {

            getSupportActionBar().setTitle(mCandidate.getNickname());
            getSupportActionBar().setSubtitle(Util.returnJobTitle(this, mCandidate.getIdJobTitle()) + " " + mCandidate.getState());
            mFavorite = PersistenceManager.getInstance().getFavorite(mCandidate.getId()) != null;

            AdapterSectionsCandidate sectionsCandidates = new AdapterSectionsCandidate(getSupportFragmentManager(),
                    this, mCandidate.getId(), mCandidate.getIdJobTitle(), mCandidate.getState(), mCandidate.getNickname());

            mViewPager.setAdapter(sectionsCandidates);
            mSlidingTabLayout.setViewPager(mViewPager);
            mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.bt_blue_focus);
                }

                @Override
                public int getDividerColor(int position) {
                    return getResources().getColor(R.color.bt_blue_focus);
                }
            });
        }
    }

    private void initListener()
    {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.candidate, menu);
        itemFavorite = menu.findItem(R.id.menu_favorite);
        alterIconFavorite();
        return true;
    }

    private void alterIconFavorite()
    {
        if(mFavorite)
            itemFavorite.setIcon(R.drawable.rating_important_dark);
        else
            itemFavorite.setIcon(R.drawable.rating_not_important_dark);
    }
    private void onClickFavorite()
    {
        mFavorite = !mFavorite;

        if(mFavorite) {
            PersistenceManager.getInstance().saveFavorite(mCandidate);
            Toast.makeText(this, R.string.add_favorite, Toast.LENGTH_SHORT).show();
        }
        else {
            PersistenceManager.getInstance().removeFavorite(mCandidate.getId());
            Toast.makeText(this, R.string.remove_favorite, Toast.LENGTH_SHORT).show();
        }

        alterIconFavorite();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                onClickFavorite();
                return true;
            case android.R.id.home:

                int key = 2;

                if(mCandidate != null) {
                    if(mCandidate.getIdJobTitle().equals(Constants.GOVERNOR))
                    {
                        key = 3;
                    }
                    else if(mCandidate.getIdJobTitle().equals(Constants.SENATOR))
                    {
                        key = 4;
                    }
                    else if(mCandidate.getIdJobTitle().equals(Constants.DEPUTYFEDERAL))
                    {
                        key = 5;
                    }
                    else if(mCandidate.getIdJobTitle().equals(Constants.DEPUTYSTATE))
                    {
                        key = 6;
                    }
                }

                Intent upIntent = new Intent(this, MainActivity.class);
                upIntent.putExtra(MainActivity.KEY_POSITION, key);

                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
