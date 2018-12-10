package com.yakin.socialsdk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SocialScene implements Parcelable {

    private String title;
    private String desc;
    private String content;
    private String thumb;
    private String link;

    public SocialScene setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SocialScene setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public SocialScene setContent(String content) {
        this.content = content;
        return this;
    }

    public String getContent() {
        return content;
    }

    public SocialScene setThumb(String thumb) {
        this.thumb = thumb;
        return this;
    }

    public String getThumb() {
        return thumb;
    }

    public SocialScene setLink(String link) {
        this.link = link;
        return this;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "title=" + title + "\ndesc=" + desc + "\ncontent=" +
                content + "\nthumb=" + thumb + "\nlink=" + link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(content);
        dest.writeString(thumb);
        dest.writeString(link);
    }

    public static final Parcelable.Creator<SocialScene> CREATOR = new Parcelable.Creator<SocialScene>() {

        public SocialScene createFromParcel(Parcel in) {
            return new SocialScene()
                    .setTitle(in.readString())
                    .setDesc(in.readString())
                    .setContent(in.readString())
                    .setThumb(in.readString())
                    .setLink(in.readString());
        }

        public SocialScene[] newArray(int size) {
            return new SocialScene[size];
        }
    };
}
