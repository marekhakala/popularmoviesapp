package com.nothing.mark.popmoviesapp.Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class ReviewEntity implements Parcelable {
    @Expose
    private String id;

    private Long movieId = 0L;

    @Expose
    private String author;

    @Expose
    private String content;

    @Expose
    private String url;

    private ReviewEntity() {
    }

    private ReviewEntity(Parcel in) {
        this.id = in.readString();
        this.movieId = in.readLong();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public String getId() {
        return id;
    }

    public ReviewEntity setId(String id) {
        this.id = id;
        return this;
    }

    public Long getMovieId() {
        return movieId;
    }

    public ReviewEntity setMovieId(Long movieId) {
        this.movieId = movieId;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public ReviewEntity setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ReviewEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ReviewEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeLong(this.movieId);
        out.writeString(this.author);
        out.writeString(this.content);
        out.writeString(this.url);
    }

    public static final Parcelable.Creator<ReviewEntity> CREATOR = new Parcelable.Creator<ReviewEntity>() {
        public ReviewEntity createFromParcel(Parcel in) {
            return new ReviewEntity(in);
        }

        public ReviewEntity[] newArray(int size) {
            return new ReviewEntity[size];
        }
    };

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putString("id", this.id);
        bundle.putLong("movieid", this.movieId);
        bundle.putString("author", this.author);
        bundle.putString("content", this.content);
        bundle.putString("url", this.url);
        return bundle;
    }

    public static ReviewEntity fromBundle(Bundle bundle) {
        ReviewEntity entity = new ReviewEntity();

        entity.id = bundle.getString("id");
        entity.movieId = bundle.getLong("movieid");
        entity.author = bundle.getString("author");
        entity.content = bundle.getString("content");
        entity.url = bundle.getString("url");
        return entity;
    }
}
