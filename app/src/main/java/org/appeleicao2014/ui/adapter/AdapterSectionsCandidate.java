package org.appeleicao2014.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.appeleicao2014.R;
import org.appeleicao2014.ui.fragment.FragmentCandidateAbout;
import org.appeleicao2014.ui.fragment.FragmentCandidateCandidature;
import org.appeleicao2014.ui.fragment.FragmentCandidateEquity;
import org.appeleicao2014.ui.fragment.FragmentCandidateStatistics;
import org.appeleicao2014.ui.fragment.FragmentComment;
import org.appeleicao2014.util.Constants;

/**
 * Created by thaleslima on 7/8/14.
 */
public class AdapterSectionsCandidate extends FragmentPagerAdapter {
    private Context mContext;
    private String mIdCandidate;
    private String mJobTitle;
    private String mState;
    private String mNameCandidate;
    private boolean hideRating;

    public AdapterSectionsCandidate(FragmentManager fm, Context context, String idCandidate, String jobTitle, String state, String nameCandidate) {
        super(fm);
        mContext = context;
        mIdCandidate = idCandidate;
        mJobTitle = jobTitle;
        mState = state;
        mNameCandidate = nameCandidate;

        if(jobTitle != null && (jobTitle.equals(Constants.DEPUTYSTATE) || jobTitle.equals(Constants.DEPUTYFEDERAL)))
        {
            hideRating = true;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle;

        if(hideRating)
        {
            switch (position) {
                case 0:
                    bundle = new Bundle();
                    bundle.putString(FragmentCandidateAbout.KEY_ID_CANDIDATE, mIdCandidate);
                    bundle.putString(FragmentCandidateAbout.KEY_JOB, mJobTitle);
                    fragment = new FragmentCandidateAbout();
                    fragment.setArguments(bundle);
                    break;

                case 1:
                    bundle = new Bundle();
                    bundle.putString(FragmentCandidateEquity.KEY_ID_CANDIDATE, mIdCandidate);
                    fragment = new FragmentCandidateEquity();
                    fragment.setArguments(bundle);
                    break;

                case 2:
                    bundle = new Bundle();
                    bundle.putString(FragmentCandidateStatistics.KEY_ID_CANDIDATE, mIdCandidate);
                    fragment = new FragmentCandidateCandidature();
                    fragment.setArguments(bundle);
                    break;

                case 3:
                    bundle = new Bundle();
                    bundle.putString(FragmentCandidateStatistics.KEY_ID_CANDIDATE, mIdCandidate);
                    fragment = new FragmentCandidateStatistics();
                    fragment.setArguments(bundle);
                    break;
            }
        }

        else
        {
            switch (position) {
                case 0:
                    bundle = new Bundle();
                    bundle.putString(FragmentCandidateAbout.KEY_ID_CANDIDATE, mIdCandidate);
                    bundle.putString(FragmentCandidateAbout.KEY_JOB, mJobTitle);
                    fragment = new FragmentCandidateAbout();
                    fragment.setArguments(bundle);
                    break;

                case 1:
                    bundle = new Bundle();
                    bundle.putString(FragmentComment.KEY_ID_CAN, mIdCandidate);
                    bundle.putString(FragmentComment.KEY_TITLE_JOB_CAN, mJobTitle);
                    bundle.putString(FragmentComment.KEY_STATE_CAN, mState);
                    bundle.putString(FragmentComment.KEY_NAME_CAN, mNameCandidate);
                    fragment = new FragmentComment();
                    fragment.setArguments(bundle);
                    break;

                case 2:
                    bundle = new Bundle();
                    bundle.putString(FragmentCandidateEquity.KEY_ID_CANDIDATE, mIdCandidate);
                    fragment = new FragmentCandidateEquity();
                    fragment.setArguments(bundle);
                    break;

                case 3:
                    bundle = new Bundle();
                    bundle.putString(FragmentCandidateStatistics.KEY_ID_CANDIDATE, mIdCandidate);
                    fragment = new FragmentCandidateCandidature();
                    fragment.setArguments(bundle);
                    break;

                case 4:
                    bundle = new Bundle();
                    bundle.putString(FragmentCandidateStatistics.KEY_ID_CANDIDATE, mIdCandidate);
                    fragment = new FragmentCandidateStatistics();
                    fragment.setArguments(bundle);
                    break;
            }
        }


        return fragment;
    }

    @Override
    public int getCount() {
        if(hideRating)
        {
            return 4;
        }

        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (hideRating) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.title_data_personal);
                case 1:
                    return mContext.getString(R.string.title_equity);
                case 2:
                    return mContext.getString(R.string.title_candidature);
                case 3:
                    return mContext.getString(R.string.title_statistics);
            }
        } else {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.title_data_personal);
                case 1:
                    return mContext.getString(R.string.title_rating_candidate);
                case 2:
                    return mContext.getString(R.string.title_equity);
                case 3:
                    return mContext.getString(R.string.title_candidature);
                case 4:
                    return mContext.getString(R.string.title_statistics);

            }
        }
        return null;
    }
}