
package com.aeasycredit.order.activitys;

import java.util.List;

import com.aeasycredit.order.R;
import com.aeasycredit.order.application.MyApplication;
import com.aeasycredit.order.models.RequestBody;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.tool.AeaCamera;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.utils.AeasyRequestUtil;
import com.aeasycredit.order.volley.MultipartEntity;
import com.aeasycredit.order.volley.Request;
import com.aeasycredit.order.volley.VolleyError;
import com.aeasycredit.order.volley.toolbox.MultipartRequest;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoSelectorActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InspectReportActivity extends BaseActivity implements OnClickListener {
    TextView tvAddress;
    String address;
    int reportType;
    String taskId;
    Button btBrowser;
    String baselocalTempImgDir = "aeasy";
    List<PhotoModel> photos;
    EditText etPhotoUploder;
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_report);
        reportType = getIntent().getExtras().getInt(AeaConstants.REPORT_TYPE);
        address = getIntent().getExtras().getString(AeaConstants.REPORT_ADDRESS);
        taskId = getIntent().getExtras().getString(AeaConstants.REPORT_TASK_ID);
        String title = getResources().getString(R.string.inspect_report_secert);
        if (AeaConstants.REPORT_PERCEIVE == reportType) {
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
        btBrowser = (Button) findViewById(R.id.inspect_report_browser);
        etPhotoUploder = (EditText) findViewById(R.id.inspect_report_contact_upload_photo);
        btSubmit = (Button) findViewById(R.id.inspect_report_submit);
        
        btSubmit.setOnClickListener(this);
        tvAddress.setText(address);
        btBrowser.setOnClickListener(this);
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
        Toast.makeText(this, getResources().getString(R.string.inspect_tasklist_request_success),
                Toast.LENGTH_SHORT).show();
    }
    
    public void submit(){
        startLoadingStatus();
        RequestBody body = new RequestBody();
        body.setTaskid(taskId);
        body.setInvestigateType("0");
        body.setInvestigateEndTime("20150313131525");
        body.setInvestigateAddr("广东中山市石岐区忠恳商业大楼22楼");
        body.setContactName("李四");
        body.setContactPhone("15257584359");
        body.setContactPost("经理");
        body.setCompanyName("亚馨信贷");
        body.setCompanyNature("有限责任制公司");
        body.setStaffNumber("200");
        body.setBusinessArea("4000");
        body.setIsHaveCompanyBoard(true);
        body.setIsHaveCompanyNameAtOfficeArea(true);
        body.setServiceContent("小额信贷业务");
        body.setCompanyScale("目测大厅100平米,办公区域1000平米,工作员工50人.");
        body.setProductionApparatus("无");
        body.setInventory("贷款余额超过2亿元,现在咨询人员超过10人");
        body.setInterviewContent("客户提供了公司注册文件,但是注册人是客户前夫......");
        body.setSummary("总结......");
        body.setOther("多次询问客户手续真实性,客户都比较遮遮掩掩,左顾而言其他.");
        String size = "";
        if(photos != null && photos.size() >0){
            size = photos.size() + "";
        }
        body.setImageSize(size);
        
        String requestJson = AeasyRequestUtil.getSubmitRequest(body, this);
        MultipartEntity entity = AeasyRequestUtil.imageFileUploadEntity(requestJson, photos);
//      requestJson = "request=%7B%22aeasyapp%22%3A%7B%22method%22%3A%22user.login%22%2C%22private%22%3A%22private%22%2C%22requestBody%22%3A%7B%22password%22%3A%22a%22%2C%22usercode%22%3A%22a%22%7D%2C%22serialNumber%22%3A%22b0SustIKsa%22%2C%22version%22%3A%22v1.0%22%7D%7D";
//      AeaJsonResquest request = new AeaJsonResquest(Request.Method.POST,
//              AeaConstants.REQUEST_URL, requestJson, this, this);
      MultipartRequest request = new MultipartRequest(Request.Method.POST,AeaConstants.REQUEST_URL, this, this);
      request.setMultipartEntity(entity);
      MyApplication.mRequestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.inspect_report_browser:
                // AeaCamera.getInstance().initialize(this);
//                AeaCamera.getInstance().choosePhoto(this, taskId);
                String dir  = Environment.getExternalStorageDirectory()
                        + "/" + baselocalTempImgDir  + "/" + taskId;
                Intent intent = new Intent(this, PhotoSelectorActivity.class);
                intent.putExtra(PhotoSelectorActivity.KEY_MAX, 15);
                intent.putExtra(PhotoSelectorActivity.KEY_DIR, dir);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, AeaCamera.REQUEST_PICK_PHOTO);
                break;
            case R.id.inspect_report_submit:
                submit();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == AeaCamera.REQUEST_PICK_PHOTO) {// selected image
            if (data != null && data.getExtras() != null) {
                @SuppressWarnings("unchecked")
                List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable(
                        "photos");
                if (photos != null && photos.size() > 0) {
                    this.photos = photos;
                    String str = getResources().getString(R.string.inspect_report_photo_selected);
                    etPhotoUploder.setText(String.format(str, photos.size()));
                }
            }
        }
    }
}
