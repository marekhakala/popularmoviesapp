package com.nothing.mark.popmoviesapp.UI.Widgets;

public enum RecyclerViewStates {
    LOADING(1),
    NORMAL(2),
    EMPTY(3),
    ERROR(4),
    UNKNOWN(0);

    protected final int value;

    RecyclerViewStates(int value) {
        this.value = value;
    }

    public int toInt() {
        return this.value;
    }

    @Override public String toString() {
        return String.valueOf(this.value);
    }

    public static RecyclerViewStates fromString(String value) {
        int intValue = Integer.parseInt(value.toLowerCase());

        if(value != null) {
            if(intValue == LOADING.value)
                return LOADING;
            else if(intValue == NORMAL.value)
                return NORMAL;
            else if(intValue == EMPTY.value)
                return EMPTY;
            else if(intValue == ERROR.value)
                return ERROR;
        }

        return UNKNOWN;
    }
}
