package com.nothing.mark.popmoviesapp.Database;

import android.app.Application;
import android.content.ContentResolver;

import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class ProviderModule {

    @Provides
    SqlBrite provideSqlBrite() {
        return SqlBrite.create(message -> Timber.tag("SQLDataSource").v(message));
    }

    @Provides
    ContentResolver provideContentResolver(Application application) {
        return application.getContentResolver();
    }

    @Provides
    BriteContentResolver provideBrideContentResolver(SqlBrite sqlBrite, ContentResolver contentResolver) {
        return sqlBrite.wrapContentProvider(contentResolver);
    }
}
