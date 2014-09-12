package org.appeleicao2014.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import org.appeleicao2014.R;
import org.appeleicao2014.util.Util;

/**
 * Created by thaleslima on 8/23/14.
 */
public class StateActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        if(!Util.getUfDefault(this).equals(""))
        {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        setTitle(getString(R.string.select_state));
    }
}
