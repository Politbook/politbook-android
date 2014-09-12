package org.appeleicao2014.ui.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.appeleicao2014.model.Party;
import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.task.LoadCandidatesLessTask;
import org.appeleicao2014.ui.activity.CandidateActivity;
import org.appeleicao2014.ui.adapter.AdapterCandidate;
import org.appeleicao2014.ui.adapter.AdapterCandidateWithoutRating;
import org.appeleicao2014.util.Callback;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.EndlessScrollListener;
import org.appeleicao2014.util.Util;

import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class FragmentCandidatesLess extends Fragment implements Callback, View.OnClickListener, EndlessScrollListener.OnLoadMoreListener {
    public List<Candidate> mCandidates;
    public AdapterCandidateWithoutRating mAdapterTables;
    private ProgressBar mpbProgressBarLess;
    private GridView mListCandidates;
    private RelativeLayout mllMessage;
    private Button mbtConnection;
    private List<Party> parties;
    private Spinner mspParty;

    public static String KEY_JOB = "job";
    public String mJobTitle;

    private boolean loadData = false;
    private int position;
    private TextView mtvNoCandidates;
    private boolean loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candidates_less, container, false);

        mListCandidates = (GridView) rootView.findViewById(R.id.lvCandidates);
        mpbProgressBarLess = (ProgressBar) rootView.findViewById(R.id.pbProgressBarLess);
        mllMessage = (RelativeLayout) rootView.findViewById(R.id.llMessage);
        mbtConnection = (Button) rootView.findViewById(R.id.btConnection);
        mspParty = (Spinner) rootView.findViewById(R.id.spParty);
        mtvNoCandidates = (TextView) rootView.findViewById(R.id.tvNoCandidates);

        initListener();

        //Log.i(Constants.DEBUG, "FragmentCandidatesLess.onCreateView");
        return rootView;
    }

    private EndlessScrollListener.OnLoadMoreListener getOnLoadMoreListener()
    {
        return this;
    }

    private void loadData() {
        if(getArguments() != null)
            mJobTitle = getArguments().getString(KEY_JOB);

        loading = true;
        if(parties == null) {
            position = Util.getFilterParty(this.getActivity(), mJobTitle);

            parties = PersistenceManager.getInstance().getParties();
            ArrayAdapter<Party> adapter = new ArrayAdapter<Party>(this.getActivity(), android.R.layout.simple_spinner_item, parties);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mspParty.setAdapter(adapter);

            mspParty.setSelection(position);
        }

        if(mCandidates == null)
        {
            mCandidates = PersistenceManager.getInstance().getCandidates(mJobTitle, Util.getUfDefault(this.getActivity()), "", getIdParty());
            mAdapterTables = new AdapterCandidateWithoutRating(getActivity(), mCandidates);
            mListCandidates.setAdapter(mAdapterTables);
            mListCandidates.setOnScrollListener(new EndlessScrollListener(getOnLoadMoreListener()));

            //Log.i(Constants.DEBUG, "FragmentCandidatesLess.loadData");
        }
    }


    private String getIdParty()
    {
        if(parties != null && parties.size() > 0 && position > 0)
            return parties.get(position).getId();

        return "S";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Log.i(Constants.DEBUG, "FragmentCandidatesLess.onActivityCreated");
    }

    @Override
    public void onResume() {
        //Log.i(Constants.DEBUG, "FragmentCandidatesLess.onResume");
        super.onResume();

        loadData();
    }

    private void initListener() {
        mListCandidates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), CandidateActivity.class);

                mCandidates.get(i).setIdJobTitle(mJobTitle);

                intent.putExtra(CandidateActivity.KEY_CANDIDATE, mCandidates.get(i));
                startActivity(intent);
            }
        });

        mspParty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.color_action_tab_bar));
                mtvNoCandidates.setVisibility(View.GONE);

                if(loadData)
                {
                    position = i;
                    Util.setFilterParty(view.getContext(), mJobTitle, i);

                    mCandidates = PersistenceManager.getInstance().getCandidates(mJobTitle, Util.getUfDefault(view.getContext()), "", getIdParty());
                    mAdapterTables = new AdapterCandidateWithoutRating(getActivity(), mCandidates);
                    mListCandidates.setAdapter(mAdapterTables);
                    mListCandidates.setOnScrollListener(new EndlessScrollListener(getOnLoadMoreListener()));

                    //Log.i(Constants.DEBUG, "setOnItemSelectedListener: size " + mCandidates.size());
                }
                else
                {
                    loadData = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mbtConnection.setOnClickListener(this);
    }

    @Override
    public void onPostExecute(int process, Object object, boolean error, String descriptionError) {
        mpbProgressBarLess.setVisibility(View.INVISIBLE);
        mllMessage.setVisibility(View.INVISIBLE);
        mspParty.setEnabled(true);

        if(!error)
        {
            List<Candidate> items = (List<Candidate>) object;
            PersistenceManager.getInstance().saveCandidates(items, mJobTitle, getIdParty());
            mCandidates.addAll(items);
            mAdapterTables.notifyDataSetChanged();

            //Log.i(Constants.DEBUG, "" + mCandidates.size());

            if(mCandidates.size() == 0)
                mtvNoCandidates.setVisibility(View.VISIBLE);
        }
        else
        {
            mllMessage.setVisibility(View.VISIBLE);
        }

        Log.i(Constants.DEBUG, "FragmentCandidatesLess.onPostExecute");
    }

    @Override
    public void onPreExecute(int process) {
        Log.i(Constants.DEBUG, "FragmentCandidatesLess.onPreExecute");
        mpbProgressBarLess.setVisibility(View.VISIBLE);
        mllMessage.setVisibility(View.INVISIBLE);
        mtvNoCandidates.setVisibility(View.GONE);
        mspParty.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        onLoadMore(mCandidates.size(),mCandidates.size());
    }


    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Party party = parties.get(position);

        if(loading)
            new LoadCandidatesLessTask(this.getActivity(), 0, this, Util.getUfDefault(this.getActivity()), mJobTitle, totalItemsCount, party.getId()).execute();
    }
}
