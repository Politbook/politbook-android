package org.appeleicao2014.task;

import android.content.Context;
import android.os.AsyncTask;

import org.appeleicao2014.model.Comment;
import org.appeleicao2014.model.User;
import org.appeleicao2014.service.Service;
import org.appeleicao2014.session.Session;
import org.appeleicao2014.util.Callback;


/**
 * Created by thaleslima on 7/8/14.
 */
public class SaveUserTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private int mProcess;
    private Callback mCallback;
    private Exception mException;
    private User mUser;

    public SaveUserTask(Context context, int process, Callback callback, User user)
    {
        mContext = context;
        mProcess = process;
        mCallback = callback;
        mUser = user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.onPreExecute(mProcess);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {

            if(Session.getInstance().getCurrentUser() == null)
                mUser = Service.saveUser(mContext, mUser);
            else
                mUser = null;

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
            mCallback.onPostExecute(mProcess, mUser, false, null);
        }
        else
        {
            mException.printStackTrace();
            mCallback.onPostExecute(mProcess, null, true, mException.getMessage());
        }
    }
}