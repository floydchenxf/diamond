package com.floyd.diamond.biz.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.floyd.diamond.utils.CommonUtil;

/**
 * Created by floyd on 15-12-3.
 */
public class AdvVO implements Parcelable, IKeepClassForProguard {
    public long id;
    public String title;
    private String imgUrl;
    public long createTime;
    public long updateTime;
    public int type;
    public int urlType;
    public String url;
//    public String content;

    public AdvVO(Parcel in) {
        id = in.readLong();
        urlType=in.readInt();
        title = in.readString();
        imgUrl = in.readString();
        createTime = in.readLong();
        updateTime = in.readLong();
        type = in.readInt();
        url=in.readString();
//        content = in.readString();
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPreviewUrl() {
        return CommonUtil.getImage_400(this.imgUrl);
    }

    public String getImgUrl() {
        return CommonUtil.getImage_800(this.imgUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(imgUrl);
        dest.writeLong(createTime);
        dest.writeLong(updateTime);
        dest.writeInt(type);
        dest.writeInt(urlType);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<AdvVO> CREATOR = new Parcelable.Creator<AdvVO>() {
        public AdvVO createFromParcel(Parcel in) {
            return new AdvVO(in);
        }

        public AdvVO[] newArray(int size) {
            return new AdvVO[size];
        }
    };
}
