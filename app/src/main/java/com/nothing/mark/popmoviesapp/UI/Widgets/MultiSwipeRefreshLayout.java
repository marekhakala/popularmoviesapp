package com.nothing.mark.popmoviesapp.UI.Widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class MultiSwipeRefreshLayout extends SwipeRefreshLayout {
    protected IScrollUpCallback mCanScrollUpCallback;

    public interface IScrollUpCallback {
        boolean canSwipeRefreshChildScrollUp();
    }

    public MultiSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public MultiSwipeRefreshLayout(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public void setScrollUpCallback(IScrollUpCallback callback) {
        mCanScrollUpCallback = callback;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mCanScrollUpCallback != null)
            return mCanScrollUpCallback.canSwipeRefreshChildScrollUp();

        return super.canChildScrollUp();
    }
}
