
package com.floyd.diamond.ui.pojo.banner;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class DataList implements Parcelable {

    private String id;
    private String gmtCreate;
    private String gmtModified;
    private String contentUrl;
    private String shareUrl;
    private String mainPic;
    private String userId;
    private String userNick;
    private String cardType;
    private String title;
    private String recommend;
    private String startTime;
    private String expiredTime;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public DataList(){

    }

    public DataList(Parcel in) {
        id = in.readString();
        gmtCreate = in.readString();
        gmtModified = in.readString();
        contentUrl = in.readString();
        shareUrl = in.readString();
        mainPic = in.readString();
        userId = in.readString();
        userNick = in.readString();
        cardType = in.readString();
        title = in.readString();
        recommend = in.readString();
        startTime = in.readString();
        expiredTime = in.readString();
    }

    /**
     *
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     *
     * @return
     *     The gmtCreate
     */
    public String getGmtCreate() {
        return gmtCreate;
    }
    /**
     *
     * @param gmtCreate
     *     The gmtCreate
     */
    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     *     The gmtModified
     */
    public String getGmtModified() {
        return gmtModified;
    }

    /**
     *
     * @param gmtModified
     *     The gmtModified
     */
    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     *
     * @return
     *     The contentUrl
     */
    public String getContentUrl() {
        return contentUrl;
    }

    /**
     *
     * @param contentUrl
     *     The contentUrl
     */
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    /**
     *
     * @return
     *     The mainPic
     */
    public String getMainPic() {
        return mainPic;
    }

    /**
     *
     * @param mainPic
     *     The mainPic
     */
    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }


    /**
     *
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     *     The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     *     The userNick
     */
    public String getUserNick() {
        return userNick;
    }

    /**
     *
     * @param userNick
     *     The userNick
     */
    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    /**
     *
     * @return
     *     The cardType
     */
    public String getCardType() {
        return cardType;
    }

    /**
     *
     * @param cardType
     *     The cardType
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(gmtCreate);
        dest.writeString(gmtModified);
        dest.writeString(contentUrl);
        dest.writeString(shareUrl);
        dest.writeString(mainPic);
        dest.writeString(userId);
        dest.writeString(userNick);
        dest.writeString(cardType);
        dest.writeString(title);
        dest.writeString(recommend);
        dest.writeString(startTime);
        dest.writeString(expiredTime);
    }

    public static final Parcelable.Creator<DataList> CREATOR = new Parcelable.Creator<DataList>() {
        public DataList createFromParcel(Parcel in) {
            return new DataList(in);
        }

        public DataList[] newArray(int size) {
            return new DataList[size];
        }
    };

}
