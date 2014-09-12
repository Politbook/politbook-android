package org.appeleicao2014.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.appeleicao2014.R;
import org.appeleicao2014.model.Comment;
import org.appeleicao2014.util.Util;

import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class AdapterComment extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Comment> mItems;

    public AdapterComment(Context context, List<Comment> items) {
        mItems = items;

        try {
            mInflater = LayoutInflater.from(context);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
            view = mInflater.inflate(R.layout.list_item_comment, null);

            itemHolder = new ViewHolder();
            itemHolder.ivSocial = ((com.facebook.widget.ProfilePictureView) view.findViewById(R.id.ivSocial));
            itemHolder.tvNameUser = ((TextView) view.findViewById(R.id.tvNameUser));
            itemHolder.ratingBar = ((RatingBar) view.findViewById(R.id.rbRating));
            itemHolder.tvComment = ((TextView) view.findViewById(R.id.tvComment));
            itemHolder.tvDate = ((TextView) view.findViewById(R.id.tvDate));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolder) view.getTag();
        }

        Comment item = mItems.get(i);

        itemHolder.tvNameUser.setText(Util.valueString(item.getNameUser()));
        itemHolder.ratingBar.setRating(item.getRating());
        itemHolder.tvDate.setText(item.getDate());
        itemHolder.tvComment.setText(item.getComment());


        final String idSocial = item.getIdSocial();
        final com.facebook.widget.ProfilePictureView ivSocial = itemHolder.ivSocial;

        Handler handler = new Handler();
        Runnable r = new Runnable(){
            public void run() {
                if(!idSocial.isEmpty())
                    ivSocial.setProfileId(idSocial);
                else
                    ivSocial.setProfileId(null);
            }
        };
        handler.post(r);
        return view;
    }

    class ViewHolder{
        public com.facebook.widget.ProfilePictureView ivSocial;
        public TextView tvNameUser;
        public RatingBar ratingBar;
        public TextView tvComment;
        public TextView tvDate;
    }
}
