package com.nothing.mark.popmoviesapp.Repository;

import com.nothing.mark.popmoviesapp.API.ModeOrder;
import com.nothing.mark.popmoviesapp.Model.MovieEntity;
import com.nothing.mark.popmoviesapp.Model.ReviewEntity;
import com.nothing.mark.popmoviesapp.Model.TrailerEntity;

import java.util.List;
import java.util.Set;

import rx.Observable;

public interface IMoviesRepository {
    Observable<List<MovieEntity>> movies();
    Observable<List<MovieEntity>> movies(ModeOrder order, int page);
    Observable<Set<Long>> moviesId();

    void add(MovieEntity entity);
    void remove(MovieEntity entity);

    Observable<List<TrailerEntity>> trailers(long id);
    Observable<List<ReviewEntity>> reviews(long id);
}
