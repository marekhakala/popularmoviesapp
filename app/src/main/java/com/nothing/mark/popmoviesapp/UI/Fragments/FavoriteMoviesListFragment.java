package com.nothing.mark.popmoviesapp.UI.Fragments;

import com.nothing.mark.popmoviesapp.UI.Widgets.RecyclerViewStates;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class FavoriteMoviesListFragment extends MoviesListFragment {
    protected Subscription mFavoritesSubscription = Subscriptions.empty();

    @Override
    public void onStart() {
        super.onStart();
        subscribeToMovies();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFavoritesSubscription.unsubscribe();
    }

    @Override
    public void onRefresh() {
        subscribeToMovies();
    }

    protected void subscribeToMovies() {
        if (!mSwipeRefreshLayout.isRefreshing())

        mFavoritesSubscription.unsubscribe();
        mFavoritesSubscription = mMoviesRepository.movies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> {
                    Timber.d("Repository> Favorite movies loading was finished. Size: " + String.valueOf(movies.size()));

                    mSwipeRefreshLayout.setRefreshing(false);
                    mMoviesAdapter.set(movies);

                    if(mMoviesAdapter.getItemCount() > 0)
                        mRecyclerViewStateWrapper.setState(RecyclerViewStates.NORMAL);
                    else
                        mRecyclerViewStateWrapper.setState(RecyclerViewStates.EMPTY);

                }, throwable -> {
                    Timber.e(throwable, "Repository> Favorite movies loading has failed.");
                    mRecyclerViewStateWrapper.setState(RecyclerViewStates.ERROR);
                });
    }
}
