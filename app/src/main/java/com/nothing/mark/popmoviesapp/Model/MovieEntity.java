package com.nothing.mark.popmoviesapp.Model;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.nothing.mark.popmoviesapp.Database.MoviesContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class MovieEntity implements Parcelable {
    @Expose
    private Long id;

    @Expose
    private String title;

    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @Expose
    private Float popularity;

    @SerializedName("vote_average")
    @Expose
    private Float voteAverage;

    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    private boolean favorite = false;

    public MovieEntity() {
    }

    private MovieEntity(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.popularity = in.readFloat();
        this.voteAverage = in.readFloat();
        this.voteCount = in.readInt();
        this.favorite = (in.readInt() == 1);
    }

    public Long getId() {
        return id;
    }

    public MovieEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MovieEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public MovieEntity setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public MovieEntity setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public MovieEntity setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public String getBackdropPath() {
        return this.backdropPath;
    }

    public MovieEntity setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public Float getPopularity() {
        return popularity;
    }

    public MovieEntity setPopularity(Float popularity) {
        this.popularity = popularity;
        return this;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public MovieEntity setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public MovieEntity setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public MovieEntity setFavorite(boolean favorite) {
        this.favorite = favorite;
        return this;
    }

    public MovieEntity setFavorite(Set<Long> ids) {
        this.favorite = ids.contains(this.id);
        return this;
    }

    public Uri getPosterUri(String url, String size) {
        Uri result = Uri.parse(url).buildUpon().appendPath(size)
                .appendEncodedPath(this.posterPath).build();

        return result;
    }

    public Uri getBackdropUri(String url, String size) {
        Uri result = Uri.parse(url).buildUpon().appendPath(size)
                .appendEncodedPath(this.backdropPath).build();

        return result;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.id);
        out.writeString(this.title);
        out.writeString(this.overview);
        out.writeString(this.releaseDate);
        out.writeString(this.posterPath);
        out.writeString(this.backdropPath);
        out.writeFloat(this.popularity);
        out.writeFloat(this.voteAverage);
        out.writeInt(this.voteCount);
        out.writeInt(this.favorite ? 1 : 0);
    }

    public static final Parcelable.Creator<MovieEntity> CREATOR = new Parcelable.Creator<MovieEntity>() {
        public MovieEntity createFromParcel(Parcel in) {
            return new MovieEntity(in);
        }

        public MovieEntity[] newArray(int size) {
            return new MovieEntity[size];
        }
    };

    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.Movies.MOVIE_ID, id);
        values.put(MoviesContract.Movies.MOVIE_TITLE, title);
        values.put(MoviesContract.Movies.MOVIE_OVERVIEW, overview);
        values.put(MoviesContract.Movies.MOVIE_RELEASE_DATE, releaseDate);
        values.put(MoviesContract.Movies.MOVIE_POSTER_PATH, posterPath);
        values.put(MoviesContract.Movies.MOVIE_BACKDROP_PATH, backdropPath);
        values.put(MoviesContract.Movies.MOVIE_POPULARITY, popularity);
        values.put(MoviesContract.Movies.MOVIE_VOTE_AVERAGE, voteAverage);
        values.put(MoviesContract.Movies.MOVIE_VOTE_COUNT, voteCount);
        values.put(MoviesContract.Movies.MOVIE_FAVORITE, favorite);

        return values;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putLong("id", id);
        bundle.putString("title", title);
        bundle.putString("overview", overview);
        bundle.putString("release_date", releaseDate);
        bundle.putString("poster_path", posterPath);
        bundle.putString("backdrop_path", backdropPath);
        bundle.putFloat("popularity", popularity);
        bundle.putFloat("vote_average", voteAverage);
        bundle.putInt("vote_count", voteCount);
        bundle.putInt("favorite", favorite ? 1 : 0);
        return bundle;
    }

    public static MovieEntity fromBundle(Bundle bundle) {
        MovieEntity entity = new MovieEntity();

        entity.id = bundle.getLong("id");
        entity.title = bundle.getString("title");
        entity.overview = bundle.getString("overview");
        entity.releaseDate = bundle.getString("release_date");
        entity.posterPath = bundle.getString("poster_path");
        entity.backdropPath = bundle.getString("backdrop_path");
        entity.popularity = bundle.getFloat("popularity");
        entity.voteAverage = bundle.getFloat("vote_average");
        entity.voteCount = bundle.getInt("vote_count");
        entity.favorite = (bundle.getInt("favorite") == 1);
        return entity;
    }
}
