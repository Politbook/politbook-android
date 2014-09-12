package org.appeleicao2014.ui.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.common.view.SlidingTabLayout;
import org.appeleicao2014.R;
import org.appeleicao2014.ui.adapter.AdapterSectionsCandidates;

/**
 * Created by thaleslima on 8/23/14.
 */
public class FragmentMainCandidates extends Fragment {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_candidades, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mSlidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);

        loadData();
        initListener();

        return rootView;
    }

    private void loadData()
    {
        getActivity().setTitle(R.string.title_candidate);

        AdapterSectionsCandidates sectionsCandidates = new AdapterSectionsCandidates(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(sectionsCandidates);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.color_indicator);
            }

            @Override
            public int getDividerColor(int position) {
                return getResources().getColor(R.color.color_indicator);
            }
        });
    }

    private void initListener()
    {

    }
}
