package com.floyd.diamond.notice.openapp;

import android.content.Context;
import android.content.Intent;

import com.floyd.diamond.notice.OpenAppIntent;
import com.floyd.diamond.notice.OpenAppVO;
import com.floyd.diamond.ui.seller.process.SellerTaskProcessActivity;

/**
 * Created by floyd on 16-1-10.
 */
public class OpenTaskProcessor implements OpenAppIntent {


    private Context mContext;
    private OpenAppVO openAppVO;

    public OpenTaskProcessor() {

    }

    public void setOpenAppVO(Context context, OpenAppVO openAppVO) {
        this.mContext = context;
        this.openAppVO = openAppVO;
    }

    @Override
    public Intent createOpenApp() {
        Intent intent = new Intent(mContext, SellerTaskProcessActivity.class);
        String o = openAppVO.params.get("moteTaskId");
        if (o != null) {
            Long taskId = Long.parseLong(o);
            intent.putExtra(SellerTaskProcessActivity.SELLER_MOTE_TASK_ID, taskId);
        }
        return intent;
    }
}
