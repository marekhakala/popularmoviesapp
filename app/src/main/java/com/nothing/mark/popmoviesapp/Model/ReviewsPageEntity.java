package com.nothing.mark.popmoviesapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class ReviewsPageEntity extends AbstractPageEntity implements Parcelable {

    @Expose
    protected List<ReviewEntity> results = new ArrayList<ReviewEntity>();

    protected ReviewsPageEntity(Parcel in) {
        this.page = in.readLong();
        in.readList(this.results, null);
        this.totalPages = in.readLong();
        this.totalResults = in.readLong();
    }

    public List<ReviewEntity> getResults() {
        return results;
    }

    public ReviewsPageEntity setResults(List<ReviewEntity> results) {
        this.results = results;
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.page);
        out.writeList(this.results);
        out.writeLong(this.totalPages);
        out.writeLong(this.totalResults);
    }

    public static final Creator<ReviewsPageEntity> CREATOR = new Creator<ReviewsPageEntity>() {
        @Override
        public ReviewsPageEntity createFromParcel(Parcel in) {
            return new ReviewsPageEntity(in);
        }

        @Override
        public ReviewsPageEntity[] newArray(int size) {
            return new ReviewsPageEntity[size];
        }
    };
}

