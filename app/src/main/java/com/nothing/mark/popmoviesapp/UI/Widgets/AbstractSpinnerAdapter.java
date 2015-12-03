package com.nothing.mark.popmoviesapp.UI.Widgets;

import android.widget.BaseAdapter;
import com.nothing.mark.popmoviesapp.API.ModeOrder;
import java.util.ArrayList;

public abstract class AbstractSpinnerAdapter extends BaseAdapter {
    protected ArrayList<ModeSpinnerItem> mItems = new ArrayList<ModeSpinnerItem>();

    protected AbstractSpinnerAdapter() {}

    public void add(String title, ModeOrder mode) {
        ModeSpinnerItem item = new ModeSpinnerItem(title, mode);
        mItems.add(item);
    }

    public void remove(String mode) {
        for (ModeSpinnerItem item : mItems)
            if(item.getTitle().toLowerCase().equals(mode.toLowerCase()))
                mItems.remove(item);
    }

    public void clear() {
        mItems.clear();
    }

    @Override
    public Object getItem(int index) {
        return mItems.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}
