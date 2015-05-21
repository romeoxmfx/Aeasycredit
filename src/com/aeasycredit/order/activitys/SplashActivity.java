
package com.aeasycredit.order.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import com.aeasycredit.order.R;
import com.aeasycredit.order.application.MyApplication;
import com.aeasycredit.order.models.Aeasyapp;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.utils.AeasyRequestUtil;
import com.aeasycredit.order.utils.AeasySharedPreferencesUtil;
import com.aeasycredit.order.volley.Request;
import com.aeasycredit.order.volley.VolleyError;
import com.aeasycredit.order.volley.toolbox.AeaJsonResquest;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.inspect_splash);
        if (!checkLogin()) {
            // go to login
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // check login
            checkLoginInServer();
        }
    }

    private boolean checkLogin() {
        if (TextUtils.isEmpty(AeasySharedPreferencesUtil.getUuid(this))) {
            return false;
        } else {
            return true;
        }
    }

    private void checkLoginInServer() {
        startLoadingStatus();
        String requestJson = AeasyRequestUtil.getCheckLoginRequest(
                AeasySharedPreferencesUtil.getUserCode(this),
                AeasySharedPreferencesUtil.getUuid(this));
        AeaJsonResquest request = new AeaJsonResquest(Request.Method.POST,
                AeaConstants.REQUEST_URL, requestJson, this, this);
        MyApplication.mRequestQueue.add(request);
    }

    @Override
    public void onResponse(RequestWrapper response) {
        // super.onResponse(response);
        if (onResponseBase(response)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if (response != null &&
                !TextUtils.isEmpty(response.getAeasyapp().getResponseBody().getUuid())) {
            // if (response != null
            // &&
            // response.getAeasyapp().getResponseCode().equals(AeaConstants.RESPONSE_CODE_601))
            // {
            // 保存
            // AeasySharedPreferencesUtil.saveUserCode(this,
            // response.getUsercode());
            AeasySharedPreferencesUtil.saveUuid(this, response.getAeasyapp().getResponseBody()
                    .getUuid());
            // Toast.makeText(this,
            // getResources().getString(R.string.inspect_checklogin_success),
            // Toast.LENGTH_SHORT).show();
            // 跳转主界面界面
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        // else{
        // //失败跳转登陆界面
        // if(response != null &&
        // !TextUtils.isEmpty(response.getAeasyapp().getResponseBody().getErrorMsg())){
        // // String code = response.getAeasyapp().getResponseCode();
        // //
        // if(AeaConstants.RESPONSE_CODE_601.equals(response.getAeasyapp().getResponseCode())){
        // // Toast.makeText(this,
        // getResources().getString(R.string.inspect_checklogin_fail_timeout),
        // Toast.LENGTH_SHORT).show();
        // // }
        // String msg = response.getAeasyapp().getResponseBody().getErrorMsg();
        // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // }else{
        // Toast.makeText(this,
        // getResources().getString(R.string.inspect_checklogin_fail),
        // Toast.LENGTH_SHORT).show();
        // }
        // Intent intent = new Intent(this, LoginActivity.class);
        // startActivity(intent);
        // finish();
        // }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        stopLoadingStatus();
        // 失败，跳转登陆界面
        Toast.makeText(this, getResources().getString(R.string.inspect_checklogin_fail),
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
