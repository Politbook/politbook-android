package org.appeleicao2014.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.appeleicao2014.model.User;
import org.appeleicao2014.ui.AppApplication;
import org.appeleicao2014.util.Constants;

/**
 * Created by thaleslima on 9/7/14.
 */
public class Session {
    private User user;

    private static Session instance;

    public static Session getInstance()
    {
        if(instance == null)
        {
            instance = new Session();
        }

        return instance;
    }

    public User getCurrentUser() {

        try {
            if (user == null) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AppApplication.getContext());
                int id = sharedPrefs.getInt(Constants.KEY_USER_ID, 0);
                String userName = sharedPrefs.getString(Constants.KEY_USER_NAME, "");
                String userEmail = sharedPrefs.getString(Constants.KEY_USER_EMAIL, "");
                String idSocial = sharedPrefs.getString(Constants.KEY_USER_ID_SOCIAL, "");

                if (id > 0) {
                    user = new User();
                    user.setId(id);
                    user.setName(userName);
                    user.setEmail(userEmail);
                    user.setPhoto("");
                    user.setIdSocial(idSocial);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public void setCurrentUser(User user) {
        try
        {
            if(user != null) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AppApplication.getContext());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt(Constants.KEY_USER_ID, user.getId());
                editor.putString(Constants.KEY_USER_NAME, user.getName());
                editor.putString(Constants.KEY_USER_EMAIL, user.getEmail());
                editor.putString(Constants.KEY_USER_ID_SOCIAL, user.getIdSocial());

                editor.commit();
            }
            else
            {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AppApplication.getContext());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt(Constants.KEY_USER_ID, 0);

                editor.commit();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.user = user;
    }
}
