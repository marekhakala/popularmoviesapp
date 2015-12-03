package com.nothing.mark.popmoviesapp.UI.Fragments;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nothing.mark.popmoviesapp.AppComponent;
import com.nothing.mark.popmoviesapp.Model.MovieEntity;
import com.nothing.mark.popmoviesapp.Model.ReviewEntity;
import com.nothing.mark.popmoviesapp.Model.TrailerEntity;
import com.nothing.mark.popmoviesapp.R;
import com.nothing.mark.popmoviesapp.Repository.IMoviesRepository;
import com.nothing.mark.popmoviesapp.UI.Activities.MovieDetailsActivity;
import com.nothing.mark.popmoviesapp.Utilities.ConstantValues;
import com.nothing.mark.popmoviesapp.Utilities.UtilityHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.Bind;
import butterknife.BindColor;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;
import static butterknife.ButterKnife.findById;

public class DetailMovieFragment extends AbstractBaseFragment implements ObservableScrollViewCallbacks {
    Toolbar mToolbar;

    private static final String EXTRA_STATE_SCROLL_VIEW = "scroll_view";
    private static final String EXTRA_STATE_REVIEWS = "reviews";
    private static final String EXTRA_STATE_TRAILERS = "trailers";

    @Bind(R.id.movie_detail_view) ObservableScrollView mScrollView;

    @Bind(R.id.movie_poster_image) ImageView mPosterImage;
    @Bind(R.id.movie_backdrop_image) ImageView mBackdropImage;
    @Bind(R.id.movie_backdrop_container) FrameLayout mBackdropContainer;

    @Bind(R.id.movie_title) TextView mTitle;
    @Bind(R.id.movie_release_date) TextView mReleaseDate;
    @Bind(R.id.movie_rating) RatingBar mRating;
    @Bind(R.id.movie_overview) TextView mOverview;

    @Bind(R.id.movie_reviews_container) ViewGroup mReviewsGroup;
    @Bind(R.id.movie_trailers_container) ViewGroup mTrailersGroup;

    @BindColor(R.color.primary) int mColorPrimary;
    @BindColor(R.color.text_white) int mColorWhite;

    @Inject IMoviesRepository mMoviesRepository;

    protected MovieEntity mMovieEntity;
    protected CompositeSubscription mSubscriptions;

    protected List<ReviewEntity> mReviews;
    protected List<TrailerEntity> mTrailers;

    protected Handler mHandler;

    protected MenuItem mMenuShare;
    protected MenuItem mMenuFavorite;

    public static DetailMovieFragment newInstance(MovieEntity entity) {
        Bundle args = new Bundle();
        args.putParcelable(ConstantValues.EXTRA_MOVIE, entity);

        DetailMovieFragment fragment = new DetailMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mHandler = new Handler();
        return inflater.inflate(R.layout.fragment_detail_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        if (getActivity() instanceof MovieDetailsActivity)
            mToolbar = ((MovieDetailsActivity) getActivity()).getToolbar();

        mScrollView.setScrollViewCallbacks(this);

        if (bundle != null) {
            mTrailers = bundle.getParcelableArrayList(EXTRA_STATE_TRAILERS);
            mReviews = bundle.getParcelableArrayList(EXTRA_STATE_REVIEWS);
            mScrollView.onRestoreInstanceState(bundle.getParcelable(EXTRA_STATE_SCROLL_VIEW));
        }
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mSubscriptions = new CompositeSubscription();

        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
        onMovieLoaded(getArguments().getParcelable(ConstantValues.EXTRA_MOVIE));

        if (mReviews != null)
            onReviewsLoaded(mReviews);
        else
            loadReviews();

        if (mTrailers != null)
            onTrailersLoaded(mTrailers);
        else
            loadTrailers();

        mSubscriptions.add(ConstantValues.FAVORITE_MOVIES_SUBJECT.asObservable()
                .filter(event -> ((mMovieEntity != null)
                        && (mMovieEntity.getId() == event.getId())))
                .subscribe(movie -> {
                    mMovieEntity.setFavorite(movie.mFavorite);
                    setFavoriteButton(movie.mFavorite);
                }));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_movie, menu);
        mMenuShare = menu.findItem(R.id.menu_share);
        mMenuFavorite = menu.findItem(R.id.menu_favorite);

        if(mMovieEntity != null && mMenuFavorite != null)
            setFavoriteButton(mMovieEntity.isFavorite());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menu_share:
                UtilityHelper.shareMovie(getActivity(), mMovieEntity);
                break;
            case R.id.menu_favorite:
                if(mMovieEntity != null && UtilityHelper.addMovieToFavorites(getActivity(),
                        mMoviesRepository, mMovieEntity, !mMovieEntity.isFavorite()))
                    showToast(R.string.message_movie_saved_to_favorites);
                break;
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(EXTRA_STATE_SCROLL_VIEW, mScrollView.onSaveInstanceState());

        if (mReviews != null)
            bundle.putParcelableArrayList(EXTRA_STATE_REVIEWS, new ArrayList<ReviewEntity>(mReviews));

        if (mTrailers != null)
            bundle.putParcelableArrayList(EXTRA_STATE_TRAILERS, new ArrayList<TrailerEntity>(mTrailers));
    }

