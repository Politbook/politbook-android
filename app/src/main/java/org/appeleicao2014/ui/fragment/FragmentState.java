package org.appeleicao2014.ui.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.appeleicao2014.R;
import org.appeleicao2014.model.State;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.ui.activity.MainActivity;
import org.appeleicao2014.ui.adapter.AdapterState;
import org.appeleicao2014.util.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class FragmentState extends Fragment {
    public List<State> mStates;
    public AdapterState mAdapterState;
    private ListView mlvStates;

    public static String KEY_CHANGE_STATE = "change";
    public boolean changeState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_state, container, false);

        mlvStates = (ListView) rootView.findViewById(R.id.lvStates);

        loadData();
        initListener();

        return rootView;
    }

    private void loadData() {

        if(getArguments() != null) {
            changeState = getArguments().getBoolean(KEY_CHANGE_STATE);
        }

        initState();
        mAdapterState = new AdapterState(getActivity(), mStates, Util.getUfDefault(this.getActivity()));
        mlvStates.setAdapter(mAdapterState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initListener() {
        mlvStates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(Util.isNetworkAvailable(view.getContext())) {
                    Util.setUfDefault(view.getContext(), mStates.get(i).getUf());

                    PersistenceManager.getInstance().removeCandidates();

                    Util.setFilterParty(getActivity(), "1", 0);
                    Util.setFilterParty(getActivity(), "3", 0);
                    Util.setFilterParty(getActivity(), "5", 0);
                    Util.setFilterParty(getActivity(), "6", 0);
                    Util.setFilterParty(getActivity(), "7", 0);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else
                    Toast.makeText(view.getContext(), R.string.no_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initState()
    {
        mStates = new ArrayList<State>();
        mStates.add(new State("AC", "Acre", R.drawable.acre));
        mStates.add(new State("AL", "Alagoas", R.drawable.alagoas));
        mStates.add(new State("AM", "Amazonas", R.drawable.amazonas));
        mStates.add(new State("AP", "Amapa", R.drawable.amapa));
        mStates.add(new State("BA", "Bahia", R.drawable.bahia));
        mStates.add(new State("CE", "Ceará", R.drawable.ceara));
        mStates.add(new State("DF", "Distrito Federal", R.drawable.distritofederal));
        mStates.add(new State("ES", "Espírito Santo", R.drawable.espiritosanto));
        mStates.add(new State("GO", "Goiás", R.drawable.goias));
        mStates.add(new State("MA", "Maranhão", R.drawable.maranhao));
        mStates.add(new State("MG", "Minas Gerais", R.drawable.minhasgeral));
        mStates.add(new State("MS", "Mato Grosso do Sul", R.drawable.matogrossodosul));
        mStates.add(new State("MT", "Mato Grosso", R.drawable.matogrosso));
        mStates.add(new State("PA", "Pará", R.drawable.para));
        mStates.add(new State("PB", "Paraíba", R.drawable.paraiba));
        mStates.add(new State("PE", "Pernambuco", R.drawable.pernambuco));
        mStates.add(new State("PI", "Piauí", R.drawable.piaui));
        mStates.add(new State("PR", "Paraná", R.drawable.parana));
        mStates.add(new State("RJ", "Rio de Janeiro", R.drawable.riodejaneiro));
        mStates.add(new State("RN", "Rio Grande do Norte", R.drawable.riograndedonorte));
        mStates.add(new State("RO", "Rondônia", R.drawable.rondonia));
        mStates.add(new State("RR", "Roraima", R.drawable.roraima));
        mStates.add(new State("RS", "Rio Grande do Sul", R.drawable.riograndedosul));
        mStates.add(new State("SC", "Santa Catarina", R.drawable.santacatarina));
        mStates.add(new State("SE", "Sergipe", R.drawable.sergipe));
        mStates.add(new State("SP", "São Paulo", R.drawable.saopaulo));
        mStates.add(new State("TO", "Tocantins", R.drawable.tocantins));
    }
}
