package com.nothing.mark.popmoviesapp.UI.Widgets;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    public interface OnLoadMoreCallback {
        void onLoadMore(int page, int totalItemsCount);
    }

    protected int mVisibleThreshold = 5;
    protected int mCurrentPage = 0;
    protected int mPreviousTotalItemCount = 0;
    protected boolean mLoading = true;
    protected int mStartingPageIndex = 0;

    protected OnLoadMoreCallback mCallback;

    public EndlessScrollListener() {}

    public EndlessScrollListener(int visibleThreshold) {
        this.mVisibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int startPage) {
        this.mVisibleThreshold = visibleThreshold;
        this.mStartingPageIndex = startPage;
        this.mCurrentPage = startPage;
    }

    public EndlessScrollListener setCallback(OnLoadMoreCallback callback) {
        mCallback = callback;
        return this;
    }

    public void onScrolled(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount < mPreviousTotalItemCount) {
            mCurrentPage = this.mStartingPageIndex;
            mPreviousTotalItemCount = totalItemCount;

            if (totalItemCount == 0)
                this.mLoading = true;
        }

        if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
            ++mCurrentPage;
        }

        if (!mLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold)) {
            onLoadMore(mCurrentPage + 1, totalItemCount);
            mLoading = true;
        }
    }

    @Override
    public abstract void onScrolled(RecyclerView recyclerView, int dx, int dy);

    public void onLoadMore(int page, int totalItemsCount) {
        if (mCallback != null)
            mCallback.onLoadMore(page, totalItemsCount);
    }

    public static EndlessScrollListener fromGridLayoutManager(GridLayoutManager layoutManager,
                                                              int visibleThreshold, int startPage) {

        return new EndlessScrollListener(visibleThreshold, startPage) {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 1)
                    return;

                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int visibleItemCount = lastVisibleItem - firstVisibleItem;
                int totalItemCount = layoutManager.getItemCount();

                onScrolled(firstVisibleItem, visibleItemCount, totalItemCount);
            }
        };
    }
}
