package com.example.nikhil.Shortnews;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.ads.NativeExpressAdView;

public class NewsData extends Object implements Parcelable {
    private String title,urlToImage,content,url,description,publishedAt;
    private NativeExpressAdView adView;

    public NewsData(String title, String urlToImage, String content,String url,String description,String publishedAt){
        this.title = title;
        this.urlToImage = urlToImage;
        this.content = content;
        this.url=url;
        this.description=description;
        this.publishedAt=publishedAt;
    }

    protected NewsData(Parcel in) {
        title = in.readString();
        urlToImage = in.readString();
        content = in.readString();
        url = in.readString();
        description = in.readString();
        publishedAt = in.readString();
    }

    public static final Creator<NewsData> CREATOR = new Creator<NewsData>() {
        @Override
        public NewsData createFromParcel(Parcel in) {
            return new NewsData(in);
        }

        @Override
        public NewsData[] newArray(int size) {
            return new NewsData[size];
        }
    };

    public NativeExpressAdView getAdView() {
        return adView;
    }

    public void setAdView(NativeExpressAdView adView) {
        this.adView = adView;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(urlToImage);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(publishedAt);
    }
}
