package com.nothing.mark.popmoviesapp.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nothing.mark.popmoviesapp.Model.MovieEntity;
import com.nothing.mark.popmoviesapp.R;
import com.nothing.mark.popmoviesapp.Utilities.ConstantValues;
import com.nothing.mark.popmoviesapp.Utilities.UtilityHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public final class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Action1<List<MovieEntity>> {
    protected Context mContext;
    protected Fragment mFragment;
    protected IOnMovieItemClickListener mListener;

    protected boolean mLoadMore = false;
    protected LayoutInflater mInflater;
    protected List<MovieEntity> mMoviesList;

    public interface ViewType {
        int LOADING = 0;
        int NORMAL = 1;
    }

    public MoviesAdapter(Fragment fragment, List<MovieEntity> moviesList) {
        mFragment = fragment;
        mContext = fragment.getActivity();

        mMoviesList = moviesList;
        mInflater = LayoutInflater.from(mContext);
        setHasStableIds(true);
    }

    public void setListener(IOnMovieItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public long getItemId(int index) {
        if(isLoadMore(index))
            return -1;

        return mMoviesList.get(index).getId();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int index) {
        if (holder instanceof MovieHolder && holder.getItemViewType() == ViewType.NORMAL) {
            MovieHolder viewHolder = (MovieHolder) holder;
            MovieEntity entity = mMoviesList.get(index);

            viewHolder.mThumbnailTitle.setText(entity.getTitle());
            viewHolder.mContainer.setOnClickListener(view -> mListener.onClicked(entity, view, holder.getAdapterPosition()));

            String sizePath = UtilityHelper.posterSizePath(UtilityHelper.findPosterSize(UtilityHelper.getScreenSizeWidth(mContext)));
            Uri posterUri = entity.getPosterUri(ConstantValues.TMDB_IMAGE_URL, sizePath);
            Picasso.with(mContext).load(posterUri).into(viewHolder.mThumbnailPoster);
        }
    }

    public void setLoadMore(boolean enabled) {
        if (mLoadMore != enabled) {
            if (mLoadMore)
                notifyItemRemoved(getItemCount());
            else
                notifyItemInserted(getItemCount());

            mLoadMore = !mLoadMore;
        }
    }

    public boolean isLoadMore() {
        return mLoadMore;
    }

    public boolean isLoadMore(int index) {
        if(mLoadMore && (index == (getItemCount() - 1)))
            return true;
        return false;
    }

    protected int loadMoreToInt() {
        if(mLoadMore)
            return 1;
        return 0;
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size() + loadMoreToInt();
    }

    @Override
    public int getItemViewType(int index) {
        if(isLoadMore(index))
            return ViewType.LOADING;

        return ViewType.NORMAL;
    }

    @Override
    public void call(List<MovieEntity> moviesList) {
        add(moviesList);
    }

    public void set(List<MovieEntity> moviesList) {
        mMoviesList = moviesList;
        notifyDataSetChanged();
    }

    public void add(List<MovieEntity> moviesList) {
        if (moviesList.size() > 0) {
            mMoviesList.addAll(moviesList);
            notifyItemRangeInserted(mMoviesList.size(), moviesList.size());
        }
    }

    public List<MovieEntity> getItems() {
        return mMoviesList;
    }

    public MovieEntity getItem(int index) {
        if(!isLoadMore(index))
            return mMoviesList.get(index);
        return null;
    }

    public void clear() {
        if(mMoviesList.size() > 0) {
            mMoviesList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case ViewType.LOADING:
                return new RecyclerView.ViewHolder(mInflater.inflate(R.layout.item_movie_loading, parent, false)) {};
            default:
                return new MovieHolder(mInflater.inflate(R.layout.item_movie, parent, false));
        }
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.movie_content_container) View mContainer;
        @Bind(R.id.movie_item_title) TextView mThumbnailTitle;
        @Bind(R.id.movie_item_poster) ImageView mThumbnailPoster;

        public MovieHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}