package com.floyd.diamond.biz.vo.seller;

/**
 * Created by floyd on 15-12-26.
 */
public class SellerTaskDetailVO {

    public long id;
    public String avartUrl;
    public String title;
    public String nickname;
    public String expressNo;
    public int status;//状态 0关注 1接单 2淘宝下单 3 完成收货并晒图4上传图片 5自购商品 6退还商品 7存在争议，申请客户 8确认退还商品
    public int finishStatus;
    public int uploadPicNum;
}