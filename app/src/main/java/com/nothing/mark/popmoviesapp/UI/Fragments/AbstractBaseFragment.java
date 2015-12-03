package com.nothing.mark.popmoviesapp.UI.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.nothing.mark.popmoviesapp.AppComponent;
import com.nothing.mark.popmoviesapp.PopularMoviesApplication;

import butterknife.ButterKnife;

public abstract class AbstractBaseFragment extends Fragment {
    protected Toast mToast;

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        setupComponent((AppComponent) PopularMoviesApplication.get(getActivity()).getComponent());
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected abstract void setupComponent(AppComponent component);

    protected void clearToast() {
        if (mToast != null)
            mToast.cancel();
    }

    protected void showToast(int resourceId) {
        clearToast();
        mToast = Toast.makeText(getActivity(), resourceId, Toast.LENGTH_SHORT);
        mToast.show();
    }
}