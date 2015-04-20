package com.aeasycredit.order.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class AeaScrollView extends ScrollView {
    private float yDistance;
    private float yLast;
    
    public AeaScrollView(Context context){
        super(context);
    }

    public AeaScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//            switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                    yDistance = 0f;
//                    yLast = ev.getY();
//                    break;
//            case MotionEvent.ACTION_MOVE:
//                    final float curY = ev.getY();
//
//                    yDistance += Math.abs(curY - yLast);
//                    yLast = curY;
//
//                    if (yDistance) {
//                            return false;   //表示向下传递事件
//                    }
//            }
//
//            return super.onInterceptTouchEvent(ev);
//    }
}
