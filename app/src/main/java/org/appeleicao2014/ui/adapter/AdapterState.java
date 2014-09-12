package org.appeleicao2014.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.model.State;
import org.appeleicao2014.util.ImageLoader;

import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class AdapterState extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<State> mItems;
    private String mUf;

    public AdapterState(Context context, List<State> items, String uf) {
        mItems = items;
        mInflater = LayoutInflater.from(context);
        mUf = uf;
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
            view = mInflater.inflate(R.layout.list_item_state, null);

            itemHolder = new ViewHolder();
            itemHolder.ivState = ((ImageView) view.findViewById(R.id.ivState));
            itemHolder.ivCheck = ((ImageView) view.findViewById(R.id.ivCheck));
            itemHolder.tvState = ((TextView) view.findViewById(R.id.tvState));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolder) view.getTag();
        }

        State item = mItems.get(i);
        itemHolder.ivState.setImageResource(item.getImage());
        itemHolder.tvState.setText(item.getDescription());

        if(mUf.equals(item.getUf()))
            itemHolder.ivCheck.setImageResource(R.drawable.check);
        else
            itemHolder.ivCheck.setImageResource(R.drawable.photo_transparent);

        return view;
    }

    class ViewHolder{
        public ImageView ivState;
        public ImageView ivCheck;
        public TextView tvState;
    }
}
