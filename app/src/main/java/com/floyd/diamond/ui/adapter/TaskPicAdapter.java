package com.floyd.diamond.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.vo.TaskPicsVO;
import com.floyd.diamond.ui.view.GalleryItem;

import java.util.List;

/**
 * Created by floyd on 15-12-6.
 */
public class TaskPicAdapter extends BaseAdapter {

    private Activity mContext;
    private ImageLoader imageLoader;
    private List<TaskPicsVO> taskPicsVOList;
    private TaskPicItemClick itemClick;



    public TaskPicAdapter(Activity context, List<TaskPicsVO> taskPicsVOs, ImageLoader imageLoader, TaskPicItemClick click) {
        this.mContext = context;
        this.taskPicsVOList = taskPicsVOs;
        this.imageLoader = imageLoader;
        this.itemClick = click;
    }

    @Override
    public int getCount() {
        return taskPicsVOList.size();
    }

    @Override
    public TaskPicsVO getItem(int position) {
        return taskPicsVOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.task_pic_adapter, null);
            holder=new ViewHolder();
            holder.dateTimeView = (TextView)convertView.findViewById(R.id.date_time);
            holder.galleryItem = (GalleryItem) convertView.findViewById(R.id.galley_item);
            convertView.setTag(holder);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick != null) {
                    itemClick.onItemClick(position, v);
                }
            }
        });

        holder = (ViewHolder)convertView.getTag();
        TaskPicsVO taskPicsVO = getItem(position);
        holder.galleryItem.initAdapter(mContext, imageLoader);
        holder.galleryItem.arraylist = taskPicsVO.taskPics;
        holder.galleryItem.setAdapter(holder.galleryItem.adapter);
        holder.dateTimeView.setText(taskPicsVO.dateTime);
        return convertView;
    }

    static class ViewHolder {
        public GalleryItem galleryItem;
        public TextView dateTimeView;
    }

    public interface TaskPicItemClick {
        void onItemClick(int position, View v);
    }
}
