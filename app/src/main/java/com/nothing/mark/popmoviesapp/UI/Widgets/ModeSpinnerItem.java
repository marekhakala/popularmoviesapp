package com.nothing.mark.popmoviesapp.UI.Widgets;

import com.nothing.mark.popmoviesapp.API.ModeOrder;

public class ModeSpinnerItem {
    String mTitle;
    ModeOrder mMode;

    public ModeSpinnerItem(String title, ModeOrder mode) {
        this.mTitle = title;
        this.mMode = mode;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public ModeOrder getMode() {
        return this.mMode;
    }
}