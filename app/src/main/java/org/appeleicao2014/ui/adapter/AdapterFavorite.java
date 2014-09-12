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
import org.appeleicao2014.util.ImageLoader;
import org.appeleicao2014.util.Util;

import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class AdapterFavorite extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Candidate> mItems;
    private ImageLoader imgLoader;

    public AdapterFavorite(Context context, List<Candidate> candidates) {
        mItems = candidates;
        mInflater = LayoutInflater.from(context);

        imgLoader = new ImageLoader(context, R.drawable.photo);
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
            view = mInflater.inflate(R.layout.list_item_favorite, null);

            itemHolder = new ViewHolder();
            itemHolder.tvNickname = ((TextView) view.findViewById(R.id.tvNickname));
            itemHolder.tvParty = ((TextView) view.findViewById(R.id.tvParty));
            itemHolder.ivPhoto = ((ImageView) view.findViewById(R.id.ivPhoto));
            itemHolder.tvJobTitle = ((TextView) view.findViewById(R.id.tvJobTitle));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolder) view.getTag();
        }

        Candidate item = mItems.get(i);
        itemHolder.tvNickname.setText(item.getNickname());
        itemHolder.tvParty.setText(item.getNumber() + " | " +item.getParty());
        itemHolder.tvJobTitle.setText(Util.returnJobTitle(mInflater.getContext(), item.getIdJobTitle()) + " " + item.getState());

        itemHolder.ivPhoto.setImageResource(R.drawable.photo);
        imgLoader.DisplayImage(item.getPhoto(), itemHolder.ivPhoto);

        return view;
    }

    class ViewHolder{
        public TextView tvNickname;
        public ImageView ivPhoto;
        public TextView tvParty;
        public TextView tvJobTitle;
    }
}
