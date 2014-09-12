package org.appeleicao2014.task;

import android.content.Context;
import android.os.AsyncTask;

import org.appeleicao2014.model.Candidature;
import org.appeleicao2014.model.Equity;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.service.Service;
import org.appeleicao2014.util.Callback;

import java.util.List;


/**
 * Created by thaleslima on 7/8/14.
 */
public class LoadCandidateCandidatureTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private int mProcess;
    private Callback mCallback;
    private List<Candidature> mItems;
    private Exception mException;
    private String mIdCandidate;

    public LoadCandidateCandidatureTask(Context context, int process, Callback callback, String idCandidate)
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

            mItems = PersistenceManager.getInstance().getCandidatures(mIdCandidate);

            if(mItems.size() == 0) {
                mItems = Service.loadCandidature(mContext, mIdCandidate);
                PersistenceManager.getInstance().saveCandidatures(mItems, mIdCandidate);
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
            mCallback.onPostExecute(mProcess, mItems, false, null);
        }
        else
        {
            mException.printStackTrace();
            mCallback.onPostExecute(mProcess, mItems, true, mException.getMessage());
        }
    }
}