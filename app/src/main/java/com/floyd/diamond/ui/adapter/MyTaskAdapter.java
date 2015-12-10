package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.floyd.diamond.biz.vo.TaskItemVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 15-12-11.
 */
public class MyTaskAdapter extends BaseAdapter {

    private ImageLoader mImageLoader;
    private List<TaskItemVO> taskItems = new ArrayList<TaskItemVO>();
    private Context mContext;

    public MyTaskAdapter(Context context, ImageLoader mImageLoader) {
        this.mContext = context;
        this.mImageLoader = mImageLoader;
    }

    public void addAll(List<TaskItemVO> tasks, boolean isClear) {
        if (isClear) {
            taskItems.clear();
        }
        taskItems.addAll(tasks);
        this.notifyDataSetChanged();
    }

    public List<TaskItemVO> getData() {
        return this.taskItems;
    }


    @Override
    public int getCount() {
        return taskItems.size();
    }

    @Override
    public TaskItemVO getItem(int position) {
        return taskItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
