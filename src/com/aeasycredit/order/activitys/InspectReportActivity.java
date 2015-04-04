
package com.aeasycredit.order.activitys;

import com.aeasycredit.order.R;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.volley.VolleyError;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class InspectReportActivity extends BaseActivity {
    TextView tvAddress;
    String address;
    int reportType;
    String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_report);
        reportType = getIntent().getExtras().getInt(AeaConstants.REPORT_TYPE);
        address = getIntent().getExtras().getString(AeaConstants.REPORT_ADDRESS);
        taskId = getIntent().getExtras().getString(AeaConstants.REPORT_TASK_ID);
        String title = getResources().getString(R.string.inspect_report_secert);
        if(AeaConstants.REPORT_PERCEIVE == reportType){
            title = getResources().getString(R.string.inspect_report_perceive);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.actionbar_background));
        init();
    }

    private void init() {
        tvAddress = (TextView) findViewById(R.id.inspect_report_address);
        tvAddress.setText(address);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // 失败
        stopLoadingStatus();
        Toast.makeText(this, getResources().getString(R.string.inspect_tasklist_request_fail),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(RequestWrapper response) {
        stopLoadingStatus();
    }
}
