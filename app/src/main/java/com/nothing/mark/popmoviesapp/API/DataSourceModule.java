package com.nothing.mark.popmoviesapp.API;

import com.nothing.mark.popmoviesapp.Utilities.ConstantValues;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import static java.util.concurrent.TimeUnit.SECONDS;

@Module
public class DataSourceModule {

    @Provides
    Gson provideGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Provides
    OkHttpClient provideOkHttpClient() {
        return createOkHttpClient();
    }

    static OkHttpClient createOkHttpClient() {
        OkHttpClient client = new OkHttpClient();

        client.setConnectTimeout(12, SECONDS);
        client.setReadTimeout(12, SECONDS);
        client.setWriteTimeout(12, SECONDS);
        return client;
    }

    @Provides
    @Named("Api") OkHttpClient provideApiClient(OkHttpClient client) {
        return client.clone();
    }

    @Provides
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(ConstantValues.ENDPOINT_URL);
    }

    @Provides
    RestAdapter provideRestAdapter(Endpoint endpoint, @Named("Api") OkHttpClient client, Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setClient(new OkClient(client))
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(request -> request.addQueryParam("api_key", ConstantValues.TMDB_API_KEY))
                .build();
    }

    @Provides
    TMDbAPI provideMoviesApi(RestAdapter restAdapter) {
        return restAdapter.create(TMDbAPI.class);
    }
}