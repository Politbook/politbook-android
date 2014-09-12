package org.appeleicao2014.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.util.ImageLoader;
import org.appeleicao2014.util.LoadBitmap;

import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class AdapterCandidate extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Candidate> mItems;
    private ImageLoader imgLoader;
    private LoadBitmap loadBitmap;

    public AdapterCandidate(Context context, List<Candidate> candidates) {
        mItems = candidates;
        mInflater = LayoutInflater.from(context);

        imgLoader = new ImageLoader(context, R.drawable.photo);
        loadBitmap = new LoadBitmap(context);
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
            view = mInflater.inflate(R.layout.list_item_candidate, null);

            itemHolder = new ViewHolder();
            itemHolder.tvNickname = ((TextView) view.findViewById(R.id.tvNickname));
            itemHolder.tvParty = ((TextView) view.findViewById(R.id.tvParty));
            itemHolder.ivPhoto = ((ImageView) view.findViewById(R.id.ivPhoto));
            itemHolder.rbRating = ((RatingBar) view.findViewById(R.id.rbRating));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolder) view.getTag();
        }

        Candidate item = mItems.get(i);
        itemHolder.tvNickname.setText(item.getNickname());
        itemHolder.tvParty.setText(item.getNumber() + " | " +item.getParty());
        itemHolder.ivPhoto.setImageResource(R.drawable.photo);
        itemHolder.rbRating.setRating(item.getAverage());

        LayerDrawable stars = (LayerDrawable) itemHolder.rbRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(mInflater.getContext().getResources().getColor(R.color.star), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(mInflater.getContext().getResources().getColor(R.color.star2), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(mInflater.getContext().getResources().getColor(R.color.star2), PorterDuff.Mode.SRC_ATOP);

        //loadBitmap.loadBitmap(item.getPhoto(), itemHolder.ivPhoto);
        imgLoader.DisplayImage(item.getPhoto(), itemHolder.ivPhoto);

        return view;
    }

    class ViewHolder{
        public TextView tvNickname;
        public ImageView ivPhoto;
        public TextView tvParty;
        public RatingBar rbRating;

    }
}
