
package com.aeasycredit.order.activitys;

import java.util.ArrayList;
import java.util.List;

import com.aeasycredit.order.R;
import com.aeasycredit.order.application.MyApplication;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.utils.AeasyRequestUtil;
import com.aeasycredit.order.utils.AeasySharedPreferencesUtil;
import com.aeasycredit.order.volley.Request;
import com.aeasycredit.order.volley.VolleyError;
import com.aeasycredit.order.volley.toolbox.AeaJsonResquest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
    Button btLogin;
    AutoCompleteTextView etName;
    AutoCompleteTextView etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loginactivity);
        btLogin = (Button) findViewById(R.id.login_button);
        etName = (AutoCompleteTextView) findViewById(R.id.login_name);
        etPassword = (AutoCompleteTextView) findViewById(R.id.login_password);
        createLoginUserAdapter();
        
        btLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String password = etPassword.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    startLoadingStatus();
                    doLogin(name, password);
                } else {
                    Toast.makeText(LoginActivity.this,
                            getResources().getString(R.string.inspect_login_input_error),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    private void createLoginUserAdapter(){
        String loginUsers = AeasySharedPreferencesUtil.getLoginUsers(this);
        try {
            if(!TextUtils.isEmpty(loginUsers)){
                String[] users = loginUsers.split(",");
                if(users != null && users.length > 0){
                    List<String> list = new ArrayList<String>();
                    for (String user : users) {
                        list.add(user);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
                    etName.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLogin(String name, String password) {
        String requestJson = AeasyRequestUtil.getLoginRequest(name, password);
        // requestJson =
        // "request=%7B%22aeasyapp%22%3A%7B%22method%22%3A%22user.login%22%2C%22private%22%3A%22private%22%2C%22requestBody%22%3A%7B%22password%22%3A%22a%22%2C%22usercode%22%3A%22a%22%7D%2C%22serialNumber%22%3A%22b0SustIKsa%22%2C%22version%22%3A%22v1.0%22%7D%7D";
        AeaJsonResquest request = new AeaJsonResquest(Request.Method.POST,
                AeaConstants.REQUEST_URL, requestJson, this, this);
        MyApplication.mRequestQueue.add(request);

        // Intent intent = new Intent();
        // intent.setClass(LoginActivity.this, HomeActivity.class);
        // startActivity(intent);
        // finish();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        stopLoadingStatus();
        Toast.makeText(this, getResources().getString(R.string.inspect_login_fail),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(RequestWrapper response) {
//        super.onResponse(response);
        if(onResponseBase(response)){
            return; 
         }
        // stopLoadingStatus();
        if (response != null
                && !TextUtils.isEmpty(response.getAeasyapp().getResponseBody().getUuid())) {
            // saveUuid
            AeasySharedPreferencesUtil.saveUuid(this, response.getAeasyapp().getResponseBody()
                    .getUuid());
            //saveLoginUser
            AeasySharedPreferencesUtil.saveLoginUsers(this, etName.getText().toString());
            AeasySharedPreferencesUtil.saveUserCode(this, etName.getText().toString());
            Toast.makeText(this, getResources().getString(R.string.inspect_login_success),
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        // else{
        // Toast.makeText(this,
        // getResources().getString(R.string.inspect_login_fail),
        // Toast.LENGTH_SHORT).show();
        // }
    }

}
