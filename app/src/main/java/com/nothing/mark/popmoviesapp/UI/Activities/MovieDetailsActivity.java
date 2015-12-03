package com.nothing.mark.popmoviesapp.UI.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nothing.mark.popmoviesapp.Model.MovieEntity;
import com.nothing.mark.popmoviesapp.R;
import com.nothing.mark.popmoviesapp.UI.Fragments.DetailMovieFragment;
import com.nothing.mark.popmoviesapp.Utilities.ConstantValues;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    @Bind(R.id.toolbar) Toolbar mToolbar;

    protected static final String MOVIE_DETAIL_FRAGMENT_TAG = "fragment_detail_movie";

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setupToolbar();
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    protected void setupToolbar() {
        if (mToolbar == null)
            return;

        ViewCompat.setElevation(mToolbar, getResources().getDimension(R.dimen.toolbar_elevation));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_detail_movie);

        if (mToolbar != null) {
            ViewCompat.setElevation(mToolbar, getResources().getDimension(R.dimen.toolbar_elevation));
            mToolbar.setNavigationOnClickListener(view -> finish());

            ActionBar ab = getSupportActionBar();

            if (ab != null) {
                ab.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
                ab.setDisplayShowHomeEnabled(true);
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }

        MovieEntity movie = getIntent().getParcelableExtra(ConstantValues.EXTRA_MOVIE);

        if (bundle == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,
                            DetailMovieFragment.newInstance(movie), MOVIE_DETAIL_FRAGMENT_TAG).commit();
        }
    }
}
