package com.nothing.mark.popmoviesapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MoviesDatabase extends SQLiteOpenHelper {
    protected static final int DB_VERSION = 1;
    protected static final String DB_NAME = "popular_movies_app.db";

    public interface Tables {
        String MOVIES = "movies";
    }

    public MoviesDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + Tables.MOVIES + "("
                + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + MoviesContract.MoviesColumns.MOVIE_ID + " TEXT NOT NULL,"
                + MoviesContract.MoviesColumns.MOVIE_TITLE + " TEXT NOT NULL,"
                + MoviesContract.MoviesColumns.MOVIE_OVERVIEW + " TEXT,"
                + MoviesContract.MoviesColumns.MOVIE_RELEASE_DATE + " TEXT,"
                + MoviesContract.MoviesColumns.MOVIE_POSTER_PATH + " TEXT,"
                + MoviesContract.MoviesColumns.MOVIE_BACKDROP_PATH + " TEXT,"
                + MoviesContract.MoviesColumns.MOVIE_POPULARITY + " REAL,"
                + MoviesContract.MoviesColumns.MOVIE_VOTE_AVERAGE + " REAL,"
                + MoviesContract.MoviesColumns.MOVIE_VOTE_COUNT + " INTEGER,"
                + MoviesContract.MoviesColumns.MOVIE_FAVORITE + " INTEGER NOT NULL DEFAULT 0,"
                + "UNIQUE (" + MoviesContract.MoviesColumns.MOVIE_ID + ") ON CONFLICT REPLACE)";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.MOVIES);
        onCreate(db);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DB_NAME);
    }
}