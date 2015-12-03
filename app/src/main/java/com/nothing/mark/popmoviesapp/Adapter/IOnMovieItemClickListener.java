package com.nothing.mark.popmoviesapp.Adapter;

import android.view.View;

import com.nothing.mark.popmoviesapp.Model.MovieEntity;

public interface IOnMovieItemClickListener {
    void onClicked(MovieEntity entity, View view, int position);

    IOnMovieItemClickListener EMPTY = new IOnMovieItemClickListener() {
        @Override
        public void onClicked(MovieEntity entity, View view, int index) {}
    };
}