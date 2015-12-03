package com.nothing.mark.popmoviesapp.Database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    public interface MoviesColumns {
        String MOVIE_ID = "movie_id";
        String MOVIE_TITLE = "title";
        String MOVIE_OVERVIEW = "overview";
        String MOVIE_RELEASE_DATE = "release_date";
        String MOVIE_POSTER_PATH = "poster_path";
        String MOVIE_BACKDROP_PATH = "backdrop_path";
        String MOVIE_POPULARITY = "popularity";
        String MOVIE_VOTE_AVERAGE = "vote_average";
        String MOVIE_VOTE_COUNT = "vote_count";
        String MOVIE_FAVORITE = "favorite";
    }

    public static final String CONTENT_AUTHORITY = "com.nothing.mark.popmoviesapp.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static class Movies implements MoviesColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String SORT_ORDER = BaseColumns._ID + " DESC";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.ANY_CURSOR_ITEM_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static Uri buildUri(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

        public static String getId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    private MoviesContract() {}
}
