package org.appeleicao2014.ui.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.ui.activity.CandidateActivity;
import org.appeleicao2014.ui.adapter.AdapterFavorite;
import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class FragmentFavorite extends Fragment{
    public List<Candidate> mCandidates;
    public AdapterFavorite mAdapter;
    private ListView mListCandidates;
    private TextView mtvFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        mListCandidates = (ListView) rootView.findViewById(R.id.lvCandidates);
        mtvFavorite = (TextView) rootView.findViewById(R.id.tvFavorite);

        loadData();
        initListener();

        return rootView;
    }

    private void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();

        mCandidates = PersistenceManager.getInstance().getFavorites();
        mAdapter = new AdapterFavorite(getActivity(), mCandidates);
        mListCandidates.setAdapter(mAdapter);

        if(mCandidates.size() == 0)
            mtvFavorite.setVisibility(View.VISIBLE);
    }

    private void initListener() {
        mListCandidates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), CandidateActivity.class);
                intent.putExtra(CandidateActivity.KEY_CANDIDATE, mCandidates.get(i));
                startActivity(intent);
            }
        });
    }
}
