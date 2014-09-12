package org.appeleicao2014.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.model.Equity;
import org.appeleicao2014.util.Util;

import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class AdapterEquity extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Equity> mItems;

    public AdapterEquity(Context context, List<Equity> items) {
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
            view = mInflater.inflate(R.layout.list_item_equity, null);

            itemHolder = new ViewHolder();
            itemHolder.tvEquity = ((TextView) view.findViewById(R.id.tvEquity));
            itemHolder.tvAmount = ((TextView) view.findViewById(R.id.tvAmount));
            itemHolder.tvYear = ((TextView) view.findViewById(R.id.tvYear));
            itemHolder.rlItem = ((RelativeLayout) view.findViewById(R.id.rlItem));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolder) view.getTag();
        }

        Equity item = mItems.get(i);
        itemHolder.tvEquity.setText(Util.valueString(item.getEquity()));
        itemHolder.tvAmount.setText(Util.formatPrice(Util.valueNumber(item.getAmount())));
        itemHolder.tvYear.setText(Util.valueString(item.getYear()));

        if(item.getColor() != 0)
            itemHolder.tvYear.setTextColor(item.getColor());

        return view;
    }

    class ViewHolder{
        public TextView tvEquity;
        public TextView tvAmount;
        public TextView tvYear;
        public RelativeLayout rlItem;

    }
}
