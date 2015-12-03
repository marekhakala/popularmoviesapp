package com.nothing.mark.popmoviesapp.API;

import com.nothing.mark.popmoviesapp.Model.MoviesPageEntity;
import com.nothing.mark.popmoviesapp.Model.ReviewsPageEntity;
import com.nothing.mark.popmoviesapp.Model.TrailersEntity;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface TMDbAPI {
    @GET("/discover/movie") Observable<MoviesPageEntity> movies(@Query("sort_by") ModeOrder order,
                                                                @Query("page") int page);

    @GET("/movie/{id}/reviews") Observable<ReviewsPageEntity> reviews(@Path("id") long id,
                                                                      @Query("page") int page);

    @GET("/movie/{id}/videos") Observable<TrailersEntity> trailers(@Path("id") long id);
}
