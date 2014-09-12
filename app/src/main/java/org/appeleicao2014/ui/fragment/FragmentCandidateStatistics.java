package org.appeleicao2014.ui.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.appeleicao2014.R;
import org.appeleicao2014.model.Statistics;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.task.LoadCandidateStatistics;
import org.appeleicao2014.task.LoadCandidatesTask;
import org.appeleicao2014.util.Callback;
import org.appeleicao2014.util.Util;

/**
 * Created by thaleslima on 8/20/14.
 */
public class FragmentCandidateStatistics extends Fragment implements Callback, View.OnClickListener {
    public static String KEY_ID_CANDIDATE = "idCandidate";

    private TextView mtvFaultsPlenary;
    private TextView mtvAveragePlenary;
    private TextView mtvFaultsCommissions;
    private TextView mtvAverageCommissions;
    private TextView mtvEvolution;
    private TextView mtvReferenceYear;
    private TextView mtvAmendments;
    private TextView mtvAverageAmendments;


    private ProgressBar mpbProgressBar;
    private LinearLayout mllStatistics;
    private LinearLayout mllMessage;
    private Button mbtConnection;
    private Statistics mStatistics;
    private String mId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candidate_statistics, container, false);

        mtvFaultsPlenary = (TextView) rootView.findViewById(R.id.tvFaultsPlenary);
        mtvAveragePlenary = (TextView) rootView.findViewById(R.id.tvAveragePlenary);
        mtvFaultsCommissions = (TextView) rootView.findViewById(R.id.tvFaultsCommissions);
        mtvAverageCommissions = (TextView) rootView.findViewById(R.id.tvAverageCommissions);
        mtvEvolution = (TextView) rootView.findViewById(R.id.tvEvolution);
        mtvReferenceYear = (TextView) rootView.findViewById(R.id.tvReferenceYear);
        mtvAmendments = (TextView) rootView.findViewById(R.id.tvAmendments);
        mtvAverageAmendments = (TextView) rootView.findViewById(R.id.tvAverageAmendments);
        mllMessage = (LinearLayout) rootView.findViewById(R.id.llMessage);
        mllStatistics = (LinearLayout) rootView.findViewById(R.id.llStatistics);
        mpbProgressBar = (ProgressBar) rootView.findViewById(R.id.pbProgressBar);
        mbtConnection = (Button) rootView.findViewById(R.id.btConnection);

        loadData();
        initListener();

        return rootView;
    }

    private void loadData() {
         mId = getArguments().getString(KEY_ID_CANDIDATE);

        mStatistics = PersistenceManager.getInstance().getStatistics(mId);

        if(mStatistics == null) {
            new LoadCandidateStatistics(this.getActivity(), 0, this, mId).execute();
        }
        else
        {
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
        if(!error) {
            mStatistics = (Statistics) object;
            initData();
        }
        else
        {
            mllMessage.setVisibility(View.VISIBLE);
        }
    }

    private void initData()
    {
        try
        {
            mtvFaultsPlenary.setText(Util.valueNumber(mStatistics.getFaultsPlenary()));
            mtvAveragePlenary.setText(Util.valueNumber(mStatistics.getAveragePlenary()));
            mtvFaultsCommissions.setText(Util.valueNumber(mStatistics.getFaultsCommissions()));
            mtvAverageCommissions.setText(Util.valueNumber(mStatistics.getAverageCommissions()));
            mtvEvolution.setText(Util.valueNumber(mStatistics.getEvolution()));
            mtvReferenceYear.setText(Util.valueNumber(mStatistics.getReferenceYear()));
            mtvAmendments.setText(Util.valueNumber(mStatistics.getAmendments()));
            mtvAverageAmendments.setText(Util.valueNumber(mStatistics.getAverageAmendments()));

            mllStatistics.setVisibility(View.VISIBLE);

        }catch (Exception ex)
        {

        }

    }

    @Override
    public void onPreExecute(int process) {
        mllStatistics.setVisibility(View.GONE);
        mpbProgressBar.setVisibility(View.VISIBLE);
        mllMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        new LoadCandidateStatistics(this.getActivity(), 0, this, mId).execute();
    }
}
