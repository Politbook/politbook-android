package org.appeleicao2014.task;

import android.content.Context;
import android.os.AsyncTask;

import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.service.Service;
import org.appeleicao2014.util.Callback;

import java.util.List;


/**
 * Created by thaleslima on 7/8/14.
 */
public class LoadCandidateAboutTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private int mProcess;
    private Callback mCallback;
    private Candidate mCandidate;
    private Exception mException;
    private String mIdCandidate;
    private String mJobTitle;

    public LoadCandidateAboutTask(Context context, int process, Callback callback, String idCandidate, String mJobTitle)
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
            mCandidate = PersistenceManager.getInstance().getCandidate(mIdCandidate);

            if (mCandidate == null) {
                mCandidate = Service.loadCandidate(mContext, mIdCandidate);
                //PersistenceManager.getInstance().saveCandidate(mCandidate, mJobTitle);
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
            mCallback.onPostExecute(mProcess, mCandidate, false, null);
        }
        else
        {
            mException.printStackTrace();
            mCallback.onPostExecute(mProcess, mCandidate, true, mException.getMessage());
        }
    }
}