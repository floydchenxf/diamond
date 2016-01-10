package com.floyd.diamond.notice.openapp;

import android.content.Context;
import android.content.Intent;

import com.floyd.diamond.notice.OpenAppIntent;
import com.floyd.diamond.notice.OpenAppVO;
import com.floyd.diamond.ui.activity.NewTaskActivity;

/**
 * Created by floyd on 16-1-10.
 */
public class OpenNewTask implements OpenAppIntent {
    private Context mContext;
    private OpenAppVO openAppVO;

    @Override
    public Intent createOpenApp() {
        Intent it = new Intent(mContext, NewTaskActivity.class);
        String o = openAppVO.params.get("taskId");
        if (o != null) {
            Long taskId = Long.parseLong(o);
            it.putExtra(NewTaskActivity.TASK_TYPE_ITEM_ID, taskId);
        }
        return it;
    }

    @Override
    public void setOpenAppVO(Context context, OpenAppVO openAppVO) {
        this.mContext = context;
        this.openAppVO = openAppVO;
    }
}
