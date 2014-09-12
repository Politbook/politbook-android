package org.appeleicao2014.ui.fragment;
import android.content.Intent;
import android.os.Bundle;
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
import org.appeleicao2014.task.LoadCandidatesRatingTask;
import org.appeleicao2014.task.LoadCandidatesTask;
import org.appeleicao2014.ui.activity.CandidateActivity;
import org.appeleicao2014.ui.adapter.AdapterCandidate;
import org.appeleicao2014.util.Callback;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.Util;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thaleslima on 8/20/14.
 */
public class FragmentCandidates extends Fragment implements Callback, View.OnClickListener {
    public List<Candidate> mCandidates;
    public AdapterCandidate mAdapterTables;
    private ProgressBar mpbProgressBar;
    private GridView mListCandidates;
    private RelativeLayout mllMessage;
    private Button mbtConnection;
    private Spinner mspParty;
    private List<Party> parties;
    public static String KEY_JOB = "job";
    public String mJobTitle;
    private boolean loadData = false;
    private int position;
    private static Map<String, Long> mapLoadRating;

    public static Map<String, Long> getMapLoadRating()
    {
        if(mapLoadRating == null)
        {
            mapLoadRating = new HashMap<String, Long>();
        }
        return mapLoadRating;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candidates, container, false);

        mListCandidates = (GridView) rootView.findViewById(R.id.lvCandidates);
        mpbProgressBar = (ProgressBar) rootView.findViewById(R.id.pbProgressBar);
        mllMessage = (RelativeLayout) rootView.findViewById(R.id.llMessage);
        mbtConnection = (Button) rootView.findViewById(R.id.btConnection);

        mspParty = (Spinner) rootView.findViewById(R.id.spParty);

        initListener();
        return rootView;
    }


    private void loadData() {
        if(getArguments() != null)
            mJobTitle = getArguments().getString(KEY_JOB);

        if(mCandidates == null) {
            position = Util.getFilterParty(this.getActivity(), mJobTitle);
            if(position == 0) {

                mCandidates = PersistenceManager.getInstance().getCandidates(mJobTitle, Util.getUfDefault(this.getActivity()), "", "");
                mAdapterTables = new AdapterCandidate(getActivity(), mCandidates);
                mListCandidates.setAdapter(mAdapterTables);

                if (mCandidates.isEmpty())
                    new LoadCandidatesTask(this.getActivity(), 0, this, Util.getUfDefault(this.getActivity()), mJobTitle).execute();
                else {
                    initParties();
                }
            }
            else
            {
                mCandidates = new ArrayList<Candidate>();
                mAdapterTables = new AdapterCandidate(getActivity(), mCandidates);
                mListCandidates.setAdapter(mAdapterTables);
                loadData = true;
                initParties();
            }
        }
        else
            loadRatingTask();
    }

    private void loadRatingTask()
    {
        long currentTime = Calendar.getInstance().getTime().getTime();
        long currentTime2 = 0;

        if(getMapLoadRating().containsKey(mJobTitle))
        {
            currentTime2 = getMapLoadRating().get(mJobTitle);
        }

        if(currentTime - currentTime2 > 200000) {
            Log.i(Constants.DEBUG, "loadRatingTask: " + currentTime + " - " + currentTime2);
            if (mCandidates != null && mCandidates.size() > 0) {
                new LoadCandidatesRatingTask(this.getActivity(), 1, this, Util.getUfDefault(this.getActivity()), mJobTitle).execute();
                getMapLoadRating().remove(mJobTitle);
                getMapLoadRating().put(mJobTitle, currentTime);
            }
        }
    }

    private void initParties()
    {
        if(parties == null) {
            parties = PersistenceManager.getInstance().getParties(mJobTitle, Util.getUfDefault(this.getActivity()));
            ArrayAdapter<Party> adapter = new ArrayAdapter<Party>(this.getActivity(), android.R.layout.simple_spinner_item, parties);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mspParty.setAdapter(adapter);

            if(parties.size() >  position)
                mspParty.setSelection(position);

            loadRatingTask();
        }
    }

    @Override
    public void onResume() {
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

                if(loadData)
                {
                    String party = i > 0 ? parties.get(i).getParty() : "";
                    mCandidates.clear();
                    mCandidates.addAll(PersistenceManager.getInstance().getCandidates(mJobTitle, Util.getUfDefault(view.getContext()), party, ""));
                    mAdapterTables.notifyDataSetChanged();

                    Util.setFilterParty(view.getContext(), mJobTitle, i);
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
        mpbProgressBar.setVisibility(View.INVISIBLE);
        mllMessage.setVisibility(View.INVISIBLE);

        if(process == 0) {
            if (!error) {
                mCandidates.clear();
                mCandidates.addAll((Collection<? extends Candidate>) object);
                mAdapterTables.notifyDataSetChanged();
                initParties();

            } else {
                mllMessage.setVisibility(View.VISIBLE);
            }
        }
        else if(process == 1 && !error && object != null)
        {
            List<Candidate> candidates = (List<Candidate>) object;

            if(candidates.size() > 0) {
                mCandidates.clear();
                mCandidates.addAll((Collection<? extends Candidate>) object);
                mAdapterTables.notifyDataSetChanged();
            }
        }

        Log.i(Constants.DEBUG, "FragmentCandidates.onPostExecute");
    }

    @Override
    public void onPreExecute(int process) {
        mllMessage.setVisibility(View.INVISIBLE);

        if(process == 0)
            mpbProgressBar.setVisibility(View.VISIBLE);

        Log.i(Constants.DEBUG, "FragmentCandidates.onPreExecute");
    }

    @Override
    public void onClick(View view) {
        new LoadCandidatesTask(this.getActivity(), 0, this, Util.getUfDefault(this.getActivity()), mJobTitle).execute();
    }
}
