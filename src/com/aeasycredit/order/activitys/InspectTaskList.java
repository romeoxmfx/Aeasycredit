
package com.aeasycredit.order.activitys;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.aeasycredit.order.R;
import com.aeasycredit.order.application.MyApplication;
import com.aeasycredit.order.database.DataBaseHelper;
import com.aeasycredit.order.models.Aeasyapp;
import com.aeasycredit.order.models.RequestBody;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.models.Task;
import com.aeasycredit.order.models.Task.ClientInfo;
import com.aeasycredit.order.tool.AeaCamera;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.utils.AeaFileUtil;
import com.aeasycredit.order.utils.AeasyRequestUtil;
import com.aeasycredit.order.utils.AeasySharedPreferencesUtil;
import com.aeasycredit.order.volley.Request;
import com.aeasycredit.order.volley.VolleyError;
import com.aeasycredit.order.volley.toolbox.AeaJsonResquest;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoSelectorActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InspectTaskList extends BaseActivity implements OnRefreshListener<ListView> {
    PullToRefreshListView listview;
    List<Task> list;
    InspectTaskAdapter mAdapter;
    public static final int REQUEST_CODE_TASK_REPORT = 1002;
    public boolean requestCodeNoRefresh;
    int selectedId;
    public Task task;
    public static final boolean BIND_SUCC_TRUE = true;
    public static final boolean BIND_SUCC_FALSE = false;
    DataBaseHelper dbHelper;
    // PullToRefreshListView pullToRefresh;
    String previewSelectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.inspect_task);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.actionbar_background));
        dbHelper = new DataBaseHelper(this);
        // listview = (ListView) findViewById(R.id.inspect_list);
        listview = (PullToRefreshListView) findViewById(R.id.inspect_list);
        listview.setOnRefreshListener(this);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(InspectTaskList.this, "id = " + position,
                // Toast.LENGTH_SHORT).show();
                int relpos = position - 1;
                Intent intent = new Intent();
                intent.setClass(InspectTaskList.this, InspectTaskDetail.class);
                // getEntity
                if (list != null && list.size() > relpos) {
                    Task task = list.get(relpos);
                    String json = new Gson().toJson(task);
                    intent.putExtra(AeaConstants.EXTRA_TASK, json);
                    startActivityForResult(intent, REQUEST_CODE_TASK_REPORT);
                }
                selectedId = relpos;
            }
        });
        // requestList();
    }

    @Override
    protected void onResume() {
        if (!requestCodeNoRefresh) {
            requestList();
        } else {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
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
            public Button btPreview;
            public TextView tvId;
            public TextView tvCustomerName;
            public TextView tvInspectDate;
            public TextView tvInspectAddress;
            public ImageView ivImage;
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
                holder.ivImage = (ImageView) v.findViewById(R.id.inspect_list_item_image);
                holder.btPreview = (Button) v.findViewById(R.id.inspect_preview);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            final Task task = list.get(position);
            if (task != null) {
                List<ClientInfo> info = task.getClientInfos();
                if (info != null && info.size() > 0) {
                    ClientInfo clientInfo = info.get(0);
                    holder.tvCustomerName.setText(clientInfo.getClientName());
                }
                holder.tvId.setText(task.getTaskid());
                holder.tvInspectAddress.setText(task.getInvestigateAddr());
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
                holder.tvInspectDate.setText(date);
                holder.btPreview.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        openPreView(task.getTaskid());
                    }
                });
                holder.btCamera.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // AeaCamera.getInstance().initialize(InspectTaskList.this);
                        InspectTaskList.this.task = task;
                        AeaCamera.getInstance().openCamara(InspectTaskList.this, task.getTaskid());
                    }
                });
                if (!bindImage(task.getTaskid(), holder.ivImage)) {
                    holder.ivImage.setTag(BIND_SUCC_FALSE);
                    holder.ivImage.setImageResource(R.drawable.photo);
                } else {
                    holder.ivImage.setTag(BIND_SUCC_TRUE);
                }
                holder.ivImage.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        boolean bind = (boolean) v.getTag();
                        doImageClick(bind, task.getTaskid());
                    }
                });
            }
            return v;
        }
    }

    public void doImageClick(boolean bindSucc, String taskId) {
        if (bindSucc) {
            openPreView(taskId);
        } else {
            InspectTaskList.this.task = task;
            AeaCamera.getInstance().openCamara(InspectTaskList.this, taskId);
        }
    }

    public void openPreView(String taskid) {
        try {
            previewSelectedId = taskid;
            String dir = Environment.getExternalStorageDirectory()
                    + "/" + "aeasy" + "/" + taskid;
            File file = new File(dir);
            if (file != null && file.exists() && file.list() != null && file.list().length > 0) {
                Intent intent = new Intent(InspectTaskList.this, PhotoSelectorActivity.class);
                intent.putExtra(PhotoSelectorActivity.KEY_MAX, 15);
                intent.putExtra(PhotoSelectorActivity.KEY_DIR, dir);
                if (dbHelper.haveRequestBody(taskid, AeaConstants.REPORT_PERCEIVE)) {
                    RequestBody body = dbHelper.getRequestBodyByTaskIdAndType(taskid,
                            AeaConstants.REPORT_PERCEIVE);
                    intent.putExtra(PhotoSelectorActivity.KEY_SELECTED, body.getFiles());
                } else if (dbHelper.haveRequestBody(taskid, AeaConstants.REPORT_SECRET)) {
                    RequestBody body = dbHelper.getRequestBodyByTaskIdAndType(taskid,
                            AeaConstants.REPORT_SECRET);
                    intent.putExtra(PhotoSelectorActivity.KEY_SELECTED, body.getFiles());
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, AeaCamera.REQUEST_PICK_PHOTO);
            } else {
                Toast.makeText(this, "无照片", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean bindImage(String taskid, ImageView iv) {
        File dir = new File(Environment.getExternalStorageDirectory()
                + "/" + "aeasy" + "/" + taskid);
        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles();
            Arrays.sort(files, new Comparator<File>() {

                @Override
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0)
                        return -1;
                    else if (diff == 0)
                        return 0;
                    else
                        return 1;
                }
            });

            if (files != null && files.length > 0) {
                // file:///mnt/sdcard/image.png
                String uri = "file:///" + files[0].getAbsolutePath();
                Log.i(AeaConstants.TAG, "file uri = " + uri);
                DisplayImageOptions options = DisplayImageOptions.createSimple();
                ImageLoader.getInstance().displayImage(uri, iv, options);
                return true;
            }
        }
        return false;
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
        // super.onResponse(response);
        listview.onRefreshComplete();
        if (onResponseBase(response)) {
            return;
        }
        // stopLoadingStatus();
        if (response != null) {
            String code = response.getAeasyapp().getResponseCode();
            if (AeaConstants.RESPONSE_CODE_200.equals(code)) {
                // Toast.makeText(this,
                // getResources().getString(R.string.inspect_tasklist_request_success),
                // Toast.LENGTH_SHORT).show();
                // 成功
                buildList(response.getAeasyapp());
            }
            // else if (AeaConstants.RESPONSE_CODE_600.equals(code)) {
            // // 登陆超时跳转登陆界面
            // Toast.makeText(this,
            // getResources().getString(R.string.inspect_checklogin_fail_timeout),
            // Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, LoginActivity.class);
            // startActivity(intent);
            // finish();
            // } else {
            // // 请求失败
            // Toast.makeText(this,
            // getResources().getString(R.string.inspect_tasklist_request_fail),
            // Toast.LENGTH_SHORT).show();
            // }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // 失败
        listview.onRefreshComplete();
        stopLoadingStatus();
        Toast.makeText(this, getResources().getString(R.string.inspect_tasklist_request_fail),
                Toast.LENGTH_SHORT).show();
        mAdapter = null;
        list = null;
        listview.setAdapter(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AeaCamera.REQUEST_TAKE_PHOTO == requestCode) {
            requestCodeNoRefresh = true;
            if (resultCode == Activity.RESULT_OK) {
                AeaCamera.getInstance().onActivityResult(requestCode, resultCode, data);
                AeaCamera.getInstance().openCamara(InspectTaskList.this, task.getTaskid());
            }
        } else if (AeaCamera.REQUEST_PICK_PHOTO == requestCode) {
            requestCodeNoRefresh = true;
        } else {
            requestCodeNoRefresh = false;
        }

        // AeaCamera.getInstance().onActivityResult(requestCode, resultCode,
        // data);
        if (RESULT_OK == resultCode && REQUEST_CODE_TASK_REPORT == requestCode) {
            // 删除数据库和本地图片
            // if(data != null && data.hasExtra(AeaConstants.REPORT_TASK_ID) &&
            // data.hasExtra(AeaConstants.REPORT_TYPE)){
            if (data != null && data.hasExtra(AeaConstants.REPORT_TASK_ID)) {
                String taskId = data.getExtras().getString(AeaConstants.REPORT_TASK_ID);
                // int type = data.getExtras().getInt(AeaConstants.REPORT_TYPE);
                File dir = new File(Environment.getExternalStorageDirectory()
                        + "/" + AeaCamera.baselocalTempImgDir + "/" + taskId);
                if (dir.exists()) {
                    AeaFileUtil.delFolder(dir.getAbsolutePath());
                }
                new DataBaseHelper(this).delRequestBodyById(taskId);
                if (list != null && list.size() > selectedId) {
                    list.remove(selectedId);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        if (RESULT_OK == resultCode && requestCode == AeaCamera.REQUEST_PICK_PHOTO) {
            if (data != null && data.getExtras() != null) {
                @SuppressWarnings("unchecked")
                List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable(
                        "photos");
                if (dbHelper.haveRequestBody(previewSelectedId, AeaConstants.REPORT_PERCEIVE)) {
                    // RequestBody body = dbHelper.updateRequestBody(body);
                    RequestBody body = dbHelper.getRequestBodyByTaskIdAndType(previewSelectedId,
                            AeaConstants.REPORT_PERCEIVE);
                    if (photos != null && photos.size() > 0) {
                        body.setImageSize(photos.size() + "");
                        StringBuffer sb = new StringBuffer();
                        for (PhotoModel photo : photos) {
                            sb.append(photo.getOriginalPath())
                                    .append(",");
                        }
                        String files = sb.toString();
                        files = files.substring(0, files.lastIndexOf(","));
                        body.setFiles(files);
                        dbHelper.updateRequestBody(body);
                    }
                } else {
                    RequestBody body = new RequestBody();
                    body.setTaskid(previewSelectedId);
                    body.setInvestigateType(AeaConstants.REPORT_PERCEIVE + "");
                    if (photos != null && photos.size() > 0) {
                        body.setImageSize(photos.size() + "");
                        StringBuffer sb = new StringBuffer();
                        for (PhotoModel photo : photos) {
                            sb.append(photo.getOriginalPath())
                                    .append(",");
                        }
                        String files = sb.toString();
                        files = files.substring(0, files.lastIndexOf(","));
                        body.setFiles(files);
                        dbHelper.insertRequestBody(body);
                    }
                }
                if (dbHelper.haveRequestBody(previewSelectedId, AeaConstants.REPORT_SECRET)) {
                    RequestBody body = dbHelper.getRequestBodyByTaskIdAndType(previewSelectedId,
                            AeaConstants.REPORT_SECRET);
                    if (photos != null && photos.size() > 0) {
                        body.setImageSize(photos.size() + "");
                        StringBuffer sb = new StringBuffer();
                        for (PhotoModel photo : photos) {
                            sb.append(photo.getOriginalPath())
                                    .append(",");
                        }
                        String files = sb.toString();
                        files = files.substring(0, files.lastIndexOf(","));
                        body.setFiles(files);
                        dbHelper.updateRequestBody(body);
                    }
                } else {
                    RequestBody body = new RequestBody();
                    body.setTaskid(previewSelectedId);
                    body.setInvestigateType(AeaConstants.REPORT_SECRET + "");
                    if (photos != null && photos.size() > 0) {
                        body.setImageSize(photos.size() + "");
                        StringBuffer sb = new StringBuffer();
                        for (PhotoModel photo : photos) {
                            sb.append(photo.getOriginalPath())
                                    .append(",");
                        }
                        String files = sb.toString();
                        files = files.substring(0, files.lastIndexOf(","));
                        body.setFiles(files);
                        dbHelper.insertRequestBody(body);
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == AeaCamera.REQUEST_PICK_PHOTO) {
            if (dbHelper.haveRequestBody(previewSelectedId, AeaConstants.REPORT_PERCEIVE)) {
                // RequestBody body = dbHelper.updateRequestBody(body);
                RequestBody body = dbHelper.getRequestBodyByTaskIdAndType(previewSelectedId,
                        AeaConstants.REPORT_PERCEIVE);
                body.setFiles(null);
                dbHelper.updateRequestBody(body);
            }
            if (dbHelper.haveRequestBody(previewSelectedId, AeaConstants.REPORT_SECRET)) {
                RequestBody body = dbHelper.getRequestBodyByTaskIdAndType(previewSelectedId,
                        AeaConstants.REPORT_SECRET);
                body.setFiles(null);
                dbHelper.updateRequestBody(body);
            }
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        requestList();
    }

}
