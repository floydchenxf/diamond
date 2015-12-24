package com.floyd.diamond.biz.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by floyd on 15-12-20.
 */
public class AreaDetailVO implements Parcelable {

    public long provideId;
    public long cityId;
    public long districtId;
    public String addressDetail;
    public String addressSummary;

    public AreaDetailVO(Parcel in) {
        provideId = in.readLong();
        cityId = in.readLong();
        districtId = in.readLong();
        addressDetail = in.readString();
        addressSummary = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(provideId);
        dest.writeLong(cityId);
        dest.writeLong(districtId);
        dest.writeString(addressDetail);
        dest.writeString(addressSummary);
    }

    public static final Parcelable.Creator<AreaDetailVO> CREATOR = new Parcelable.Creator<AreaDetailVO>() {
        public AreaDetailVO createFromParcel(Parcel in) {
            return new AreaDetailVO(in);
        }

        public AreaDetailVO[] newArray(int size) {
            return new AreaDetailVO[size];
        }
    };
}
