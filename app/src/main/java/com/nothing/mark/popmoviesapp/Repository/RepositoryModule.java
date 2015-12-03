package com.nothing.mark.popmoviesapp.Repository;

import android.content.ContentResolver;
import com.nothing.mark.popmoviesapp.API.TMDbAPI;
import com.squareup.sqlbrite.BriteContentResolver;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    @Provides
    public IMoviesRepository providesMoviesRepository(TMDbAPI api, ContentResolver contentResolver,
                                                      BriteContentResolver briteContentResolver) {
        return new MoviesRepository(api, contentResolver, briteContentResolver);
    }
}
