package org.appeleicao2014.task;

import android.content.Context;
import android.os.AsyncTask;

import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.model.Equity;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.service.Service;
import org.appeleicao2014.util.Callback;

import java.util.List;


/**
 * Created by thaleslima on 7/8/14.
 */
public class LoadCandidateEquityTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private int mProcess;
    private Callback mCallback;
    private List<Equity> mEquities;
    private Exception mException;
    private String mIdCandidate;

    public LoadCandidateEquityTask(Context context, int process, Callback callback, String idCandidate)
    {
        mContext = context;
        mProcess = process;
        mCallback = callback;
        mIdCandidate = idCandidate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.onPreExecute(mProcess);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            mEquities = PersistenceManager.getInstance().getEquities(mIdCandidate);

            if(mEquities.size() == 0) {
                mEquities = Service.loadCandidateEquity(mContext, mIdCandidate);
                PersistenceManager.getInstance().saveEquities(mEquities, mIdCandidate);
            }

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
            mCallback.onPostExecute(mProcess, mEquities, false, null);
        }
        else
        {
            mException.printStackTrace();
            mCallback.onPostExecute(mProcess, mEquities, true, mException.getMessage());
        }
    }
}