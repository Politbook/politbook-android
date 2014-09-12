package org.appeleicao2014.task;

import android.content.Context;
import android.os.AsyncTask;

import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.service.Service;
import org.appeleicao2014.util.Callback;

import java.util.List;


/**
 * Created by thaleslima on 7/8/14.
 */
public class LoadCandidatesSearchTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private int mProcess;
    private Callback mCallback;
    private List<Candidate> mCandidates;
    private Exception mException;
    private String mState;
    private String mJobTitle;
    private int mPage;
    private String mName;

    public LoadCandidatesSearchTask(Context context, int process, Callback callback, String state, String jobTitle, int page, String name)
    {
        mContext = context;
        mProcess = process;
        mCallback = callback;
        mState = state;
        mJobTitle = jobTitle;
        mPage = page;
        mName = name;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.onPreExecute(mProcess);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {

             mCandidates = Service.loadCandidatesSearch(mContext, mState, mJobTitle, mPage, mName);

        } catch (Exception ex) {
            mException = ex;
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        if(response)
        {
            mCallback.onPostExecute(mProcess, mCandidates, false, null);
        }
        else
        {
            mException.printStackTrace();
            mCallback.onPostExecute(mProcess, mCandidates, true, mException.getMessage());
        }
    }
}