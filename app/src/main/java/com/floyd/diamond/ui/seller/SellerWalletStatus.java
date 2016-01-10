package com.floyd.diamond.ui.seller;

/**
 * Created by floyd on 16-1-10.
 */
public enum SellerWalletStatus {

    ALL(0, "全部"), PUBLISH_TASK(1, "放单"), SELF_BUY(2, "待确认"), DRAW_MONEY(3, "提现"), ADD_CASH(4, "充值");

    public int code;
    public String name;

    SellerWalletStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
