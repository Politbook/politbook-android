package org.appeleicao2014.util;

/**
 * Created by thaleslima on 7/8/14.
 */
public interface Callback {
    void onPostExecute(int process, Object object, boolean error, String descriptionError);
    void onPreExecute(int process);
}
