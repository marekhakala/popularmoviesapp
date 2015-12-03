package com.nothing.mark.popmoviesapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class TrailersEntity implements Parcelable {
    @Expose
    private Integer id;

    @Expose
    protected List<TrailerEntity> results = new ArrayList<TrailerEntity>();

    protected TrailersEntity(Parcel in) {
        this.id = in.readInt();
        in.readList(this.results, null);
    }

    public Integer getId() {
        return id;
    }

    public TrailersEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public List<TrailerEntity> getResults() {
        return results;
    }

    public TrailersEntity setResults(List<TrailerEntity> results) {
        this.results = results;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeList(this.results);
    }

    public static final Creator<TrailersEntity> CREATOR = new Creator<TrailersEntity>() {
        @Override
        public TrailersEntity createFromParcel(Parcel in) {
            return new TrailersEntity(in);
        }

        @Override
        public TrailersEntity[] newArray(int size) {
            return new TrailersEntity[size];
        }
    };
}
