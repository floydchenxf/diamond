package com.floyd.diamond.biz.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by floyd on 15-12-3.
 */
public class AdvVO implements Parcelable {
    public long id;
    public String title;
    public String imgUrl;
    public long createTime;
    public long updateTime;
    public int type;
    public String content;

    public AdvVO(Parcel in) {
        id = in.readLong();
        title = in.readString();
        imgUrl = in.readString();
        createTime = in.readLong();
        updateTime = in.readLong();
        type = in.readInt();
        content = in.readString();
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
