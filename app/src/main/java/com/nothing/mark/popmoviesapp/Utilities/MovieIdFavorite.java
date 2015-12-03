package com.nothing.mark.popmoviesapp.Utilities;

import com.nothing.mark.popmoviesapp.Model.MovieEntity;

public class MovieIdFavorite {
    public long mId;
    public boolean mFavorite;

    public MovieIdFavorite(MovieEntity entity, boolean favorite) {
        this(entity.getId(), favorite);
    }

    public MovieIdFavorite(long id, boolean favorite) {
        this.mId = id;
        this.mFavorite = favorite;
    }

    public long getId() {
        return this.mId;
    }

    public MovieIdFavorite setId(long mId) {
        this.mId = mId;
        return this;
    }

    public boolean getFavorite() {
        return this.mFavorite;
    }

    public MovieIdFavorite setFavorite(boolean value) {
        this.mFavorite = value;
        return this;
    }

    public boolean compareId(MovieEntity entity) {
        return entity.getId() == getId();
    }
}
