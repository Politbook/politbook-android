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
import org.appeleicao2014.model.Equity;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by thaleslima on 8/20/14.
 */
public class AdapterEquity2 extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Equity> mItems;
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    public AdapterEquity2(Context context, List<Equity> items) {
        mItems = items;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AdapterEquity2(Context context) {
        mItems = new ArrayList<Equity>();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Equity item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final Equity item) {
        mItems.add(item);
        sectionHeader.add(mItems.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? Constants.TYPE_SEPARATOR : Constants.TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
        int rowType = getItemViewType(i);
        if (view == null) {
            itemHolder = new ViewHolder();
            switch (rowType) {
                case Constants.TYPE_SEPARATOR:
                    view = mInflater.inflate(R.layout.list_item_equity_separator, null);
                    itemHolder.tvYear = ((TextView) view.findViewById(R.id.tvYear));

                    view.setOnClickListener(null);
                    view.setOnLongClickListener(null);
                    view.setLongClickable(false);

                    break;
                case Constants.TYPE_ITEM:
                    view = mInflater.inflate(R.layout.list_item_equity, null);
                    itemHolder.tvEquity = ((TextView) view.findViewById(R.id.tvEquity));
                    itemHolder.tvAmount = ((TextView) view.findViewById(R.id.tvAmount));
                    itemHolder.tvYear = ((TextView) view.findViewById(R.id.tvYear));
                    itemHolder.rlItem = ((RelativeLayout) view.findViewById(R.id.rlItem));
                    break;
            }

            view.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolder) view.getTag();
        }

        Equity item = mItems.get(i);

        if(rowType == Constants.TYPE_SEPARATOR)
        {
            itemHolder.tvYear.setText(item.getYear());
            itemHolder.tvYear.setTextColor(item.getColor());
        }
        else
        {
            itemHolder.tvEquity.setText(Util.valueString(item.getEquity()));
            itemHolder.tvAmount.setText(Util.formatPrice(Util.valueNumber(item.getAmount())));
            itemHolder.tvYear.setText("");
        }
        return view;
    }

    class ViewHolder{
        public TextView tvEquity;
        public TextView tvAmount;
        public TextView tvYear;
        public RelativeLayout rlItem;

    }
}
