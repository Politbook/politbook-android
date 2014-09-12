package org.appeleicao2014.task;

import android.content.Context;
import android.os.AsyncTask;

import org.appeleicao2014.model.CommentsUser;
import org.appeleicao2014.service.Service;
import org.appeleicao2014.util.Callback;


/**
 * Created by thaleslima on 7/8/14.
 */
public class DeleteCommentsTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private int mProcess;
    private Callback mCallback;
    private CommentsUser mComments;
    private Exception mException;
    private String mIdCandidate;
    private int mIdUser;

    public DeleteCommentsTask(Context context, int process, Callback callback, String idCandidate, int idUser)
    {
        mContext = context;
        mProcess = process;
        mCallback = callback;
        mIdCandidate = idCandidate;
        mIdUser = idUser;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.onPreExecute(mProcess);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {

            mComments = Service.deleteComment(mContext, mIdCandidate, mIdUser);

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