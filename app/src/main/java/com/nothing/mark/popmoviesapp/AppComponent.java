package com.nothing.mark.popmoviesapp;

import com.nothing.mark.popmoviesapp.API.DataSourceComponent;
import com.nothing.mark.popmoviesapp.API.DataSourceModule;
import com.nothing.mark.popmoviesapp.Database.ProviderComponent;
import com.nothing.mark.popmoviesapp.Database.ProviderModule;
import com.nothing.mark.popmoviesapp.Repository.RepositoryComponent;
import com.nothing.mark.popmoviesapp.Repository.RepositoryModule;
import com.nothing.mark.popmoviesapp.UI.UiComponent;
import com.nothing.mark.popmoviesapp.UI.UiModule;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                DataSourceModule.class,
                ProviderModule.class,
                RepositoryModule.class,
                UiModule.class
        }
)
public interface AppComponent extends DataSourceComponent, ProviderComponent, RepositoryComponent, UiComponent {
    void inject(PopularMoviesApplication application);
}
