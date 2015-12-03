/*
************************************************************************
* PopMoviesApp 1.0 (GNU GPL v3) for Android platform
* Copyright (C) 2015  Marek Hakala
************************************************************************
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.nothing.mark.popmoviesapp;

import android.app.Application;
import android.content.Context;

import com.nothing.mark.popmoviesapp.API.DataSourceModule;
import com.nothing.mark.popmoviesapp.Database.ProviderModule;
import com.nothing.mark.popmoviesapp.Repository.RepositoryModule;
import com.nothing.mark.popmoviesapp.UI.UiModule;
import timber.log.Timber;

public class PopularMoviesApplication extends Application {
    protected AppComponent component;

    public static AppComponent appComponent(Context context) {
        return get(context).getComponent();
    }

    public AppComponent getComponent() {
        return component;
    }

    public static PopularMoviesApplication get(Context context) {
        return (PopularMoviesApplication) context.getApplicationContext();
    }

    @Override public void onCreate() {
        super.onCreate();
        setupComponent();
        Timber.plant(new Timber.DebugTree());
    }

    protected void setupComponent() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataSourceModule(new DataSourceModule())
                .providerModule(new ProviderModule())
                .repositoryModule(new RepositoryModule())
                .uiModule(new UiModule())
                .build();
    }
}
