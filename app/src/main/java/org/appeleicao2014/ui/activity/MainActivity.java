package org.appeleicao2014.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.facebook.widget.ProfilePictureView;

import org.appeleicao2014.R;
import org.appeleicao2014.session.Session;
import org.appeleicao2014.ui.adapter.Item;
import org.appeleicao2014.ui.adapter.MenuAdapter;
import org.appeleicao2014.ui.fragment.FragmentCandidates;
import org.appeleicao2014.ui.fragment.FragmentCandidatesLess;
import org.appeleicao2014.ui.fragment.FragmentFavorite;
import org.appeleicao2014.ui.fragment.FragmentState;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.Util;

public class MainActivity extends ActionBarActivity  implements SearchView.OnQueryTextListener  {
    private String[] mLeftMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FragmentManager mFragManager;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mHeaderView;
    private MenuItem searchMenuItem;
    private int currentPosition;
    public static String KEY_POSITION = "currentPosition";
    private TextView mTvUser;
    private ProfilePictureView mivPhotoFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragManager = getSupportFragmentManager();

        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "org.appeleicao2014",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.d("KeyHash:", e.getMessage());
        }*/

        mLeftMenuTitles = getResources().getStringArray(R.array.left_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mHeaderView = ((LayoutInflater) this
                .getSystemService(this.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.list_item_left_menu_header, null, false);

        mTvUser = (TextView) mHeaderView.findViewById(R.id.tvNameUser);
        mivPhotoFacebook = (ProfilePictureView) mHeaderView.findViewById(R.id.ivPhotoUser);

        loadData();
        initListener();

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_POSITION);
            selectItem(currentPosition);
        }
        else
        {
            int i = getIntent().getIntExtra(KEY_POSITION, 2);
            selectItem(i);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_POSITION, currentPosition);
    }


    private void loadData()
    {

        if(Session.getInstance().getCurrentUser() != null)
        {
            mTvUser.setText(Session.getInstance().getCurrentUser().getName());
            mivPhotoFacebook.setProfileId(Session.getInstance().getCurrentUser().getIdSocial());
            mDrawerList.addHeaderView(mHeaderView, null, false);
        }
        else
        {
            View headerView = ((LayoutInflater) this
                .getSystemService(this.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.list_item_left_menu_header_empty, null, false);

            mDrawerList.addHeaderView(headerView, null, false);
        }

        mDrawerList.getBackground().setAlpha(250);
        mDrawerList.setAdapter(initMenu());

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {

                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }
        };







    }

    private MenuAdapter initMenu()
    {
        MenuAdapter mAdapter = new MenuAdapter(this);
        mAdapter.addSectionHeaderItem(new Item(getString(R.string.title_candidate_menu)));
        mAdapter.addItem(new Item(R.drawable.ic_candidates, getString(R.string.title_president), 0, false));
        mAdapter.addItem(new Item(R.drawable.ic_candidates, getString(R.string.title_governor), 0, false));
        mAdapter.addItem(new Item(R.drawable.ic_candidates, getString(R.string.title_senator), 0, false));
        mAdapter.addItem(new Item(R.drawable.ic_candidates, getString(R.string.title_deputy_federal), 0, false));
        mAdapter.addItem(new Item(R.drawable.ic_candidates, getString(R.string.title_deputy_state), 0, false));
        mAdapter.addSectionHeaderItem(new Item(getString(R.string.title_my_favorite_menu)));
        mAdapter.addItem(new Item(R.drawable.ic_favorite, getString(R.string.title_favorite), 0, false));
        mAdapter.addSectionHeaderItem(new Item(getString(R.string.title_settings_menu)));
        mAdapter.addItem(new Item(R.drawable.ic_location, getString(R.string.change_state), Util.returnImageUf(this), true));

        return mAdapter;
    }

    private void initListener()
    {
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        query = query.trim();

        if(query.isEmpty())
            return false;

        if(!Util.isNumeric(query))
        {
            if(query.length() < 4)
            {
                Util.alertDialog(this, getString(R.string.search_msg));
                return false;
            }
        }

        Intent i = new Intent();
        i.setClass( getApplicationContext(), SearchActivity.class);
        i.putExtra(SearchActivity.KEY_QUERY, query);

        if(currentPosition == 4 )
            i.putExtra(SearchActivity.KEY_JOB, Constants.DEPUTYFEDERAL);

        else if(currentPosition == 5)
            i.putExtra(SearchActivity.KEY_JOB, Constants.DEPUTYSTATE);

        startActivity(i);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();

        getSupportActionBar().setSubtitle(null);




        switch (position) {


            case 2:
                bundle.putString(FragmentCandidates.KEY_JOB, Constants.PRESIDENT);
                fragment = new FragmentCandidates();
                fragment.setArguments(bundle);

                getSupportActionBar().setTitle(Util.returnJobTitle(this, Constants.PRESIDENT));

                break;
            case 3:
                bundle.putString(FragmentCandidates.KEY_JOB, Constants.GOVERNOR);
                fragment = new FragmentCandidates();
                fragment.setArguments(bundle);

                getSupportActionBar().setTitle(Util.returnJobTitle(this, Constants.GOVERNOR));
                getSupportActionBar().setSubtitle(Util.returnDescricaoUf(this));
                break;
            case 4:
                bundle.putString(FragmentCandidates.KEY_JOB, Constants.SENATOR);
                fragment = new FragmentCandidates();
                fragment.setArguments(bundle);

                getSupportActionBar().setTitle(Util.returnJobTitle(this, Constants.SENATOR));
                getSupportActionBar().setSubtitle(Util.returnDescricaoUf(this));
                break;
            case 5:
                bundle.putString(FragmentCandidates.KEY_JOB, Constants.DEPUTYFEDERAL);
                fragment = new FragmentCandidatesLess();
                fragment.setArguments(bundle);

                getSupportActionBar().setTitle(Util.returnJobTitle(this, Constants.DEPUTYFEDERAL));
                getSupportActionBar().setSubtitle(Util.returnDescricaoUf(this));

                break;
            case 6:
                bundle.putString(FragmentCandidates.KEY_JOB, Constants.DEPUTYSTATE);
                fragment = new FragmentCandidatesLess();
                fragment.setArguments(bundle);
                getSupportActionBar().setTitle(Util.returnJobTitle(this, Constants.DEPUTYSTATE));
                getSupportActionBar().setSubtitle(Util.returnDescricaoUf(this));

                break;
            case 8:
                fragment = new FragmentFavorite();
                getSupportActionBar().setTitle(R.string.title_favorite);
                break;
            case 10:
                bundle.putBoolean(FragmentState.KEY_CHANGE_STATE, true);
                fragment = new FragmentState();
                fragment.setArguments(bundle);
                getSupportActionBar().setTitle(R.string.change_state);
                break;
        }

        mFragManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);

        currentPosition = position;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchMenuItem = menu.findItem(R.id.menu_search);
        searchMenuItem.setVisible(false);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);

        if(currentPosition == 4 ) {
            searchMenuItem.setVisible(true);
            searchView.setQueryHint(Html.fromHtml("<font color = #77ffaa>" + getResources().getString(R.string.title_search_hint_federal) + "</font>"));
        }
        else
            if(currentPosition == 5)
            {
                searchMenuItem.setVisible(true);
                searchView.setQueryHint(Html.fromHtml("<font color = #77ffaa>" + getResources().getString(R.string.title_search_hint_state) + "</font>"));
            }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
