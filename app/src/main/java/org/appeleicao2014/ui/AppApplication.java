package org.appeleicao2014.ui;

import android.app.Application;
import android.content.Context;

/**
 * Created by thaleslima on 7/9/14.
 */
public class AppApplication extends Application {
    private static Context _context;

    @Override
    public void onCreate() {
        super.onCreate();
        _context = this.getBaseContext();
    }

    public static Context getContext() {
        return _context;
    }

}
