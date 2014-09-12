package org.appeleicao2014.ui.fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;

import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidature;
import org.appeleicao2014.model.Equity;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.task.LoadCandidateCandidatureTask;
import org.appeleicao2014.task.LoadCandidateEquityTask;
import org.appeleicao2014.ui.adapter.AdapterCandidature;
import org.appeleicao2014.util.Callback;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

/**
 * Created by thaleslima on 8/20/14.
 */
public class FragmentCandidateCandidature extends Fragment implements Callback, View.OnClickListener {

    public static String KEY_ID_CANDIDATE = "idCandidate";

    public List<Candidature> mItems;
    public AdapterCandidature mAdapter;
    private ProgressBar mpbProgressBar;
    private ListView mList;
    private LinearLayout mllMessage;
    private Button mbtConnection;
    private String mId;
    private TextView mtvNoItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candidate_candidature, container, false);

        mList = (ListView) rootView.findViewById(R.id.lvCandidatures);
        mpbProgressBar = (ProgressBar) rootView.findViewById(R.id.pbProgressBar);
        mllMessage = (LinearLayout) rootView.findViewById(R.id.llMessage);
        mbtConnection = (Button) rootView.findViewById(R.id.btConnection);
        mtvNoItem = (TextView) rootView.findViewById(R.id.tvNoItem);

        loadData();
        initListener();

        return rootView;
    }

    private void loadData() {
        mId = getArguments().getString(KEY_ID_CANDIDATE);

        mItems = PersistenceManager.getInstance().getCandidatures(mId);
        mAdapter = new AdapterCandidature(getActivity(), mItems);
        mList.setAdapter(mAdapter);

        if(mItems.size() == 0)
        {
            new LoadCandidateCandidatureTask(this.getActivity(), 0, this, mId).execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initListener() {
        mbtConnection.setOnClickListener(this);
    }

    @Override
    public void onPostExecute(int process, Object object, boolean error, String descriptionError) {
        mpbProgressBar.setVisibility(View.INVISIBLE);
        mllMessage.setVisibility(View.INVISIBLE);

        Log.i(Constants.DEBUG, "FragmentCandidateCandidature.onPostExecute1");

        if (!error) {
            mItems.clear();
            mItems.addAll((Collection<? extends Candidature>) object);
            mAdapter.notifyDataSetChanged();

            if(mItems.size() == 0)
            {
                mtvNoItem.setVisibility(View.VISIBLE);
            }
        } else {
            mllMessage.setVisibility(View.VISIBLE);
        }

        Log.i(Constants.DEBUG, "FragmentCandidateCandidature.onPostExecute2");
    }

    @Override
    public void onPreExecute(int process) {
        mpbProgressBar.setVisibility(View.VISIBLE);
        mllMessage.setVisibility(View.INVISIBLE);

        Log.i(Constants.DEBUG, "FragmentCandidateCandidature.onPreExecute");

    }

    @Override
    public void onClick(View view) {
        new LoadCandidateCandidatureTask(this.getActivity(), 0, this, mId).execute();
    }
}
