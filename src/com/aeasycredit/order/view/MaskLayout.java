package com.aeasycredit.order.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MaskLayout extends LinearLayout {
    
    public MaskLayout(Context context) {
        super(context);
    }

    public MaskLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
//        return super.onInterceptTouchEvent(ev);
    }
    
}
