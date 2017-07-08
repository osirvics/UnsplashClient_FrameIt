package com.free.wallpaper.download.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {

    private String id;
    private String created_at;
    private int width;
    private int height;
    private String color;
    private int likes;

    private UserInfo user;
    private PhotoUrl urls;

    public Photo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public PhotoUrl getUrls() {
        return urls;
    }

    public void setUrls(PhotoUrl urls) {
        this.urls = urls;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public int getRegularWidth() {
        try {
            int w = Integer.parseInt(Uri.parse(urls.regular).getQueryParameter("w"));
            return w == 0 ? 1080 : w;
        } catch (NumberFormatException e) {
            return 1080;
        }
    }

    public int getRegularHeight() {
        int w = getRegularWidth();
        return (int) (1.0 * height * w / width);
    }


    protected Photo(Parcel in) {
        id = in.readString();
        created_at = in.readString();
        width = in.readInt();
        height = in.readInt();
        color = in.readString();
        likes = in.readInt();
        user = (UserInfo) in.readValue(UserInfo.class.getClassLoader());
        urls = (PhotoUrl) in.readValue(PhotoUrl.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(created_at);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(color);
        dest.writeInt(likes);
        dest.writeValue(user);
        dest.writeValue(urls);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}