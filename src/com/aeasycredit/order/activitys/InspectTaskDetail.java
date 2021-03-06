
package com.aeasycredit.order.activitys;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aeasycredit.order.R;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.models.Task;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.volley.VolleyError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InspectTaskDetail extends BaseActivity implements OnClickListener {
    TextView tvNum;
    TextView tvCustomerInfo;
//    TextView tvCustomerPhone;
    TextView tvAddress;
    TextView tvContact;
    TextView tvContackPhone;
    TextView tvInspectPerson;
    TextView tvDate;
    TextView tvRemak;
    Button btPerceiveReport;
    Button btSecretReport;
    private Task task;
    boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.inspect_task_detail);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.actionbar_background));

        tvNum = (TextView) findViewById(R.id.inspect_detail_num);
        tvCustomerInfo = (TextView) findViewById(R.id.inspect_detail_customer_info);
        tvAddress = (TextView) findViewById(R.id.inspect_detail_inspect_address);
        tvContact = (TextView) findViewById(R.id.inspect_detail_contacts);
        tvContackPhone = (TextView) findViewById(R.id.inspect_detail_contacts_phone);
        tvInspectPerson = (TextView) findViewById(R.id.inspect_detail_inspect_person);
        tvDate = (TextView) findViewById(R.id.inspect_detail_inspect_predate);
        tvRemak = (TextView) findViewById(R.id.inspect_task_detail_inspect_remark);
        btPerceiveReport = (Button) findViewById(R.id.inspect_detail_inspect_perceive_report);
        btSecretReport = (Button) findViewById(R.id.inspect_detail_inspect_secret_report);
        btPerceiveReport.setOnClickListener(this);
        btSecretReport.setOnClickListener(this);

        receiveDetail(getIntent());
    }

    private void receiveDetail(Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().containsKey(AeaConstants.EXTRA_TASK)) {
            String json = intent.getExtras().getString(AeaConstants.EXTRA_TASK);
//            task = new Gson().fromJson(json, Task.class);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            try {
                task = mapper.readValue(json, Task.class);
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bindData();
        } else {
            Toast.makeText(this, getResources().getString(R.string.inspect_taskdetail_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void bindData() {
        String clientInfos = "";
        StringBuffer sb = null;
        if(task.getClientInfos() != null && task.getClientInfos().size() > 0){
            sb = new StringBuffer();
            for (Task.ClientInfo clientInfo : task.getClientInfos()) {
                sb.append(""+clientInfo.toString())
                .append(",");
            }
            clientInfos = sb.toString();
            if(!TextUtils.isEmpty(clientInfos)){
                clientInfos = clientInfos.substring(0,clientInfos.lastIndexOf(","));
                tvCustomerInfo.setText(clientInfos);
            }
        }
        tvNum.setText(task.getLoanBillNumber());
//        tvCustomerName.setText(task.getClientName());
//        tvCustomerPhone.setText(task.getClientPhone());
        tvAddress.setText(task.getInvestigateAddr());
        ;
        tvContact.setText(task.getContactName());
        tvContackPhone.setText(task.getContactPhone());
        ;
        tvInspectPerson.setText(task.getInvestigateName());
        String date = task.getAppointInvestigateTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date data = format.parse(date);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            date = format1.format(data);
        } catch (ParseException e) {
            e.printStackTrace();
            date = task.getAppointInvestigateTime();
        }
        tvDate.setText(date);
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
        int id = v.getId();
        switch (id) {
            case R.id.inspect_detail_inspect_perceive_report:
                Intent intentPerceiveReport = new Intent(this, InspectReportActivity.class);
                intentPerceiveReport.putExtra(AeaConstants.REPORT_TYPE,
                        AeaConstants.REPORT_PERCEIVE);
                intentPerceiveReport.putExtra(AeaConstants.REPORT_TASK_ID, task.getTaskid());
                intentPerceiveReport.putExtra(AeaConstants.REPORT_ADDRESS, task.getInvestigateAddr());
                startActivityForResult(intentPerceiveReport,InspectTaskList.REQUEST_CODE_TASK_REPORT);
                break;
            case R.id.inspect_detail_inspect_secret_report:
                Intent intentSecretReport = new Intent(this, InspectReportActivity.class);
                intentSecretReport.putExtra(AeaConstants.REPORT_TYPE,
                        AeaConstants.REPORT_SECRET);
                intentSecretReport.putExtra(AeaConstants.REPORT_TASK_ID, task.getTaskid());
                intentSecretReport.putExtra(AeaConstants.REPORT_ADDRESS, task.getInvestigateAddr());
                startActivityForResult(intentSecretReport,InspectTaskList.REQUEST_CODE_TASK_REPORT);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode){
            if(InspectTaskList.REQUEST_CODE_TASK_REPORT == requestCode){
//                result = true;
                Intent intent = new Intent();
                intent.putExtra(AeaConstants.REPORT_TASK_ID, task.getTaskid());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
    
    @Override
    protected void onDestroy() {
//        if(result){
//            Intent intent = new Intent();
//            intent.putExtra(AeaConstants.REPORT_TASK_ID, task.getTaskid());
//            setResult(RESULT_OK, intent);
//        }
        super.onDestroy();
    }
}
