package com.nothing.mark.popmoviesapp.Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailerEntity implements Parcelable {
    @Expose
    private String id;

    private Long movieId = 0L;

    @Expose
    @SerializedName("iso_639_1")
    private String language;

    @Expose
    private String key;

    @Expose
    private String name;

    @Expose
    private String site;

    @Expose
    private Integer size;

    @Expose
    private String type;

    private TrailerEntity() {
    }

    private TrailerEntity(Parcel in) {
        this.id = in.readString();
        this.movieId = in.readLong();
        this.language = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    public String getId() {
        return id;
    }

    public TrailerEntity setId(String id) {
        this.id = id;
        return this;
    }

    public Long getMovieId() {
        return movieId;
    }

    public TrailerEntity setMovieId(Long movieId) {
        this.movieId = movieId;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public TrailerEntity setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getKey() {
        return key;
    }

    public TrailerEntity setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        return name;
    }

    public TrailerEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getSite() {
        return site;
    }

    public TrailerEntity setSite(String site) {
        this.site = site;
        return this;
    }

    public Integer getSize() {
        return size;
    }

    public TrailerEntity setSize(Integer size) {
        this.size = size;
        return this;
    }

    public String getType() {
        return type;
    }

    public TrailerEntity setType(String type) {
        this.type = type;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeLong(this.movieId);
        out.writeString(this.language);
        out.writeString(this.key);
        out.writeString(this.name);
        out.writeString(this.site);
        out.writeInt(this.size);
        out.writeString(this.type);
    }

    public static final Parcelable.Creator<TrailerEntity> CREATOR = new Parcelable.Creator<TrailerEntity>() {
        public TrailerEntity createFromParcel(Parcel in) {
            return new TrailerEntity(in);
        }

        public TrailerEntity[] newArray(int size) {
            return new TrailerEntity[size];
        }
    };

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putString("id", this.id);
        bundle.putLong("movieid", this.movieId);
        bundle.putString("language", this.language);
        bundle.putString("key", this.key);
        bundle.putString("name", this.name);
        bundle.putString("site", this.site);
        bundle.putInt("size", this.size);
        bundle.putString("type", this.type);
        return bundle;
    }

    public static TrailerEntity fromBundle(Bundle bundle) {
        TrailerEntity entity = new TrailerEntity();

        entity.id = bundle.getString("id");
        entity.movieId = bundle.getLong("movieid");
        entity.language = bundle.getString("language");
        entity.key = bundle.getString("key");
        entity.name = bundle.getString("name");
        entity.site = bundle.getString("site");
        entity.size = bundle.getInt("size");
        entity.type = bundle.getString("type");
        return entity;
    }
}
