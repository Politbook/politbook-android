package org.appeleicao2014.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.appeleicao2014.R;
import org.appeleicao2014.util.Util;

public class AdapterMenuOptions extends BaseAdapter implements AdapterView.OnItemClickListener {
    LayoutInflater mInflater;
    String[] mArrayItems;


    public AdapterMenuOptions(Context c, String[] lstItems){

        mInflater = (LayoutInflater.from(c));
        mArrayItems = lstItems;
    }

    @Override
    public int getCount() {
        return mArrayItems.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vHolder;

        if (view == null){
            view = mInflater.inflate(R.layout.list_item_left_menu,null);
            vHolder = new ViewHolder();
            vHolder.twItem = (TextView) view.findViewById(R.id.tvItem);
            vHolder.iwImage = (ImageView) view.findViewById(R.id.ivImage);
            vHolder.iwFlag = (ImageView) view.findViewById(R.id.iwFlag);
            view.setTag(vHolder);
        }
        else
        {
            vHolder = (ViewHolder) view.getTag();
        }

       vHolder.twItem.setText(mArrayItems[i]);
        switch (i){
            case 0:
                vHolder.iwImage.setImageResource(R.drawable.ic_candidates);
                vHolder.iwFlag.setVisibility(View.GONE);
                break;
            case 1:
                vHolder.iwImage.setImageResource(R.drawable.ic_favorite);
                vHolder.iwFlag.setVisibility(View.GONE);
                break;
            case 2:
                vHolder.iwImage.setImageResource(R.drawable.ic_location);
                vHolder.iwFlag.setVisibility(View.VISIBLE);
                vHolder.iwFlag.setImageResource(Util.returnImageUf(mInflater.getContext()));
                break;
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    class ViewHolder{
        public TextView twItem;
        public ImageView iwImage;
        public ImageView iwFlag;
    }

}

