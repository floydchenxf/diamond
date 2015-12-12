package com.floyd.diamond.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.tools.DateUtil;
import com.floyd.diamond.biz.vo.MoteTaskPicVO;
import com.floyd.diamond.biz.vo.TaskPicsVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 15-12-6.
 */
public class TaskPicAdapter extends BaseAdapter {

    private Activity mContext;
    private ImageLoader imageLoader;
    private List<TaskPicsVO> taskPicsVOList = new ArrayList<TaskPicsVO>();
    private TaskPicItemClick itemClick;


    public TaskPicAdapter(Activity context, ImageLoader imageLoader, TaskPicItemClick click) {
        this.mContext = context;
        this.imageLoader = imageLoader;
        this.itemClick = click;
    }

    public void addAll(List<TaskPicsVO> pics, boolean clear) {
        if (clear) {
            this.taskPicsVOList.clear();
        }
        this.taskPicsVOList.addAll(pics);
        this.notifyDataSetChanged();
    }

    public List<TaskPicsVO> getData() {
        return this.taskPicsVOList;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.task_pic_item, null);
            holder = new ViewHolder();
            holder.dateTimeView = (TextView) convertView.findViewById(R.id.date_time);
            holder.imageLayout = (LinearLayout) convertView.findViewById(R.id.galley_item);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        TaskPicsVO taskPicsVO = getItem(position);
        holder.imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick != null) {
                    itemClick.onItemClick(position, v);
                }
            }
        });
        holder.imageLayout.removeAllViews();
        List<MoteTaskPicVO> pics = taskPicsVO.taskPics;
        if (pics != null && !pics.isEmpty()) {
            int Width = mContext.getWindowManager().getDefaultDisplay().getWidth();
            float ondp = mContext.getResources().getDimension(R.dimen.one_dp);
            float eachWidth = (Width - 16 * ondp) / 3;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) eachWidth, (int) (100 * ondp));
            lp.setMargins((int) (2 * ondp), 0, (int) (2 * ondp), 0);
            for (MoteTaskPicVO vo : pics) {
                NetworkImageView imageView = new NetworkImageView(mContext);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageUrl(vo.getPreviewImageUrl(), imageLoader);
                holder.imageLayout.addView(imageView);
            }
        }

        String dateBefore = DateUtil.getDayBefore(taskPicsVO.dateTime);
        holder.dateTimeView.setText(dateBefore);
        return convertView;
    }

    static class ViewHolder {
        public LinearLayout imageLayout;
        public TextView dateTimeView;
    }

    public interface TaskPicItemClick {
        void onItemClick(int position, View v);
    }
}
