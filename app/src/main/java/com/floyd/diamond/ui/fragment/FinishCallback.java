package com.floyd.diamond.ui.fragment;

/**
 * Created by floyd on 15-12-15.
 */
public interface FinishCallback {

    public static final int TYPE_CHOOSE_PIC_EVENT = 1;
    public static final int TYPE_SELF_BUY_EVENT = 2;
    public static final int TYPE_RETURN_ITEM_EVENT = 3;
    void doFinish(int type);
}
