package com.nothing.mark.popmoviesapp.API;

import java.io.Serializable;

public enum ModeOrder implements Serializable {

    POPULARITY("popularity.desc"),
    HIGHEST("vote_average.desc"),
    FAVOURITE("favourites"),
    UNKNOWN("unknown");

    protected final String value;

    ModeOrder(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return this.value;
    }

    public static ModeOrder fromString(String value) {
        String lowerValue = value.toLowerCase();

        if(value != null) {
            if(lowerValue.equals(POPULARITY.value))
                return POPULARITY;
            else if(lowerValue.equals(HIGHEST.value))
                return HIGHEST;
            else if(lowerValue.equals(FAVOURITE.value))
                return FAVOURITE;
        }

        return UNKNOWN;
    }
}
