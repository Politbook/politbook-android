package org.appeleicao2014.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.appeleicao2014.R;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.Util;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by thaleslima on 8/24/14.
 */
public class MenuAdapter extends BaseAdapter {
    private ArrayList<Item> mData = new ArrayList<Item>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public MenuAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Item item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final Item item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
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
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case Constants.TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.snippet_item1, null);
                    holder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
                    holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
                    holder.ivFlag = (ImageView) convertView.findViewById(R.id.ivFlag);
                    break;
                case Constants.TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.snippet_item2, null);
                    holder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);

                    convertView.setOnClickListener(null);
                    convertView.setOnLongClickListener(null);
                    convertView.setLongClickable(false);

                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = mData.get(position);

        if(rowType == Constants.TYPE_SEPARATOR)
        {
            holder.tvItem.setText(item.getDescription());
        }
        else
        {
            holder.tvItem.setText(item.getDescription());

            holder.ivImage.setImageResource(item.getIcon());
            if(mData.get(position).isLocale())
            {
                holder.ivFlag.setVisibility(View.VISIBLE);
                holder.ivFlag.setImageResource(Util.returnImageUf(mInflater.getContext()));
            }
            else
            {
                holder.ivFlag.setVisibility(View.GONE);
            }

        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView tvItem;
        public ImageView ivImage;
        public ImageView ivFlag;

    }
}
