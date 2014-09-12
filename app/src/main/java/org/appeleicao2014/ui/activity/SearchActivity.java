package org.appeleicao2014.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.task.LoadCandidatesSearchTask;
import org.appeleicao2014.ui.adapter.AdapterCandidate;
import org.appeleicao2014.ui.adapter.AdapterCandidateSearch;
import org.appeleicao2014.util.Callback;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.EndlessScrollListener;
import org.appeleicao2014.util.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SearchActivity extends ActionBarActivity implements Callback, OnItemClickListener, View.OnClickListener, SearchView.OnQueryTextListener, EndlessScrollListener.OnLoadMoreListener {
    public List<Candidate> mCandidates;
    public AdapterCandidateSearch mAdapterTables;
	private TextView mtvNotFound;
    private ListView mListCandidates;

	private String mQuery;
    private ProgressBar mpbProgressBarLess;
    private RelativeLayout mllMessage;
    private Button mbtConnection;

    public static final String KEY_QUERY = "query";
    public static final String KEY_JOB = "job";
    public String mJobTitle;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
        setContentView(R.layout.activity_search);

        mListCandidates  = (ListView) findViewById(R.id.lvCandidates);
        mtvNotFound = (TextView) findViewById(R.id.tvNotFound);
        mllMessage = (RelativeLayout) findViewById(R.id.llMessage);
        mbtConnection = (Button) findViewById(R.id.btConnection);
        mpbProgressBarLess = (ProgressBar) findViewById(R.id.pbProgressBarLess);

        loadData();
        initListener();
	}

    private void loadData()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mJobTitle = getIntent().getStringExtra(KEY_JOB);
        mQuery = getIntent().getStringExtra(KEY_QUERY);

        getSupportActionBar().setSubtitle(Util.returnJobTitle(this, mJobTitle) + " " + Util.getUfDefault(this));

        mCandidates = new ArrayList<Candidate>();
        mAdapterTables = new AdapterCandidateSearch(this, mCandidates);
        mListCandidates.setAdapter(mAdapterTables);

        mListCandidates.setOnScrollListener(new EndlessScrollListener(this));
        setTitle(mQuery);
    }

    private void initListener()
    {
        mListCandidates.setOnItemClickListener(this);
        mbtConnection.setOnClickListener(this);
    }
	
	private void updateList() {
		setTitle(mQuery);

        onLoadMore(mCandidates.size(),mCandidates.size());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
	    searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);


        if(mJobTitle.equals(Constants.DEPUTYFEDERAL)) {
            searchView.setQueryHint(Html.fromHtml("<font color = #77ffaa>" + getResources().getString(R.string.title_search_hint_federal) + "</font>"));
        }
        else if(mJobTitle.equals(Constants.DEPUTYSTATE))
        {
            searchView.setQueryHint(Html.fromHtml("<font color = #77ffaa>" + getResources().getString(R.string.title_search_hint_state) + "</font>"));
        }

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, MainActivity.class);
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


	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int i, long arg3) {
        Intent intent = new Intent(view.getContext(), CandidateActivity.class);

        mCandidates.get(i).setIdJobTitle(mJobTitle);

        intent.putExtra(CandidateActivity.KEY_CANDIDATE, mCandidates.get(i));
        startActivity(intent);
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

        mQuery = query;
		updateList();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}


    @Override
    public void onPostExecute(int process, Object object, boolean error, String descriptionError) {
        mpbProgressBarLess.setVisibility(View.INVISIBLE);
        mllMessage.setVisibility(View.INVISIBLE);
        mtvNotFound.setVisibility(View.INVISIBLE);

        if(!error)
        {
            //mCandidates.clear();
            mCandidates.addAll((Collection<? extends Candidate>) object);
            mAdapterTables.notifyDataSetChanged();

            if(mCandidates.size() == 0)
            {
                mtvNotFound.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            mllMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPreExecute(int process) {
        Util.closeVirtualKeyboard(this, mListCandidates);
        mllMessage.setVisibility(View.INVISIBLE);
        mtvNotFound.setVisibility(View.INVISIBLE);
        mpbProgressBarLess.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        updateList();
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        new LoadCandidatesSearchTask(this, 0, this, Util.getUfDefault(this), mJobTitle, page, mQuery).execute();
    }
}
