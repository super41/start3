package com.example.userversion.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

/**
 * Created by XJP on 2017/12/4.
 */
public class MyAutoCompleteTextView extends AutoCompleteTextView {
    public MyAutoCompleteTextView(Context context) {
        super(context);
    }
    public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean enoughToFilter() {
        return true;
    }
    @Override
    protected void onFocusChanged(boolean focused,int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction,previouslyFocusedRect);
        if(!TextUtils.isEmpty(getText().toString())&& getAdapter()!= null) {
            performFiltering(getText(), KeyEvent.KEYCODE_UNKNOWN);
        }
    }
}