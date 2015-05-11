package com.aeasycredit.order.activitys;

import com.actionbarsherlock.app.SherlockActivity;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.volley.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

public abstract class BaseActivity extends SherlockActivity implements Response.Listener<RequestWrapper>,Response.ErrorListener{
    View mLoadingView;

    public void startLoadingStatus() {
        // if(mMainView != null){
        // mMainView.startLoadingStatus(hiddenMarginTop);
        // }

        android.widget.LinearLayout.LayoutParams marginParams = null;
        if (mLoadingView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            mLoadingView = inflater.inflate(com.aeasycredit.order.R.layout.loading_mask,
                    null);
            mLoadingView.setClickable(true);
            // mLoadingView.setLayoutParams(new
            // LayoutParams(LayoutParams.MATCH_PARENT,
            // LayoutParams.MATCH_PARENT));
            // TextView tv = (TextView)
            // mLoadingView.findViewById(Res.id("common_mask_tips);
            // tv.setText(getString(R.string.wht_tm_str_empty_result_tip));
            this.addContentView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }
//        if (hiddenMarginTop.length >= 1 && hiddenMarginTop[0] == true) {
//            View view = mLoadingView.findViewById(com.aeasycredit.order.R.id.loading_body);
//            marginParams = (android.widget.LinearLayout.LayoutParams)
//                    view.getLayoutParams();
//            marginParams.topMargin = 0;
//            view.setLayoutParams(marginParams);
//        }
        if (mLoadingView.getVisibility() != View.VISIBLE)
        {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }
    
    public void stopLoadingStatus() {
        // if(mMainView != null){
        // mMainView.stopLoadingStatus();
        // }
        if (mLoadingView != null)
        {
            // ViewGroup vg = (ViewGroup) (mLoadingView.getParent());
            // vg.removeView(mLoadingView);
            // mLoadingView = null;
            if (mLoadingView.getVisibility() != View.GONE)
            {
                mLoadingView.setVisibility(View.GONE);
            }
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        int id = item.getItemId();
      switch (id) {
          case android.R.id.home:
              this.finish();
              break;

          default:
              break;
      }
      return super.onOptionsItemSelected(item);
    }
}
