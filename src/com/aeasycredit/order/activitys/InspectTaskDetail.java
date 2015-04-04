
package com.aeasycredit.order.activitys;

import com.aeasycredit.order.R;
import com.aeasycredit.order.models.Aeasyapp;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.models.Task;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.volley.VolleyError;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InspectTaskDetail extends BaseActivity implements OnClickListener{
    TextView tvNum;
    TextView tvCustomerName;
    TextView tvCustomerPhone;
    TextView tvAddress;
    TextView tvContact;
    TextView tvContackPhone;
    TextView tvInspectPerson;
    TextView tvDate;
    TextView tvRemak;
    Button btPerceiveReport;
    Button btSecretReport;
    private Task task;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.inspect_task_detail);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.actionbar_background));

        tvNum = (TextView) findViewById(R.id.inspect_detail_num);
        tvCustomerName = (TextView) findViewById(R.id.inspect_detail_customer_name);
        tvCustomerPhone = (TextView) findViewById(R.id.inspect_detail_customer_phone);
        tvAddress = (TextView) findViewById(R.id.inspect_detail_inspect_address);
        tvContact = (TextView) findViewById(R.id.inspect_detail_contacts);
        tvContackPhone = (TextView) findViewById(R.id.inspect_detail_contacts_phone);
        tvInspectPerson = (TextView) findViewById(R.id.inspect_detail_inspect_person);
        tvDate = (TextView) findViewById(R.id.inspect_detail_inspect_predate);
        tvRemak = (TextView) findViewById(R.id.inspect_task_detail_inspect_remark);
        btPerceiveReport = (Button) findViewById(R.id.inspect_detail_inspect_perceive_report);
        btSecretReport = (Button) findViewById(R.id.inspect_task_detail_inspect_secret_report);
        btPerceiveReport.setOnClickListener(this);
        btSecretReport.setOnClickListener(this);
        
        receiveDetail(getIntent());
    }
    
    private void receiveDetail(Intent intent){
        if(intent.getExtras() != null && intent.getExtras().containsKey(AeaConstants.EXTRA_TASK)){
            String json = intent.getExtras().getString(AeaConstants.EXTRA_TASK);
            task = new Gson().fromJson(json, Task.class);
            bindData();
        }else{
            Toast.makeText(this, getResources().getString(R.string.inspect_taskdetail_fail), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindData() {
        tvNum.setText(task.getLoanBillNumber());
        tvCustomerName.setText(task.getClientName());
        tvCustomerPhone.setText(task.getClientPhone());
        tvAddress.setText(task.getInvestigateAddr());;
        tvContact.setText(task.getContactName());
        tvContackPhone.setText(task.getContactPhone());;
        tvInspectPerson.setText(task.getInvestigateName());
        tvDate.setText(task.getAppointInvestigateTime());
        tvRemak.setText(task.getRemarks());
    }

    @Override
    public void onResponse(RequestWrapper response) {
        
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        
    }

    @Override
    public void onClick(View v) {
        
    }

}