package org.appeleicao2014.task;

import android.content.Context;
import android.os.AsyncTask;

import org.appeleicao2014.model.Comment;
import org.appeleicao2014.model.CommentsUser;
import org.appeleicao2014.service.Service;
import org.appeleicao2014.util.Callback;

import java.util.List;


/**
 * Created by thaleslima on 7/8/14.
 */
public class SaveCommentTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private int mProcess;
    private Callback mCallback;
    private Exception mException;
    private Comment mComment;
    private CommentsUser mCommentsUser;

    public SaveCommentTask(Context context, int process, Callback callback, Comment comment)
    {
        mContext = context;
        mProcess = process;
        mCallback = callback;
        mComment = comment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.onPreExecute(mProcess);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {

            mCommentsUser = Service.saveComment(mContext, mComment);

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
            mCallback.onPostExecute(mProcess, mCommentsUser, false, null);
        }
        else
        {
            mException.printStackTrace();
            mCallback.onPostExecute(mProcess, null, true, mException.getMessage());
        }
    }
}