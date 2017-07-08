package com.free.wallpaper.download.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Collection implements Parcelable {

    private String id;
    private String title;
    private String description;
    private String publishedAt;
    private Boolean curated;
    private Boolean featured;
    private int total_photos;

    private UserInfo user;
    private PhotoUrl urls;
    Photo cover_photo;


    public Collection(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Boolean getCurated() {
        return curated;
    }

    public void setCurated(Boolean curated) {
        this.curated = curated;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public int getTotalPhotos() {
        return total_photos;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.total_photos= totalPhotos;
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

    public void setCover_photo(UserInfo user) {
        this.user = user;
    }
    public void setPhoto(Photo cover_photo){
        this.cover_photo = cover_photo;
    }
    public Photo getCover_photo(){
        return cover_photo;
    }



    protected Collection(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        publishedAt = in.readString();
        byte curatedVal = in.readByte();
        curated = curatedVal == 0x02 ? null : curatedVal != 0x00;
        byte featuredVal = in.readByte();
        featured = featuredVal == 0x02 ? null : featuredVal != 0x00;
        total_photos = in.readInt();
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
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(publishedAt);
        if (curated == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (curated ? 0x01 : 0x00));
        }
        if (featured == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (featured ? 0x01 : 0x00));
        }
        dest.writeInt(total_photos);
        dest.writeValue(user);
        dest.writeValue(urls);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Collection> CREATOR = new Parcelable.Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel in) {
            return new Collection(in);
        }

        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
        }
    };
}