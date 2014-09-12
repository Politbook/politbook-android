package org.appeleicao2014.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import org.appeleicao2014.R;
import org.appeleicao2014.ui.fragment.FragmentCandidates;
import org.appeleicao2014.util.Constants;

/**
 * Created by thaleslima on 7/8/14.
 */
public class AdapterSectionsCandidates extends FragmentPagerAdapter {
    private Context mContext;
    public AdapterSectionsCandidates(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();


        switch (position) {
            case 0:
                bundle.putString(FragmentCandidates.KEY_JOB, Constants.PRESIDENT);
                fragment = new FragmentCandidates();
                fragment.setArguments(bundle);
                break;
            case 1:
                bundle.putString(FragmentCandidates.KEY_JOB, Constants.GOVERNOR);
                fragment = new FragmentCandidates();
                fragment.setArguments(bundle);
                break;
            case 2:
                bundle.putString(FragmentCandidates.KEY_JOB, Constants.SENATOR);
                fragment = new FragmentCandidates();
                fragment.setArguments(bundle);
                break;
            case 3:
                bundle.putString(FragmentCandidates.KEY_JOB, Constants.DEPUTYFEDERAL);
                fragment = new FragmentCandidates();
                fragment.setArguments(bundle);
                break;
            case 4:
                bundle.putString(FragmentCandidates.KEY_JOB, Constants.DEPUTYSTATE);
                fragment = new FragmentCandidates();
                fragment.setArguments(bundle);
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_president);
            case 1:
                return mContext.getString(R.string.title_governor);
            case 2:
                return mContext.getString(R.string.title_senator);
            case 3:
                return mContext.getString(R.string.title_deputy_federal);
            case 4:
                return mContext.getString(R.string.title_deputy_state);
        }
        return null;
    }
}