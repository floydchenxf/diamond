package com.floyd.diamond.notice;

import android.content.Context;
import android.content.Intent;

/**
 * Created by floyd on 16-1-10.
 */
public interface OpenAppIntent {

    Intent createOpenApp();

    void setOpenAppVO(Context context, OpenAppVO openAppVO);
}
