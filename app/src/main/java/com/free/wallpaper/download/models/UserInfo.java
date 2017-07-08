package com.free.wallpaper.download.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor on 1/16/2017.
 */

public class UserInfo implements Parcelable {

    private String id;
    private String username;
    private String name;
    private String firstName;
    private String lastName;
    private String portfolioUrl;
       /* private String bio;
        private String location;
        private Integer totalLikes;
        private Integer totalPhotos;
        private Integer totalCollections;
        private ProfileImage profileImage;
        private Links links;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }


    protected UserInfo(Parcel in) {
        id = in.readString();
        username = in.readString();
        name = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        portfolioUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(portfolioUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}