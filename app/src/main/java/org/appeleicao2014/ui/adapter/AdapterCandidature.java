package org.appeleicao2014.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidature;
import org.appeleicao2014.model.Equity;
import org.appeleicao2014.util.Util;

import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class AdapterCandidature extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Candidature> mItems;

    public AdapterCandidature(Context context, List<Candidature> items) {
        mItems = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder itemHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_candidature, null);

            itemHolder = new ViewHolder();
            itemHolder.tvElectionYear = ((TextView) view.findViewById(R.id.tvElectionYear));
            itemHolder.tvJobTitle = ((TextView) view.findViewById(R.id.tvJobTitle));
            itemHolder.tvParty = ((TextView) view.findViewById(R.id.tvParty));
            itemHolder.tvCity = ((TextView) view.findViewById(R.id.tvCity));
            itemHolder.tvState = ((TextView) view.findViewById(R.id.tvState));
            itemHolder.tvResult = ((TextView) view.findViewById(R.id.tvResult));
            itemHolder.tvVotes = ((TextView) view.findViewById(R.id.tvVotes));
            itemHolder.tvFinancialResources = ((TextView) view.findViewById(R.id.tvFinancialResources));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolder) view.getTag();
        }

        Candidature item = mItems.get(i);
        itemHolder.tvElectionYear.setText(Util.valueString(item.getElectionYear()));
        itemHolder.tvJobTitle.setText(Util.valueString(item.getJobTitle()));
        itemHolder.tvParty.setText(mInflater.getContext().getText(R.string.party) + " " + Util.valueString(item.getParty()));
        itemHolder.tvCity.setText(mInflater.getContext().getText(R.string.city) + " " + Util.valueString(item.getCity()));
        itemHolder.tvState.setText(mInflater.getContext().getText(R.string.state) + " " + Util.valueString(item.getState()));
        itemHolder.tvResult.setText(mInflater.getContext().getText(R.string.result) + " " + Util.valueString(item.getResult()));
        itemHolder.tvVotes.setText(mInflater.getContext().getText(R.string.votes) + " " + Util.formatNumber(Util.valueNumber(item.getVotes())));
        itemHolder.tvFinancialResources.setText(mInflater.getContext().getText(R.string.financialResources)
                + " " + Util.formatPrice(Util.valueNumber(item.getFinancialResources())));

        return view;
    }

    class ViewHolder{
        public TextView tvElectionYear;
        public TextView tvJobTitle;
        public TextView tvParty;
        public TextView tvCity;
        public TextView tvState;
        public TextView tvResult;
        public TextView tvVotes;
        public TextView tvFinancialResources;
    }
}
