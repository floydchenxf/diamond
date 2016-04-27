package com.floyd.diamond.biz.vo.mote;

import com.floyd.diamond.utils.CommonUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by floyd on 15-12-9.
 */
public class TaskItemVO implements Serializable{
    public long id;//任务id
    public long userId;
    public String title;
    public String url;
    public float price;
    public int priceFen;
    public float shotFee;
    public int shotFeeFen;
    private String imgUrl;
    public float selfBuyOff;
    public String shotDesc;
    public int gender;
    public int heightMin;
    public int heightMax;
    public int ageMin;
    public int ageMax;
    public int areaid;
    public String areaName;
    public int modelerLevel;
    public int selfBuyRate;
    public int number;
    public int shotAreaId;
    public int status;
    public long createTime;
    public long updateTime;
    public float totalFee;
    public int totalFeeFen;
    public String nickname;
    public String oldUrl;
    public int acceptNumber;
    public int followNum;
    public long moteTaskId;
    public int acceptStauts;
    public boolean isDirect;
    public String h5Url;

    public Boolean isTimerTask = false; //是否为倒计时任务  false不是
    public Boolean isVipSeller = false; //是否为VIP商家  false不是
    public Boolean isCustomTask = false; // 是否为自定义任务 false不是

    public long diffTime;   //倒计时时间,正数倒计时，负数，正常接单
    public long futurePublishTime;//系统预发布时间
    public long databaseTime;//当前时间

    public boolean isAccepted;
    public int  finishStatus;

    public String getPreviewImageUrl() {
        return CommonUtil.getImage_400(this.imgUrl);
    }

    public String getDetailImageUrl() {
        return CommonUtil.getImage_800(this.imgUrl);
    }

    public boolean isFinish() {
        return this.finishStatus == 1;
    }

    public boolean canAccept() {
        return !isFinish() && this.acceptStauts == 1;
    }
}
