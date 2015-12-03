package com.nothing.mark.popmoviesapp.Repository;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.nothing.mark.popmoviesapp.API.ModeOrder;
import com.nothing.mark.popmoviesapp.API.TMDbAPI;
import com.nothing.mark.popmoviesapp.Database.MoviesContract;
import com.nothing.mark.popmoviesapp.Model.MovieEntity;
import com.nothing.mark.popmoviesapp.Model.ReviewEntity;
import com.nothing.mark.popmoviesapp.Model.TrailerEntity;
import com.nothing.mark.popmoviesapp.Utilities.UtilityHelper;
import com.squareup.sqlbrite.BriteContentResolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class MoviesRepository implements IMoviesRepository {
    protected TMDbAPI mApi;
    protected ContentResolver mContentResolver;
    protected BriteContentResolver mBriteContentResolver;

    private BehaviorSubject<Set<Long>> mMoviesIdSubject;

    public static final String[] PROJECTION = {
            MoviesContract.Movies._ID,
            MoviesContract.Movies.MOVIE_ID,
            MoviesContract.Movies.MOVIE_TITLE,
            MoviesContract.Movies.MOVIE_OVERVIEW,
            MoviesContract.Movies.MOVIE_RELEASE_DATE,
            MoviesContract.Movies.MOVIE_POSTER_PATH,
            MoviesContract.Movies.MOVIE_BACKDROP_PATH,
            MoviesContract.Movies.MOVIE_POPULARITY,
            MoviesContract.Movies.MOVIE_VOTE_AVERAGE,
            MoviesContract.Movies.MOVIE_VOTE_COUNT,
            MoviesContract.Movies.MOVIE_FAVORITE
    };

    public static final String[] ID_PROJECTION = {
            MoviesContract.Movies.MOVIE_ID
    };

    public MoviesRepository(TMDbAPI api, ContentResolver contentResolver,
                            BriteContentResolver briteContentResolver) {
        mApi = api;
        mContentResolver = contentResolver;
        mBriteContentResolver = briteContentResolver;
    }

    @Override
    public Observable<List<MovieEntity>> movies() {
        return mBriteContentResolver.createQuery(MoviesContract.Movies.CONTENT_URI, PROJECTION, null, null, MoviesContract.Movies.SORT_ORDER, true)
                .map(query -> {
                    Cursor cursor = query.run();

                    try {
                        List<MovieEntity> moviesList = new ArrayList<MovieEntity>();

                        while (cursor.moveToNext()) {
                            MovieEntity entity = new MovieEntity();

                            entity.setId(UtilityHelper.getCursorLong(cursor, MoviesContract.Movies.MOVIE_ID));
                            entity.setTitle(UtilityHelper.getCursorString(cursor, MoviesContract.Movies.MOVIE_TITLE));
                            entity.setOverview(UtilityHelper.getCursorString(cursor, MoviesContract.Movies.MOVIE_OVERVIEW));
                            entity.setReleaseDate(UtilityHelper.getCursorString(cursor, MoviesContract.Movies.MOVIE_RELEASE_DATE));
                            entity.setPosterPath(UtilityHelper.getCursorString(cursor, MoviesContract.Movies.MOVIE_POSTER_PATH));
                            entity.setBackdropPath(UtilityHelper.getCursorString(cursor, MoviesContract.Movies.MOVIE_BACKDROP_PATH));
                            entity.setPopularity(UtilityHelper.getCursorFloat(cursor, MoviesContract.Movies.MOVIE_POPULARITY));
                            entity.setVoteAverage(UtilityHelper.getCursorFloat(cursor, MoviesContract.Movies.MOVIE_VOTE_AVERAGE));
                            entity.setVoteCount(UtilityHelper.getCursorInt(cursor, MoviesContract.Movies.MOVIE_VOTE_COUNT));
                            entity.setFavorite(UtilityHelper.getCursorBoolean(cursor, MoviesContract.Movies.MOVIE_FAVORITE));
                            moviesList.add(entity);
                        }
                        return moviesList;
                    } finally {
                        cursor.close();
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<MovieEntity>> movies(ModeOrder order, int page) {
        return mApi.movies(order, page)
                .timeout(6, TimeUnit.SECONDS)
                .retry(3)
                .map(response -> response.getResults())
                .withLatestFrom(getSavedMovieIds(), (movies, ids) -> {
                    for (MovieEntity movie : movies)
                        movie.setFavorite(ids);
                    return movies;
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Set<Long>> moviesId() {
        return mBriteContentResolver.createQuery(MoviesContract.Movies.CONTENT_URI, ID_PROJECTION, null, null, null, true)
                .map(query -> {
                    Cursor cursor = query.run();
                    Set<Long> idSet = new HashSet<Long>(cursor.getCount());

                    try {
                        while (cursor.moveToNext())
                            idSet.add(UtilityHelper.getCursorLong(cursor, MoviesContract.Movies.MOVIE_ID));
                    } finally {
                        cursor.close();
                    }
                    return idSet;
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void add(MovieEntity entity) {
        AsyncQueryHandler handler = new AsyncQueryHandler(mContentResolver) {};
        handler.startInsert(-1, null, MoviesContract.Movies.CONTENT_URI, entity.getValues());
    }

    @Override
    public void remove(MovieEntity entity) {
        AsyncQueryHandler handler = new AsyncQueryHandler(mContentResolver) {};
        String[] args = new String[]{String.valueOf(entity.getId())};
        handler.startDelete(-1, null, MoviesContract.Movies.CONTENT_URI, MoviesContract.Movies.MOVIE_ID + "=?", args);
    }

    @Override
    public Observable<List<ReviewEntity>> reviews(long id) {
        return mApi.reviews(id, 1)
                .timeout(6, TimeUnit.SECONDS)
                .retry(3)
                .map(response -> response.getResults());
    }

    @Override
    public Observable<List<TrailerEntity>> trailers(long id) {
        return mApi.trailers(id)
                .timeout(6, TimeUnit.SECONDS)
                .retry(3)
                .map(response -> response.getResults());
    }

    private Observable<Set<Long>> getSavedMovieIds() {
        if (mMoviesIdSubject == null) {
            mMoviesIdSubject = BehaviorSubject.create();
            moviesId().subscribe(mMoviesIdSubject);
        }

        return mMoviesIdSubject.asObservable();
    }
}
