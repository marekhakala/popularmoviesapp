package com.nothing.mark.popmoviesapp.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ShareCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.nothing.mark.popmoviesapp.API.ModeOrder;
import com.nothing.mark.popmoviesapp.Model.MovieEntity;
import com.nothing.mark.popmoviesapp.Model.TrailerEntity;
import com.nothing.mark.popmoviesapp.Repository.IMoviesRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class UtilityHelper {

    public static String getCursorString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndexOrThrow(column));
    }

    public static boolean getCursorBoolean(Cursor cursor, String column) {
        return getCursorInt(cursor, column) == 1;
    }

    public static long getCursorLong(Cursor cursor, String column) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(column));
    }

    public static int getCursorInt(Cursor cursor, String column) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(column));
    }

    public static Float getCursorFloat(Cursor cursor, String column) {
        return cursor.getFloat(cursor.getColumnIndexOrThrow(column));
    }

    public static boolean addMovieToFavorites(Context context,
                                           IMoviesRepository repository,
                                           MovieEntity entity, boolean favorite) {
        entity.setFavorite(favorite);

        ConstantValues.FAVORITE_MOVIES_SUBJECT.onNext(new MovieIdFavorite(entity, favorite));

        if (favorite) {
            repository.add(entity);
            addToFavorites(context, entity);
            return true;
        } else {
            repository.remove(entity);
            removeFromFavorites(context, entity);
            return false;
        }
    }

    public static Set<String> getListOfFavourites(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getStringSet(ConstantValues.PREF_MOVIE_FAVORITES, new HashSet<String>());
    }

    public static void addToFavorites(Context context, MovieEntity entity) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> movieFavoritesList = sp.getStringSet(ConstantValues.PREF_MOVIE_FAVORITES, null);

        if (movieFavoritesList == null)
            movieFavoritesList = new TreeSet<String>();

        movieFavoritesList.add(String.valueOf(entity.getId()));
        sp.edit().putStringSet(ConstantValues.PREF_MOVIE_FAVORITES, movieFavoritesList).apply();
    }

    public static void removeFromFavorites(Context context, MovieEntity entity) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> movieFavoritesList = sp.getStringSet(ConstantValues.PREF_MOVIE_FAVORITES, null);

        if (movieFavoritesList == null)
            movieFavoritesList = new TreeSet<String>();

        movieFavoritesList.remove(String.valueOf(entity.getId()));
        sp.edit().putStringSet(ConstantValues.PREF_MOVIE_FAVORITES, movieFavoritesList).apply();
    }

    public static boolean existInFavorites(Context context, MovieEntity entity) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> movieFavoritesList = sp.getStringSet(ConstantValues.PREF_MOVIE_FAVORITES, null);

        if(movieFavoritesList != null && movieFavoritesList.contains(String.valueOf(entity.getId())))
            return true;
        return false;
    }

    public static boolean playTrailer(Context context, TrailerEntity entity) {
        if (entity.getSite().toLowerCase().equals("youtube")) {
            String url = "http://www.youtube.com/watch?v=" + entity.getKey();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        }

        return false;
    }

    public static int getScreenSizeWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int findPosterSize(int screenWidth) {
        ArrayList<Integer> sizeList = new ArrayList<Integer>();
        sizeList.add(92);
        sizeList.add(154);
        sizeList.add(185);
        sizeList.add(342);
        sizeList.add(500);
        sizeList.add(780);

        int outputSize = 92;

        for (Integer value : sizeList) {
            if (value < screenWidth)
                outputSize = value;
        }
        return outputSize;
    }

    public static String posterSizePath(int width) {
        switch(width) {
            case 92: return "w92";
            case 154: return "w154";
            case 185: return "w185";
            case 342: return "w342";
            case 500: return "w500";
            default: return "w780";
        }
    }

    public static void shareMovie(Context context, MovieEntity entity) {
        String text = "Share movie";
        Intent intent = createShareIntent(context, entity);
        context.startActivity(Intent.createChooser(intent, text));
    }

    public static Intent createShareIntent(Context context, MovieEntity entity) {
        Activity activity;
        String message = "Check out "+ entity.getTitle() + " at https://www.themoviedb.org/movie/" + String.valueOf(entity.getId());

        if(context instanceof Activity) {
            activity = (android.app.Activity) context;

            ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(activity)
                    .setType("text/plain")
                    .setText(message);

            return intentBuilder.getIntent();
        }
        return null;
    }

    public static void addViewGroup(Handler handler, View view, ViewGroup viewGroup) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                viewGroup.addView(view);
            }
        });
    }

    public static void setVisibilityForViewGroup(Handler handler, int visibility, ViewGroup viewGroup) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                viewGroup.setVisibility(visibility);
            }
        });
    }

    public static void clearViewGroup(Handler handler, ViewGroup viewGroup) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for(int i = viewGroup.getChildCount() - 1; i > 1; --i)
                    viewGroup.removeViewAt(i);
            }
        });
    }

    public static void setStarIcon(Handler handler, MenuItem item, int drawable) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                item.setIcon(drawable);
            }
        });
    }

    public static String getReleaseDate(String releaseDate) {
        if(releaseDate.isEmpty())
            return "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(releaseDate));

            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
            String year = String.valueOf(calendar.get(Calendar.YEAR));

            return day + " " + month + " " + year;
        } catch (ParseException e) {
            return "";
        }
    }

    public static String getModeOrder(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(ConstantValues.PREF_MOVIES_MODE, ModeOrder.POPULARITY.toString());
    }

    public static void setModeOrder(Context context, String mode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(ConstantValues.PREF_MOVIES_MODE, mode).apply();
    }
}