package com.floyd.diamond.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.tools.DateUtil;
import com.floyd.diamond.biz.vo.mote.TaskItemVO;
import com.floyd.diamond.event.TaskEvent;
import com.floyd.diamond.ui.activity.TaskProcessActivity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by floyd on 15-12-11.
 */
public class MyTaskAdapter extends BaseAdapter {

    private static final String TAG = "MyTaskAdapter";
    private ImageLoader mImageLoader;
    private List<TaskItemVO> taskItems = new ArrayList<TaskItemVO>();
    private Context mContext;
    private static final int TIME_EVENT = 1;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case TIME_EVENT:
                    MsgObj o = (MsgObj) msg.obj;
                    long id = o.id;
                    SoftReference<TextView> view = o.timeView;
                    if (view == null && view.get() == null) {
                        return;
                    }

                    TextView timeView = view.get();
                    if (timeView == null) {
                        return;
                    }

                    TaskItemVO itemVO = (TaskItemVO) timeView.getTag(R.id.LEFT_TIME_ID);
                    if (itemVO == null || itemVO.id == 0l || id != itemVO.id) {
                        return;
                    }

                    if (itemVO.status != 1 || itemVO.isFinish()) {
                        return;
                    }

                    String dateLeft = DateUtil.getDateBefore(itemVO.createTime);
                    if (dateLeft == null && itemVO.createTime != 0l) {
                        timeView.setVisibility(View.GONE);
                        itemVO.status = 2;
                    } else {
                        timeView.setVisibility(View.VISIBLE);
                        timeView.setText("剩余时间:" + dateLeft);
                    }

                    Message newMsg = new Message();
                    newMsg.what = TIME_EVENT;
                    newMsg.obj = o;
                    mHandler.sendMessageDelayed(newMsg, 1000);
                    break;
            }
        }
    };

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

    public void updateStatus(TaskEvent taskEvent) {
        if (taskItems == null || taskItems.isEmpty()) {
            return;
        }

        boolean isChanged = false;
        for (TaskItemVO vo : taskItems) {
            if (vo.moteTaskId == taskEvent.moteTaskId) {
                vo.status = taskEvent.status;
                vo.finishStatus = taskEvent.finishStatus;
                isChanged = true;
                break;
            }
        }

        if (isChanged) {
            this.notifyDataSetChanged();
        }
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.my_task_layout, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.task_title);
            holder.taskPicView = (NetworkImageView) convertView.findViewById(R.id.task_pic);
            holder.taskPicView.setDefaultImageResId(R.drawable.tupian);
            holder.leftTimeView = (TextView) convertView.findViewById(R.id.left_time);
            holder.fillOrderView = (TextView) convertView.findViewById(R.id.fill_order);
            holder.uploadImageView = (TextView) convertView.findViewById(R.id.upload_pic);
            holder.orderInfo= ((RelativeLayout) convertView.findViewById(R.id.orderInfo));
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        final TaskItemVO taskItemVO = getItem(position);
        holder.taskPicView.setImageUrl(taskItemVO.getPreviewImageUrl(), mImageLoader);
        holder.titleView.setText(taskItemVO.title);

        int status = taskItemVO.status;
        int finishStatus = taskItemVO.finishStatus;
        Log.i(TAG, "status is" + status + "-----" + taskItemVO.title);
        holder.leftTimeView.setVisibility(View.GONE);
        holder.leftTimeView.setTag(R.id.LEFT_TIME_ID, taskItemVO);
        if (finishStatus == 1) {
            holder.fillOrderView.setText("任务结束");
        } else {
            if (status == 1) {
                String dateleft = DateUtil.getDateBefore(taskItemVO.createTime);
                if (dateleft == null && taskItemVO.createTime != 0l) {
                    taskItemVO.status = 2;
                } else {
                    holder.leftTimeView.setVisibility(View.VISIBLE);
                    holder.leftTimeView.setText("剩余时间:" + dateleft);
                    Message msg = new Message();
                    msg.what = TIME_EVENT;
                    MsgObj msgObj = new MsgObj();
                    msgObj.id = taskItemVO.id;
                    msgObj.timeView = new SoftReference<TextView>(holder.leftTimeView);
                    msg.obj = msgObj;
                    mHandler.sendMessage(msg);
                }

                holder.fillOrderView.setText("填订单号");
            } else if (status >=2 && status < 3) {
                holder.fillOrderView.setText("上传照片");
            }else if (status >=4 && status < 5 ) {
                holder.fillOrderView.setText("自购退回");
            } else if (status >=5 && status < 7 ) {
                holder.fillOrderView.setText("商家确认");
            } else if (status >= 7 || status == 0) {
                holder.fillOrderView.setText("任务结束");
            } else {
                holder.fillOrderView.setText("未知");
            }
        }
        holder.orderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, TaskProcessActivity.class);
                it.putExtra(TaskProcessActivity.MOTE_TASK_ID, taskItemVO.moteTaskId);
                mContext.startActivity(it);
            }
        });
        return convertView;
    }


    public static class ViewHolder {
        public NetworkImageView taskPicView;
        public TextView titleView;
        public TextView leftTimeView;
        public TextView uploadImageView;
        public TextView fillOrderView;
        public RelativeLayout orderInfo;
    }

    public static class MsgObj {
        public SoftReference<TextView> timeView;
        public long id;
    }
}
