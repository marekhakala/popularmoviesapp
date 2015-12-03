package com.nothing.mark.popmoviesapp.UI.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.nothing.mark.popmoviesapp.Adapter.IOnMovieItemClickListener;
import com.nothing.mark.popmoviesapp.AppComponent;
import com.nothing.mark.popmoviesapp.Model.MovieEntity;
import com.nothing.mark.popmoviesapp.R;
import com.nothing.mark.popmoviesapp.Repository.IMoviesRepository;
import com.nothing.mark.popmoviesapp.Adapter.MoviesAdapter;
import com.nothing.mark.popmoviesapp.UI.Widgets.MultiSwipeRefreshLayout;
import com.nothing.mark.popmoviesapp.UI.Widgets.RecyclerViewStates;
import com.nothing.mark.popmoviesapp.UI.Widgets.RecyclerViewStatesWrapper;
import com.nothing.mark.popmoviesapp.Utilities.UtilityHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

public abstract class MoviesListFragment extends AbstractBaseFragment implements
        MultiSwipeRefreshLayout.OnRefreshListener,
        MultiSwipeRefreshLayout.IScrollUpCallback, IOnMovieItemClickListener {

    public interface Listener {
        void onMovieSelected(MovieEntity entity, View view);
    }

    protected static final String EXTRA_STATE_MOVIES = "movies";
    protected static final String EXTRA_STATE_SELECTED_POSITION = "selected_position";

    @Bind(R.id.movies_recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.movies_recycler_view_states_wrapper) RecyclerViewStatesWrapper mRecyclerViewStateWrapper;
    @Bind(R.id.multi_swipe_refresh_layout) MultiSwipeRefreshLayout mSwipeRefreshLayout;

    @Inject IMoviesRepository mMoviesRepository;

    protected MoviesAdapter mMoviesAdapter;

    protected GridLayoutManager mGridLayoutManager;
    protected CompositeSubscription mSubscriptions;

    protected Listener mListener;
    protected int mSelectedPosition = -1;

    protected Animation slide_in_left;
    protected Animation slide_out_right;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (Listener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_main, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        mSelectedPosition = -1;
        mSubscriptions = new CompositeSubscription();
        List<MovieEntity> restoredMovies = new ArrayList<MovieEntity>();

        if(bundle != null) {
            restoredMovies = bundle.getParcelableArrayList(EXTRA_STATE_MOVIES);
            mSelectedPosition = bundle.getInt(EXTRA_STATE_SELECTED_POSITION, -1);
        }

        mMoviesAdapter = new MoviesAdapter(this, restoredMovies);
        mMoviesAdapter.setListener(this);

        initRefreshLayout();
        initRecyclerView();

        HashMap<RecyclerViewStates, Integer> statesViewMap = new HashMap<RecyclerViewStates, Integer>();
        statesViewMap.put(RecyclerViewStates.LOADING, R.id.recycler_view_loading);
        statesViewMap.put(RecyclerViewStates.NORMAL, R.id.movies_recycler_view);
        statesViewMap.put(RecyclerViewStates.EMPTY, R.id.recycler_view_empty);
        statesViewMap.put(RecyclerViewStates.ERROR, R.id.recycler_view_error);
        mRecyclerViewStateWrapper.setViewMap(statesViewMap);

        slide_in_left = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);

        mRecyclerViewStateWrapper.setInAnimation(slide_in_left);
        mRecyclerViewStateWrapper.setOutAnimation(slide_out_right);
    }

    protected void initRefreshLayout() {
        mSwipeRefreshLayout.setScrollUpCallback(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_progress_colors));
    }

    @CallSuper
    protected void initRecyclerView() {
        int spanCount = getResources().getInteger(R.integer.list_view_columns);
        mGridLayoutManager = new GridLayoutManager(getActivity(), spanCount);

        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (mMoviesAdapter.isLoadMore(position))
                    return mGridLayoutManager.getSpanCount();
                return 1;
            }
        });

        mRecyclerView.setAdapter(mMoviesAdapter);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        if (mSelectedPosition != -1)
            mRecyclerView.scrollToPosition(mSelectedPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle bandle) {
        super.onSaveInstanceState(bandle);

        bandle.putInt(EXTRA_STATE_SELECTED_POSITION, mSelectedPosition);
        bandle.putParcelableArrayList(EXTRA_STATE_MOVIES, new ArrayList<MovieEntity>(mMoviesAdapter.getItems()));
    }

    @Override
    public void onDestroyView() {
        mSubscriptions.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mListener = (movie, view) -> {};
        mMoviesAdapter.setListener(IOnMovieItemClickListener.EMPTY);
        super.onDetach();
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        if(mRecyclerView != null && ViewCompat.canScrollVertically(mRecyclerView, -1))
            return true;

        if(mRecyclerViewStateWrapper != null && mRecyclerViewStateWrapper.isLoading())
            return true;

        return false;
    }

    public void goToTop(boolean smooth) {
        if (smooth)
            mRecyclerView.smoothScrollToPosition(0);
        else
            mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onClicked(MovieEntity entity, View view, int position) {
        mSelectedPosition = position;

        if(UtilityHelper.existInFavorites(getActivity(), entity))
            entity.setFavorite(true);

        mListener.onMovieSelected(entity, view);
    }

    @Override
    protected void setupComponent(AppComponent component) {
        component.inject(this);
    }
}
