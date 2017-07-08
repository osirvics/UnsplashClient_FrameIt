package com.free.wallpaper.download.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor on 1/11/2017.
 */

public class PhotoUrl implements Parcelable {

    public String raw;
    public String full;
    public String regular;
    public String small;
    public String thumb;


    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }



    protected PhotoUrl(Parcel in) {
        raw = in.readString();
        full = in.readString();
        regular = in.readString();
        small = in.readString();
        thumb = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(raw);
        dest.writeString(full);
        dest.writeString(regular);
        dest.writeString(small);
        dest.writeString(thumb);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PhotoUrl> CREATOR = new Parcelable.Creator<PhotoUrl>() {
        @Override
        public PhotoUrl createFromParcel(Parcel in) {
            return new PhotoUrl(in);
        }

        @Override
        public PhotoUrl[] newArray(int size) {
            return new PhotoUrl[size];
        }
    };
}