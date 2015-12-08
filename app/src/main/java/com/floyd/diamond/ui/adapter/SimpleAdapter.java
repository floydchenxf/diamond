package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.floyd.diamond.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 15-11-21.
 */
public class SimpleAdapter extends BaseAdapter {

    private List<String> mList = new ArrayList<String>();
    private LayoutInflater infalter;

    public SimpleAdapter(Context context, List<String> args) {
        infalter = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList.addAll(args);
    }

    public void add(String k) {
        mList.add(k);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = infalter.inflate(R.layout.simple_adapter, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.simple_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(getItem(position));
        return convertView;
    }

    public static class ViewHolder {

        private TextView textView;

    }

}
