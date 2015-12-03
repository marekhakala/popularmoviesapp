package com.nothing.mark.popmoviesapp.UI.Fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.nothing.mark.popmoviesapp.API.ModeOrder;
import com.nothing.mark.popmoviesapp.Model.MovieEntity;
import com.nothing.mark.popmoviesapp.R;
import com.nothing.mark.popmoviesapp.UI.Widgets.EndlessScrollListener;
import com.nothing.mark.popmoviesapp.UI.Widgets.RecyclerViewStates;
import com.nothing.mark.popmoviesapp.Utilities.ConstantValues;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

public class SortedMoviesListFragment extends MoviesListFragment implements EndlessScrollListener.OnLoadMoreCallback {
    private static final int VISIBLE_THRESHOLD = 10;

    private static final String EXTRA_STATE_ORDER = "order";
    private static final String EXTRA_STATE_LOADING = "loading";
    private static final String EXTRA_STATE_CURRENT_PAGE = "current_page";

    private EndlessScrollListener mEndlessScrollListener;
    private BehaviorSubject<Observable<List<MovieEntity>>> mItemsObservableSubject = BehaviorSubject.create();

    private ModeOrder mOrderMode;
    private int mCurrentPage = 0;
    private boolean mLoading = false;

    public static SortedMoviesListFragment newInstance(ModeOrder order) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_STATE_ORDER, order);

        SortedMoviesListFragment fragment = new SortedMoviesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        mOrderMode = (ModeOrder) getArguments().getSerializable(EXTRA_STATE_ORDER);

        if (bundle != null) {
            Timber.d("Restore from bundle> Page: " + String.valueOf(mCurrentPage));

            mCurrentPage = bundle.getInt(EXTRA_STATE_CURRENT_PAGE, 0);
            mLoading = bundle.getBoolean(EXTRA_STATE_LOADING, true);
        }

        mMoviesAdapter.setLoadMore(true);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        mSubscriptions.add(ConstantValues.FAVORITE_MOVIES_SUBJECT.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    int count = mMoviesAdapter.getItemCount();

                    for (int position = 0; position < count; position++) {
                        if (mMoviesAdapter.getItemId(position) == event.getId())
                            mMoviesAdapter.notifyItemChanged(position);
                    }
                }));

        subscribeToMovies();

        if (bundle == null)
            reloadContent();
    }

    @Override
    public void onSaveInstanceState(Bundle bandle) {
        super.onSaveInstanceState(bandle);
        bandle.putInt(EXTRA_STATE_CURRENT_PAGE, mCurrentPage);
        bandle.putBoolean(EXTRA_STATE_LOADING, mLoading);
        bandle.putSerializable(EXTRA_STATE_ORDER, mOrderMode);
    }

    @Override
    public void onDestroyView() {
        mSubscriptions.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        reloadContent();
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (mMoviesAdapter.isLoadMore())
            pullPage(page);
    }

    protected void reloadContent() {
        if(!mSwipeRefreshLayout.isRefreshing())
            mRecyclerViewStateWrapper.setState(RecyclerViewStates.LOADING);

        mCurrentPage = 0;
        mSelectedPosition = -1;
        reAddOnScrollListener(mGridLayoutManager, mCurrentPage);
        pullPage(1);
    }

    private void subscribeToMovies() {
        Timber.d("Repository> Subscribing of movies beginning...");

        mSubscriptions.add(Observable.concat(mItemsObservableSubject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviesList -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mCurrentPage++;

                    Timber.d("Repository> Subscribing of movies" +
                            " - Page: " + mCurrentPage + ", Size: " + String.valueOf(moviesList.size()));

                    if (mCurrentPage == 1)
                        mMoviesAdapter.clear();

                    mMoviesAdapter.setLoadMore(!moviesList.isEmpty());
                    mMoviesAdapter.add(moviesList);

                    if(moviesList.size() > 0)
                        mRecyclerViewStateWrapper.setState(RecyclerViewStates.NORMAL);
                    else
                        mRecyclerViewStateWrapper.setState(RecyclerViewStates.EMPTY);

                    Timber.d("Repository> Subscribing of movies was finished.");
                }, throwable -> {
                    Timber.e(throwable, "Repository> Subscribing of movies has failed.");
                    mSwipeRefreshLayout.setRefreshing(false);

                    mMoviesAdapter.setLoadMore(false);
                    Toast.makeText(getActivity(), R.string.view_error_message, Toast.LENGTH_SHORT).show();
                    mRecyclerViewStateWrapper.setState(RecyclerViewStates.ERROR);
                }));
    }

    private void pullPage(int page) {
        Timber.d("Repository> Page: " + String.valueOf(page) + ", Subscribing of movies beginning...");
        mItemsObservableSubject.onNext(mMoviesRepository.movies(mOrderMode, page));
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        reAddOnScrollListener(mGridLayoutManager, mCurrentPage);
    }

    private void reAddOnScrollListener(GridLayoutManager layoutManager, int startPage) {
        if (mEndlessScrollListener != null)
            mRecyclerView.removeOnScrollListener(mEndlessScrollListener);

        mEndlessScrollListener = EndlessScrollListener.fromGridLayoutManager(layoutManager, VISIBLE_THRESHOLD, startPage).setCallback(this);
        mRecyclerView.addOnScrollListener(mEndlessScrollListener);
    }

}

