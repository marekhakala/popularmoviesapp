package com.nothing.mark.popmoviesapp.UI.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewAnimator;

import java.util.HashMap;

/*
    http://sampleprogramz.com/android/viewanimator.php
*/
public class RecyclerViewStatesWrapper extends ViewAnimator {
    protected RecyclerViewStates mCurrentState = RecyclerViewStates.UNKNOWN;
    protected HashMap<RecyclerViewStates, Integer> mStatesViewMap = new HashMap<RecyclerViewStates, Integer>();

    public RecyclerViewStatesWrapper(Context context, AttributeSet attributesSet) {
        super(context, attributesSet);
    }

    public void setViewMap(HashMap<RecyclerViewStates, Integer> statesViewMap) {
        mStatesViewMap = statesViewMap;
        setState(RecyclerViewStates.NORMAL);
    }

    public void setState(RecyclerViewStates state) {
        if(state.equals(mCurrentState))
            return;

        if(!mStatesViewMap.containsKey(state))
            throw new IllegalArgumentException("No view for state: " + state.toString());
        else {
            int stateViewIndex = getIndexOfView(mStatesViewMap.get(state));
            this.mCurrentState = state;

            if(stateViewIndex == -1)
                throw new IllegalArgumentException("No view for state: " + state.toString());

            setDisplayedChild(stateViewIndex);
        }
    }

    public RecyclerViewStates getState() {
        return this.mCurrentState;
    }

    public boolean isLoading() {
        return (mCurrentState == RecyclerViewStates.LOADING);
    }

    protected int getIndexOfView(int id) {
        for(int i = 0; i < getChildCount(); ++i) {
            if(this.getChildAt(i).getId() == id)
                return i;
        }

        return -1;
    }
}