    @Override
    public void onDestroyView() {
        mSubscriptions.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        ViewCompat.setTranslationY(mBackdropContainer, scrollY / 2);

        if (mToolbar != null) {
            int parallaxImageHeight = mBackdropContainer.getMeasuredHeight();
            float alpha = Math.min(1, (float) scrollY / parallaxImageHeight);

            mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, mColorPrimary));
            mToolbar.setTitleTextColor(ScrollUtils.getColorWithAlpha(alpha, mColorWhite));
        }
    }

    @Override
    public void onDownMotionEvent() {}

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {}

    protected void onMovieLoaded(MovieEntity movie) {
        mMovieEntity = movie;

        if (mToolbar != null)
            mToolbar.setTitle(mMovieEntity.getTitle());

        mTitle.setText(movie.getTitle());
        mRating.setRating(movie.getVoteAverage());
        mReleaseDate.setText(UtilityHelper.getReleaseDate(movie.getReleaseDate()));
        mOverview.setText(movie.getOverview());
        setFavoriteButton(movie.isFavorite());

        String sizePath = UtilityHelper.posterSizePath(UtilityHelper.findPosterSize(UtilityHelper.getScreenSizeWidth(getActivity())));

        Uri uri_poster = movie.getPosterUri(ConstantValues.TMDB_IMAGE_URL, sizePath);
        Picasso.with(getActivity()).load(uri_poster).into(mPosterImage);

        Uri uri_backdrop = movie.getBackdropUri(ConstantValues.TMDB_IMAGE_URL, sizePath);
        Picasso.with(getActivity()).load(uri_backdrop).into(mBackdropImage);

        ratingsColorSchema();
    }

    protected void loadReviews() {
        mSubscriptions.add(mMoviesRepository.reviews(mMovieEntity.getId())
                .subscribe(reviews -> {
                    Timber.d("Repository> Reviews loading was finished. Size: " + String.valueOf(reviews.size()));
                    onReviewsLoaded(reviews);
                }, throwable -> {
                    Timber.e(throwable, "Repository> Reviews loading has failed.");
                    onReviewsLoaded(null);
                }));
    }

    protected void onReviewsLoaded(List<ReviewEntity> reviewsList) {
        mReviews = reviewsList;
        UtilityHelper.clearViewGroup(mHandler, mReviewsGroup);

        int visibility = View.GONE;
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        if (reviewsList != null && reviewsList.size() > 0) {
            for (ReviewEntity review : reviewsList) {
                if (TextUtils.isEmpty(review.getAuthor()))
                    continue;

                View reviewView = inflater.inflate(R.layout.item_review_detail, mReviewsGroup, false);
                TextView reviewAuthorView = findById(reviewView, R.id.review_author);
                TextView reviewTextView = findById(reviewView, R.id.review_text);

                reviewAuthorView.setText(review.getAuthor());
                reviewTextView.setText(review.getContent());

                visibility = View.VISIBLE;
                UtilityHelper.addViewGroup(mHandler, reviewView, mReviewsGroup);
            }
        }

        UtilityHelper.setVisibilityForViewGroup(mHandler, visibility, mReviewsGroup);
    }

    protected void loadTrailers() {
        mSubscriptions.add(mMoviesRepository.trailers(mMovieEntity.getId()).subscribe(trailers -> {
            Timber.d("Repository> Trailers loading was finished. Size: " + String.valueOf(trailers.size()));
            onTrailersLoaded(trailers);
        }, throwable -> {
            Timber.e(throwable, "Repository> Trailers loading has failed.");
            onTrailersLoaded(null);
        }));
    }

    protected void onTrailersLoaded(List<TrailerEntity> trailersList) {
        mTrailers = trailersList;
        UtilityHelper.clearViewGroup(mHandler, mTrailersGroup);

        int visibility = View.GONE;
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        if (trailersList != null && trailersList.size() > 0) {
            for (TrailerEntity trailer : trailersList) {
                View trailerView = inflater.inflate(R.layout.item_trailer_detail, mTrailersGroup, false);
                TextView trailerNameView = findById(trailerView, R.id.trailer_title);

                trailerView.setTag(trailer);
                trailerNameView.setText(trailer.getSite() + ": " + trailer.getName());

                trailerView.setOnClickListener(view -> {
                    UtilityHelper.playTrailer(getActivity(), (TrailerEntity) view.getTag());
                });

                UtilityHelper.addViewGroup(mHandler, trailerView, mTrailersGroup);
                visibility = View.VISIBLE;
            }
        }

        UtilityHelper.setVisibilityForViewGroup(mHandler, visibility, mTrailersGroup);
    }

    protected void ratingsColorSchema() {
        LayerDrawable stars = (LayerDrawable) mRating.getProgressDrawable();

        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.star_fully_selected), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.star_partially_selected), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.star_not_selected), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected void setupComponent(AppComponent component) {
        component.inject(this);
    }

    public void setFavoriteButton(boolean state) {
        if(mMenuFavorite == null)
            return;

        if(state)
            UtilityHelper.setStarIcon(mHandler, mMenuFavorite, R.drawable.ic_star);
        else
            UtilityHelper.setStarIcon(mHandler, mMenuFavorite, R.drawable.ic_star_border);
    }
}
