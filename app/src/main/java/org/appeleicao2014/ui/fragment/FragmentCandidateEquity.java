package org.appeleicao2014.ui.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import org.appeleicao2014.R;
import org.appeleicao2014.model.Equity;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.task.LoadCandidateEquityTask;
import org.appeleicao2014.ui.adapter.AdapterEquity;
import org.appeleicao2014.util.Callback;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

/**
 * Created by thaleslima on 8/20/14.
 */
public class FragmentCandidateEquity extends Fragment implements Callback, View.OnClickListener {

    public static String KEY_ID_CANDIDATE = "idCandidate";

    public List<Equity> mEquities;
    public AdapterEquity mAdapterEquity;
    private ProgressBar mpbProgressBar;
    private ListView mListEquities;
    private BarGraph mBarGraph;
    private LinearLayout mllMessage;
    private Button mbtConnection;
    private String mId;
    private TextView mtvNoEquity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candidate_equity, container, false);

        mListEquities = (ListView) rootView.findViewById(R.id.lvEquities);
        mpbProgressBar = (ProgressBar) rootView.findViewById(R.id.pbProgressBar);
        mllMessage = (LinearLayout) rootView.findViewById(R.id.llMessage);
        mBarGraph = (BarGraph)rootView.findViewById(R.id.graph);
        mbtConnection = (Button) rootView.findViewById(R.id.btConnection);
        mtvNoEquity = (TextView) rootView.findViewById(R.id.tvNoEquity);

        loadData();
        initListener();

        return rootView;
    }

    private void loadData() {
        mId = getArguments().getString(KEY_ID_CANDIDATE);

        mEquities = PersistenceManager.getInstance().getEquities(mId);
        mAdapterEquity = new AdapterEquity(getActivity(), mEquities);
        mListEquities.setAdapter(mAdapterEquity);

        if (mEquities.size() == 0) {
            new LoadCandidateEquityTask(this.getActivity(), 0, this, mId).execute();
        }
        else {
            initData();
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

        Log.i(Constants.DEBUG, "FragmentCandidateEquity.onPostExecute1");

        if (!error) {
            mEquities.clear();
            mEquities.addAll((Collection<? extends Equity>) object);
            mAdapterEquity.notifyDataSetChanged();

            if(mEquities.size() == 0)
            {
                mtvNoEquity.setVisibility(View.VISIBLE);
            }

            initData();
        } else {
            mllMessage.setVisibility(View.VISIBLE);
        }

        Log.i(Constants.DEBUG, "FragmentCandidateEquity.onPostExecute2");
    }

    private void initData()
    {
        try {

            if (mEquities.size() > 0 && mBarGraph != null) {
                Hashtable<String, Float> dict = new Hashtable<String, Float>();
                SortedSet<String> key = new TreeSet<String>();

                Stack<Integer> colors = new Stack<Integer>();

                colors.push(getResources().getColor(R.color.frame_yellow));
                colors.push(getResources().getColor(R.color.frame_orange));
                colors.push(getResources().getColor(R.color.frame_blue));
                colors.push(getResources().getColor(R.color.frame_green));
                colors.push(getResources().getColor(R.color.frame_red));

                float valor;
                for (Equity equity : mEquities) {
                    valor = Util.parseFloat(equity.getAmount());

                    if (dict.containsKey(equity.getYear())) {
                        valor += dict.get(equity.getYear());
                        dict.remove(equity.getYear());
                        dict.put(equity.getYear(), valor);
                    } else {
                        key.add(equity.getYear());
                        dict.put(equity.getYear(), valor);
                    }
                }

                ArrayList<Bar> points = new ArrayList<Bar>();
                Bar d;

                for(String item : key)
                {
                    d = new Bar();
                    int color = colors.pop();
                    d.setColor(color);
                    d.setName(item);
                    d.setValue(dict.get(item));
                    points.add(d);

                    for (Equity equity : mEquities) {
                        if(equity.getYear().equals(item))
                        {
                            equity.setColor(color);
                        }
                    }
                }

                mBarGraph.setBars(points);
                mBarGraph.setUnit("R$");

                mBarGraph.setVisibility(View.VISIBLE);
            }
            else
            {
                if(mBarGraph != null)
                {
                    mBarGraph.setVisibility(View.INVISIBLE);
                }

                mAdapterEquity.notifyDataSetChanged();
            }

        } catch (Exception ex) {
            Log.i(Constants.DEBUG, "FragmentCandidateEquity.initData: " + ex.getMessage());

            if(mBarGraph != null)
            {
                mBarGraph.setVisibility(View.INVISIBLE);
            }

            mAdapterEquity.notifyDataSetChanged();
        }
    }

    @Override
    public void onPreExecute(int process) {
        mpbProgressBar.setVisibility(View.VISIBLE);
        mllMessage.setVisibility(View.INVISIBLE);

        Log.i(Constants.DEBUG, "FragmentCandidateEquity.onPreExecute");

    }

    @Override
    public void onClick(View view) {
        new LoadCandidateEquityTask(this.getActivity(), 0, this, mId).execute();
    }
}
