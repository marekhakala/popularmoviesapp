package com.nothing.mark.popmoviesapp;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    protected PopularMoviesApplication application;

    public AppModule(PopularMoviesApplication application) {
        this.application = application;
    }

    @Provides @Singleton Application provideApplication() {
        return this.application;
    }
}
