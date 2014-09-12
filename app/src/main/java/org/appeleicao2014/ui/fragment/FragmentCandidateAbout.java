package org.appeleicao2014.ui.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.task.LoadCandidateAboutTask;
import org.appeleicao2014.util.Callback;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.ImageLoader;
import org.appeleicao2014.util.Util;

/**
 * Created by thaleslima on 8/20/14.
 */
public class FragmentCandidateAbout extends Fragment implements Callback, View.OnClickListener {
    public static String KEY_ID_CANDIDATE = "idCandidate";
    public static String KEY_JOB = "job";

    private TextView mtvNickname;
    private TextView mtvPartyAndNumber;
    private TextView mtvName;
    private TextView mtvAge;
    private TextView mtvInstruction;
    private TextView mtvOccupation;
    private TextView mtvTitle;
    private TextView mtvEnrollment;
    private TextView mtvMiniBio;
    private TextView mtvPositions;
    private TextView mtvCountertops;
    private TextView mtvPrediction;
    private TextView mtvReelection;
    private TextView mtvProcesses;
    private ImageView mivPhoto;
    private Candidate mCandidate;
    private ProgressBar mpbProgressBar;
    private LinearLayout mllAbout;
    private LinearLayout mllMessage;
    private Button mbtConnection;
    private String mId;
    private String mJobTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candidate_about, container, false);

        mtvNickname = (TextView) rootView.findViewById(R.id.tvNickname);
        mtvPartyAndNumber = (TextView) rootView.findViewById(R.id.tvPartyAndNumber);
        mtvName = (TextView) rootView.findViewById(R.id.tvName);
        mtvAge = (TextView) rootView.findViewById(R.id.tvAge);
        mtvInstruction = (TextView) rootView.findViewById(R.id.tvInstruction);
        mtvOccupation = (TextView) rootView.findViewById(R.id.tvOccupation);
        mtvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        mtvEnrollment = (TextView) rootView.findViewById(R.id.tvEnrollment);
        mtvMiniBio = (TextView) rootView.findViewById(R.id.tvMiniBio);
        mtvPositions = (TextView) rootView.findViewById(R.id.tvPositions);
        mivPhoto = (ImageView) rootView.findViewById(R.id.ivPhoto);
        mllMessage = (LinearLayout) rootView.findViewById(R.id.llMessage);
        mpbProgressBar = (ProgressBar) rootView.findViewById(R.id.pbProgressBar);
        mllAbout = (LinearLayout) rootView.findViewById(R.id.llAbout);
        mbtConnection = (Button) rootView.findViewById(R.id.btConnection);
        mtvCountertops = (TextView) rootView.findViewById(R.id.tvCountertops);
        mtvPrediction = (TextView) rootView.findViewById(R.id.tvPrediction);
        mtvReelection = (TextView) rootView.findViewById(R.id.tvReelection);
        mtvProcesses = (TextView) rootView.findViewById(R.id.tvProcesses);
        loadData();
        initListener();

        return rootView;
    }

    private void loadData() {
        mId = getArguments().getString(KEY_ID_CANDIDATE);
        mJobTitle = getArguments().getString(KEY_JOB);

        mCandidate = PersistenceManager.getInstance().getCandidate(mId);

        if(mCandidate == null)
            new LoadCandidateAboutTask(this.getActivity(), 0, this, mId, mJobTitle).execute();
        else
            initData();
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
            mCandidate = (Candidate) object;
            initData();
        }
        else
        {
            mllMessage.setVisibility(View.VISIBLE);
        }

        Log.i(Constants.DEBUG, "FragmentCandidateAbout.onPostExecute");
    }

    private void initData()
    {
        try{
            getActivity().setTitle(mCandidate.getNickname());

            mtvNickname.setText(Util.valueString(mCandidate.getNickname()));
            mtvPartyAndNumber.setText(Util.valueString(mCandidate.getNumber() + " | " + mCandidate.getParty()));
            mtvName.setText(Util.valueString(mCandidate.getName()));
            mtvAge.setText(Util.valueString(mCandidate.getAge()));
            mtvInstruction.setText(Util.valueString(mCandidate.getInstruction()));
            mtvOccupation.setText(Util.valueString(mCandidate.getOccupation()));
            mtvTitle.setText(Util.valueString(mCandidate.getTitle()));
            mtvEnrollment.setText(Util.valueString(mCandidate.getEnrollment()));
            mtvMiniBio.setText(Html.fromHtml(Util.valueString(mCandidate.getMiniBio())));
            mtvPositions.setText(Util.valueString(Util.valueString(mCandidate.getPositions())));
            mtvCountertops.setText(Util.valueString(mCandidate.getCountertops()));
            mtvPrediction.setText(Util.formatPrice(mCandidate.getPrediction()));


            if(mCandidate.getReelection().equals("true"))
            {
                mtvReelection.setText(getString(R.string.yes));
            }
            else
            {
                mtvReelection.setText(getString(R.string.no));
            }

            mtvProcesses.setText(Html.fromHtml(Util.valueString(mCandidate.getProcesses())));

            new ImageLoader(getActivity(), R.drawable.photo_transparent).DisplayImage(mCandidate.getPhoto(), mivPhoto);

            mllAbout.setVisibility(View.VISIBLE);
        }
        catch (Exception ex)
        {
            Log.i(Constants.DEBUG, "FragmentCandidateAbout.initData: " + ex.getMessage());
        }
    }

    @Override
    public void onPreExecute(int process) {
        mllAbout.setVisibility(View.GONE);
        mpbProgressBar.setVisibility(View.VISIBLE);
        mllMessage.setVisibility(View.INVISIBLE);

        Log.i(Constants.DEBUG, "FragmentCandidateAbout.onPreExecute");
    }

    @Override
    public void onClick(View view) {
        new LoadCandidateAboutTask(this.getActivity(), 0, this, mId, mJobTitle).execute();
    }
}
