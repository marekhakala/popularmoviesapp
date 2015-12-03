package com.nothing.mark.popmoviesapp.UI.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nothing.mark.popmoviesapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
    http://stackoverflow.com/questions/2695646/declaring-a-custom-android-ui-element-using-xml
*/
public final class RecyclerViewStateView extends LinearLayout {
    protected View mLayout;

    @Bind(R.id.recycler_view_state_text) TextView mTextView;
    @Bind(R.id.recycler_view_state_icon) ImageView mImageView;
    @Bind(R.id.recycler_view_state_button) Button mImageButton;

    private RecyclerViewStateView(Context context) {
        super(context, null, 0);
        initialize(context, null, 0);
    }

    public RecyclerViewStateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        initialize(context, attributeSet, 0);
    }

    private RecyclerViewStateView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        initialize(context, attributeSet, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        mLayout = LayoutInflater.from(context).inflate(R.layout.recycler_view_state, this, true);
        ButterKnife.bind(this, mLayout);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewStateView, defStyleAttr, 0);
        mImageView.setImageDrawable(typedArray.getDrawable(R.styleable.RecyclerViewStateView_messageIcon));
        mTextView.setText(typedArray.getString(R.styleable.RecyclerViewStateView_messageText));
        mImageButton.setVisibility(typedArray.getBoolean(R.styleable.RecyclerViewStateView_messageButton, false) ? View.VISIBLE : View.GONE);
        typedArray.recycle();
    }

    public void setMessageIcon(Drawable icon) {
        mImageView.setImageDrawable(icon);
    }

    public void setMessageText(CharSequence text) {
        mTextView.setText(text);
    }

    public void setMessageButton(boolean value) {
        mImageButton.setVisibility(value ? View.VISIBLE : View.GONE);
    }
}
