package com.nothing.mark.popmoviesapp.UI;

import com.nothing.mark.popmoviesapp.UI.Fragments.DetailMovieFragment;
import com.nothing.mark.popmoviesapp.UI.Fragments.MoviesListFragment;

public interface UiComponent {
    void inject(MoviesListFragment fragment);
    void inject(DetailMovieFragment fragment);
}
