package org.appeleicao2014.task;

import android.content.Context;
import android.os.AsyncTask;
import org.appeleicao2014.model.Statistics;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.service.Service;
import org.appeleicao2014.util.Callback;


/**
 * Created by thaleslima on 7/8/14.
 */
public class LoadCandidateStatistics extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private int mProcess;
    private Callback mCallback;
    private Statistics mStatistics;
    private Exception mException;
    private String mIdCandidate;


    public LoadCandidateStatistics(Context context, int process, Callback callback, String idCandidate)
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

            mStatistics = PersistenceManager.getInstance().getStatistics(mIdCandidate);

            if(mStatistics == null) {
                mStatistics = Service.loadCandidateStatistics(mContext, mIdCandidate);
                PersistenceManager.getInstance().saveStatistics(mStatistics, mIdCandidate);
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
            mCallback.onPostExecute(mProcess, mStatistics, false, null);
        }
        else
        {
            mException.printStackTrace();
            mCallback.onPostExecute(mProcess, mStatistics, true, mException.getMessage());
        }
    }
}