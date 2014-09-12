package org.appeleicao2014.task;

import android.content.Context;
import android.os.AsyncTask;

import org.appeleicao2014.model.Comment;
import org.appeleicao2014.model.Equity;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.service.Service;
import org.appeleicao2014.util.Callback;

import java.util.List;


/**
 * Created by thaleslima on 7/8/14.
 */
public class LoadCommentsTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private int mProcess;
    private Callback mCallback;
    private List<Comment> mComments;
    private Exception mException;
    private String mIdCandidate;
    private int mPage;

    public LoadCommentsTask(Context context, int process, Callback callback, String idCandidate, int page)
    {
        mContext = context;
        mProcess = process;
        mCallback = callback;
        mIdCandidate = idCandidate;
        mPage = page;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.onPreExecute(mProcess);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {

            mComments = Service.loadComments(mContext, mIdCandidate, mPage);

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
            mCallback.onPostExecute(mProcess, mComments, false, null);
        }
        else
        {
            mException.printStackTrace();
            mCallback.onPostExecute(mProcess, mComments, true, mException.getMessage());
        }
    }
}