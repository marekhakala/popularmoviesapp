package com.nothing.mark.popmoviesapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class MoviesPageEntity extends AbstractPageEntity implements Parcelable {

    @Expose
    protected List<MovieEntity> results = new ArrayList<MovieEntity>();

    public MoviesPageEntity() {
        this.page = 0L;
        this.totalPages = 0L;
        this.totalResults = 0L;
    }

    protected MoviesPageEntity(Parcel in) {
        this.page = in.readLong();
        in.readList(this.results, null);
        this.totalPages = in.readLong();
        this.totalResults = in.readLong();
    }

    public List<MovieEntity> getResults() {
        return results;
    }

    public MoviesPageEntity setResults(List<MovieEntity> results) {
        this.results = results;
        this.totalResults = new Long(results.size());
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.page);
        out.writeList(this.results);
        out.writeLong(this.totalPages);
        out.writeLong(this.totalResults);
    }

    public static final Creator<MoviesPageEntity> CREATOR = new Creator<MoviesPageEntity>() {
        @Override
        public MoviesPageEntity createFromParcel(Parcel in) {
            return new MoviesPageEntity(in);
        }

        @Override
        public MoviesPageEntity[] newArray(int size) {
            return new MoviesPageEntity[size];
        }
    };
}
