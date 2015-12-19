package com.floyd.diamond.biz.vo.process;

import java.io.Serializable;

/**
 * Created by floyd on 15-12-13
 * <p/>
 * <p/>
 * <p/>
 * {"data":{"isFollow":false,"moteTask":
 * <p/>
 */
public class ProcessMoteTaskVO implements Serializable {
    public long id;
    public long userId;
    public long taskId;
    public long createTime;
    public long updateTime;
    public String orderNo; //订单号
    public String expressCompanyId;
    public String expressNo;
    public int selfBuyFee;
    public int status;//状态 0关注 1接单 2淘宝下单 3 完成收货并晒图4上传图片 5自购商品 6退还商品 7存在争议，申请客户 8确认退还商品
    public int finishStatus;
    public long acceptedTime;//接单时间
    public long orderNoTime;//填写订单号时间
    public long showPicTime;//
    public long uploadPicTime;//上传图片时间
    public long returnItemTime; //退货时间
    public long finishStatusTime;//结束时间
    public long selfBuyTime;//自购时间
}
