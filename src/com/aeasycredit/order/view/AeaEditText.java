
package com.aeasycredit.order.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class AeaEditText extends EditText {

    float initX;
    float initY;
    float curY;

    public AeaEditText(Context context) {
        super(context);
        init();
    }

    public AeaEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    // // return super.onTouchEvent(event);
    // this.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
    // switch (event.getAction() & MotionEvent.ACTION_MASK) {
    // case MotionEvent.ACTION_UP:
    // this.getParent().requestDisallowInterceptTouchEvent(false);
    // break;
    // }
    // return false;
    // }

    public void init() {
        this.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("NewApi")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // if(getScrollY() == 0 && !canScrollVertically(1)){
                // Log.i(AeaConstants.TAG, !canScrollVertically(1)+"");
                // }
                // if (MotionEvent.ACTION_DOWN == event.getAction()) {
                // initY = event.getY();
                // } else if (MotionEvent.ACTION_MOVE == event.getAction()) {
                // curY = event.getY();
                // float direction = event.getY() - initY;
                // if (direction >= 0) {
                // if (canScrollVertically(1)) {
                // v.getParent().getParent().getParent()
                // .requestDisallowInterceptTouchEvent(true);
                // } else {
                // v.getParent().getParent().getParent()
                // .requestDisallowInterceptTouchEvent(false);
                // }
                // } else {
                // if (canScrollVertically(-1)) {
                // v.getParent().getParent().getParent()
                // .requestDisallowInterceptTouchEvent(true);
                // } else {
                // v.getParent().getParent().getParent()
                // .requestDisallowInterceptTouchEvent(false);
                // }
                // }
                // initY = curY;
                // }

                // if( || canScrollVertically(-1)){

                // }else if(!canScrollVertically(1) ||
                // !canScrollVertically(-1)){
                // }else {
                // v.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(false);
                // }
                if (v.hasFocus()) {
                    v.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                }
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }
}
