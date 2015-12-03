package com.nothing.mark.popmoviesapp.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

import com.nothing.mark.popmoviesapp.API.ModeOrder;
import com.nothing.mark.popmoviesapp.Model.MovieEntity;
import com.nothing.mark.popmoviesapp.R;
import com.nothing.mark.popmoviesapp.UI.Fragments.AbstractBaseFragment;
import com.nothing.mark.popmoviesapp.UI.Fragments.FavoriteMoviesListFragment;
import com.nothing.mark.popmoviesapp.UI.Fragments.DetailMovieFragment;
import com.nothing.mark.popmoviesapp.UI.Fragments.MoviesListFragment;
import com.nothing.mark.popmoviesapp.UI.Fragments.SortedMoviesListFragment;
import com.nothing.mark.popmoviesapp.UI.Widgets.AbstractSpinnerAdapter;
import com.nothing.mark.popmoviesapp.UI.Widgets.ModeSpinnerItem;
import com.nothing.mark.popmoviesapp.Utilities.ConstantValues;
import com.nothing.mark.popmoviesapp.Utilities.UtilityHelper;

public class MainActivity extends AppCompatActivity implements MoviesListFragment.Listener {
    @Bind(R.id.toolbar) Toolbar mToolbar;

    protected static final String EXTRA_STATE_MODE = "mode_order";

    public static final String MOVIES_MAIN_FRAGMENT_TAG = "fragment_main";
    public static final String MOVIE_DETAILS_FRAGMENT_TAG = "fragment_detail_movie";

    protected AbstractSpinnerAdapter mSpinnerAdapter = new ModeSpinnerAdapter();

    protected ModeOrder mMode;
    protected boolean mTwoPane;
    protected MoviesListFragment mMoviesFragment;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.movie_detail_container) != null)
            mTwoPane = true;
        else
            mTwoPane = false;

        if(bundle != null)
            mMode = ModeOrder.fromString(bundle.getString(EXTRA_STATE_MODE, ModeOrder.POPULARITY.toString()));
        else
            mMode = ModeOrder.fromString(UtilityHelper.getModeOrder(this));

        initModeSpinner();
    }

    @Override
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        mMoviesFragment = (MoviesListFragment) getSupportFragmentManager().findFragmentByTag(MOVIES_MAIN_FRAGMENT_TAG);

        if (mMoviesFragment == null) {
            if(mMode == ModeOrder.FAVOURITE)
                replaceFragment(new FavoriteMoviesListFragment());
            else
                replaceFragment(SortedMoviesListFragment.newInstance(mMode));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_go_to_top:
                mMoviesFragment.goToTop(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(EXTRA_STATE_MODE, mMode.toString());
    }

    @Override
    protected void onPause() {
        UtilityHelper.setModeOrder(this, mMode.toString());
        super.onPause();
    }

    @Override
    public void onMovieSelected(MovieEntity entity, View view) {
        Timber.d("Movie " + entity.getTitle() + "[" + entity.getId() + "] was selected.");

        if (mTwoPane)
            replaceFragment(DetailMovieFragment.newInstance(entity));
        else {
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra(ConstantValues.EXTRA_MOVIE, entity);
            startActivity(intent);
        }
    }

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

    protected void replaceFragment(AbstractBaseFragment fragment) {
        if(fragment instanceof MoviesListFragment) {
            mMoviesFragment = (MoviesListFragment) fragment;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_list_container, fragment, MOVIES_MAIN_FRAGMENT_TAG).commit();
        } else if(fragment instanceof DetailMovieFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIE_DETAILS_FRAGMENT_TAG).commit();
        }
    }

    protected void initModeSpinner() {
        Toolbar toolbar = getToolbar();

        if (toolbar == null)
            return;

        mSpinnerAdapter.clear();
        mSpinnerAdapter.add(getString(R.string.mode_order_favorites), ModeOrder.FAVOURITE);
        mSpinnerAdapter.add(getString(R.string.mode_order_popularity), ModeOrder.POPULARITY);
        mSpinnerAdapter.add(getString(R.string.mode_order_vote_average), ModeOrder.HIGHEST);

        int itemToSelect = 0;

        if(mMode.equals(ModeOrder.FAVOURITE))
            itemToSelect = 0;
        else if(mMode.equals(ModeOrder.POPULARITY))
            itemToSelect = 1;
        else if(mMode.equals(ModeOrder.HIGHEST))
            itemToSelect = 2;

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        View spinnerContainer = LayoutInflater.from(this)
                .inflate(R.layout.toolbar_spinner, toolbar, false);
        toolbar.addView(spinnerContainer, layoutParams);

        Spinner spinner = (Spinner) spinnerContainer.findViewById(R.id.mode_spinner);
        spinner.setAdapter(mSpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int position, long itemId) {
                ModeSpinnerItem item = (ModeSpinnerItem) mSpinnerAdapter.getItem(position);

                if (item != null)
                    onModeSelected(item.getMode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        if (itemToSelect > -1)
            spinner.setSelection(itemToSelect);
    }

    protected void onModeSelected(ModeOrder mode) {
        if (mMode == mode)
            return;

        mMode = mode;

        if (mMode == ModeOrder.FAVOURITE)
            replaceFragment(new FavoriteMoviesListFragment());
        else
            replaceFragment(SortedMoviesListFragment.newInstance(mMode));
    }

    public class ModeSpinnerAdapter extends AbstractSpinnerAdapter {

        protected View checkView(View view, ViewGroup parent, int resource, String viewState) {
            String state = "";

            if(view != null)
                state = (String) view.getTag();

            if (state.isEmpty() || !state.equals(viewState.toLowerCase())) {
                view = getLayoutInflater().inflate(resource, parent, false);
                view.setTag(viewState.toLowerCase());
            }

            return view;
        }

        @Override
        public View getDropDownView(int index, View view, ViewGroup parent) {
            view = checkView(view, parent, R.layout.item_order_dropdown, "DropDown");

            TextView normalText = (TextView) view.findViewById(android.R.id.text1);
            normalText.setVisibility(View.VISIBLE);

            ModeSpinnerItem item = (ModeSpinnerItem) getItem(index);

            if(item != null)
                normalText.setText(item.getTitle());

            return view;
        }

        @Override
        public View getView(int index, View view, ViewGroup parent) {
            view = checkView(view, parent, R.layout.item_order, "NonDropDown");

            TextView normalText = (TextView) view.findViewById(android.R.id.text1);
            ModeSpinnerItem item = (ModeSpinnerItem) getItem(index);

            if(item != null)
                normalText.setText(item.getTitle());

            return view;
        }
    }
}
