package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.biz.vo.AreaVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 15-12-20.
 */
public class AreaAdapter extends BaseAdapter {

    private List<AreaVO> areaList = new ArrayList<AreaVO>();
    private Context mContext;

    public AreaAdapter(Context context) {
        this.mContext = context;
    }

    public List<AreaVO> getAreaList() {
        return this.areaList;
    }

    public void addAll(List<AreaVO> areas, boolean clear) {
        if (clear) {
            this.areaList.clear();
        }
        this.areaList.addAll(areas);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return areaList.size();
    }

    @Override
    public AreaVO getItem(int position) {
        return areaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.activity_area_item, null);
            holder = new ViewHolder();
            holder.areaView = (TextView) convertView.findViewById(R.id.area_view);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        AreaVO areaVO = getItem(position);
        holder.areaView.setText(areaVO.name);
        return convertView;
    }

    public static class ViewHolder {
        public TextView areaView;
    }
}
