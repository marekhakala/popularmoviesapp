package com.nothing.mark.popmoviesapp.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import timber.log.Timber;

import com.nothing.mark.popmoviesapp.Utilities.DBQueryBuilder;

public class MoviesProvider extends ContentProvider {
    protected static final String TAG_LOG = MoviesProvider.class.getSimpleName();

    protected static final UriMatcher sUriMatcher = buildUriMatcher();
    protected SQLiteOpenHelper mDBHelper = null;

    protected static final int MOVIES = 200;
    protected static final int MOVIES_ID = 201;

    @Override
    public boolean onCreate() {
        mDBHelper = new MoviesDatabase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                return MoviesContract.Movies.CONTENT_TYPE;
            case MOVIES_ID:
                return MoviesContract.Movies.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String order) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        String log_message = "Query> match: " + match + ", uri: " + uri;
        if(projection != null) log_message += ", projection: " + projection.toString();
        if(selection != null) log_message += ", selection: " + selection;
        if(selectionArgs != null) log_message += ", selectionArgs: " + selectionArgs.toString();

        Timber.tag(TAG_LOG).v(log_message);

        String distictString = uri.getQueryParameter("distict");
        boolean distinct = (distictString != null && !distictString.isEmpty());

        DBQueryBuilder queryBuilder = buildQuery(uri);

        Context context = getContext();

        Cursor cursor = queryBuilder
                .setWhere(selection, selectionArgs)
                .query(db, distinct, projection, order, null);

        if (context != null)
            cursor.setNotificationUri(context.getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String log_message = "Insert Query> uri: " + uri;
        if(values != null) log_message += ", values: " + values.toString();
        Timber.tag(TAG_LOG).v(log_message);

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MOVIES: {
                db.insertOrThrow(MoviesDatabase.Tables.MOVIES, null, values);
                notifyChange(uri);
                return MoviesContract.Movies.buildUri(values.getAsString(MoviesContract.Movies.MOVIE_ID));
            }
            default:
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String log_message = "Update Query> uri: " + uri;
        if(values != null) log_message += ", values: " + values.toString();
        if(selection != null) log_message += ", selection: " + selection;
        if(selectionArgs != null) log_message += ", selectionArgs: " + selectionArgs.toString();
        Timber.tag(TAG_LOG).v(log_message);

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        int returnValue = buildQuery(uri).setWhere(selection, selectionArgs).update(db, values);
        notifyChange(uri);

        return returnValue;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String log_message = "Delete Query> uri: " + uri + ", selection: " + selection
                + ", selectionArgs: " + selectionArgs.toString();
        Timber.tag(TAG_LOG).v(log_message);

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        if (uri.equals(MoviesContract.BASE_CONTENT_URI)) {
            deleteDatabase();
            notifyChange(uri);
            return 1;
        }

        int returnValue = buildQuery(uri).setWhere(selection, selectionArgs).delete(db);
        notifyChange(uri);

        return returnValue;
    }

    protected void deleteDatabase() {
        mDBHelper.close();
        Context context = getContext();
        MoviesDatabase.deleteDatabase(context);
        mDBHelper = new MoviesDatabase(getContext());
    }

    protected void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

    protected static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MoviesContract.CONTENT_AUTHORITY, "movies", MOVIES);
        matcher.addURI(MoviesContract.CONTENT_AUTHORITY, "movies/*", MOVIES_ID);

        return matcher;
    }

    protected DBQueryBuilder buildQuery(Uri uri) {
        DBQueryBuilder queryBuilder = new DBQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                return queryBuilder.setTable(MoviesDatabase.Tables.MOVIES);
            case MOVIES_ID:
                return queryBuilder.setTable(MoviesDatabase.Tables.MOVIES)
                        .setWhere(MoviesContract.Movies.MOVIE_ID + "=?",
                                MoviesContract.Movies.getId(uri));
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
