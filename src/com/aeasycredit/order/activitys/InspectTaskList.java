
package com.aeasycredit.order.activitys;

import java.io.File;
import java.util.List;

import com.aeasycredit.order.R;
import com.aeasycredit.order.application.MyApplication;
import com.aeasycredit.order.database.DataBaseHelper;
import com.aeasycredit.order.models.Aeasyapp;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.models.Task;
import com.aeasycredit.order.tool.AeaCamera;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.utils.AeaFileUtil;
import com.aeasycredit.order.utils.AeasyRequestUtil;
import com.aeasycredit.order.utils.AeasySharedPreferencesUtil;
import com.aeasycredit.order.volley.Request;
import com.aeasycredit.order.volley.VolleyError;
import com.aeasycredit.order.volley.toolbox.AeaJsonResquest;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InspectTaskList extends BaseActivity {
    ListView listview;
    List<Task> list;
    InspectTaskAdapter mAdapter;
    public static final int REQUEST_CODE_TASK_REPORT = 1002;
    public boolean requestCodeNoRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.inspect_task);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.actionbar_background));

        listview = (ListView) findViewById(R.id.inspect_list);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(InspectTaskList.this, "id = " + position,
                // Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(InspectTaskList.this, InspectTaskDetail.class);
                // getEntity
                if (list != null && list.size() > position) {
                    Task task = list.get(position);
                    String json = new Gson().toJson(task);
                    intent.putExtra(AeaConstants.EXTRA_TASK, json);
                }
                startActivityForResult(intent,REQUEST_CODE_TASK_REPORT);
            }
        });
//        requestList();
    }

    @Override
    protected void onResume() {
        if(!requestCodeNoRefresh){
            requestList();
        }
        super.onResume();
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        requestCodeNoRefresh = false;
    }
    
    private void requestList() {
        // list = new ArrayList<Task>();
        // Task task = null;
        // for (int i = 0; i < 3; i++) {
        // task = new Task();
        // task.setTaskid(i + 1 + "");
        // task.setLoanBillNumber("asdfdasfsad");
        // task.setClientName("张三");
        // task.setClientPhone("23892329");
        // task.setInvestigateAddr("阿里山的减肥了撒解放路撒阿斯蒂芬就爱上了的减肥了撒的减肥；了撒的减肥了；撒的减肥了；撒的减肥；了撒的减肥了；撒的减肥；了的撒加飞拉萨；的减肥了；撒的减肥了；撒的减肥了；撒的减肥；了撒的减肥了三大；减肥");
        // task.setContactName("李四");
        // task.setContactPhone("32323232");
        // task.setInvestigateName("王五");
        // task.setAppointInvestigateTime("2015/2/3");
        // task.setRemarks("无");
        // list.add(task);
        // }
        //
        // mAdapter = new InspectTaskAdapter(list, this);
        // listview.setAdapter(mAdapter);
        startLoadingStatus();
        String requestJson = AeasyRequestUtil.getTaskListRequest(
                AeasySharedPreferencesUtil.getUserCode(this),
                AeasySharedPreferencesUtil.getUuid(this));
        AeaJsonResquest request = new AeaJsonResquest(Request.Method.POST,
                AeaConstants.REQUEST_URL, requestJson, this, this);
        MyApplication.mRequestQueue.add(request);
    }

    class InspectTaskAdapter extends BaseAdapter {
        private List<Task> list;
        Context context;

        public InspectTaskAdapter(List<Task> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Task getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            public Button btCamera;
            public TextView tvId;
            public TextView tvCustomerName;
            public TextView tvInspectDate;
            public TextView tvInspectAddress;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                v = vi.inflate(R.layout.inspect_list_item, null);
                holder = new ViewHolder();
                holder.btCamera = (Button) v.findViewById(R.id.inspect_camera);
                holder.tvCustomerName = (TextView) v
                        .findViewById(R.id.inspect_list_item_customer_name_value);
                holder.tvId = (TextView) v.findViewById(R.id.inspect_list_item_id);
                holder.tvInspectDate = (TextView) v
                        .findViewById(R.id.inspect_list_item_customer_inspect_date_value);
                holder.tvInspectAddress = (TextView) v
                        .findViewById(R.id.inspect_list_item_customer_address);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            final Task task = list.get(position);
            if (task != null) {
                holder.tvCustomerName.setText(task.getClientName());
                holder.tvId.setText(task.getTaskid());
                holder.tvInspectAddress.setText(task.getInvestigateAddr());
                holder.tvInspectDate.setText(task.getAppointInvestigateTime());
                holder.btCamera.setOnClickListener(new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        AeaCamera.getInstance().initialize(InspectTaskList.this);
                        AeaCamera.getInstance().openCamara(task.getTaskid());
                    }
                });
            }

            return v;
        }
    }

    private void buildList(Aeasyapp response) {
        list = response.getResponseBody().getTask();
        // Task task = null;
        // for (int i = 0; i < 3; i++) {
        //
        // list.add(task);
        // }
        mAdapter = new InspectTaskAdapter(list, this);
        listview.setAdapter(mAdapter);
    }

    @Override
    public void onResponse(RequestWrapper response) {
        stopLoadingStatus();
        if (response != null) {
            String code = AeasyRequestUtil.checkoutResponseCode(response.getAeasyapp()
                    .getResponseCode());
            if (AeaConstants.RESPONSE_CODE_200.equals(code)) {
                Toast.makeText(this,
                        getResources().getString(R.string.inspect_tasklist_request_success),
                        Toast.LENGTH_SHORT).show();
                // 成功
                buildList(response.getAeasyapp());
            } else if (AeaConstants.RESPONSE_CODE_600.equals(code)) {
                // 登陆超时跳转登陆界面
                Toast.makeText(this,
                        getResources().getString(R.string.inspect_checklogin_fail_timeout),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 请求失败
                Toast.makeText(this,
                        getResources().getString(R.string.inspect_tasklist_request_fail),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // 失败
        stopLoadingStatus();
        Toast.makeText(this, getResources().getString(R.string.inspect_tasklist_request_fail),
                Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(AeaCamera.REQUEST_TAKE_PHOTO == requestCode){
            requestCodeNoRefresh = true;
        }else{
            requestCodeNoRefresh = false;
        }
        AeaCamera.getInstance().onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK ==  resultCode && REQUEST_CODE_TASK_REPORT == requestCode){
            //删除数据库和本地图片
            if(data != null && data.hasExtra(AeaConstants.REPORT_TASK_ID) && data.hasExtra(AeaConstants.REPORT_TYPE)){
                String taskId = data.getExtras().getString(AeaConstants.REPORT_TASK_ID);
                int type = data.getExtras().getInt(AeaConstants.REPORT_TYPE);
                File dir = new File(Environment.getExternalStorageDirectory()
                        + "/" + AeaCamera.baselocalTempImgDir + "/" + taskId);
                if(dir.exists()){
                    AeaFileUtil.delFolder(dir.getAbsolutePath());
                }
                new DataBaseHelper(this).delRequestBodyByIdAndType(taskId, type);
            }
        }
    }
}
