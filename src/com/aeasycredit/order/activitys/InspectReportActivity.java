
package com.aeasycredit.order.activitys;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.aeasycredit.order.R;
import com.aeasycredit.order.application.MyApplication;
import com.aeasycredit.order.database.DataBaseHelper;
import com.aeasycredit.order.models.RequestBody;
import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.tool.AeaCamera;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.utils.AeasyRequestUtil;
import com.aeasycredit.order.utils.DateTimePickDialogUtil;
import com.aeasycredit.order.volley.ByteArrayBodyWrapper;
import com.aeasycredit.order.volley.MultiPartRequest;
import com.aeasycredit.order.volley.Request;
import com.aeasycredit.order.volley.VolleyError;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoSelectorActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class InspectReportActivity extends BaseActivity implements OnClickListener,
        OnMenuItemClickListener {
    TextView tvAddress;
    String address;
    int reportType;
    String taskId;
    Button btBrowser;
    String baselocalTempImgDir = "aeasy";
    List<PhotoModel> photos;
    EditText etPhotoUploder;
    Button btSubmit;
    boolean submiting = false;
//    String endTime;

    EditText etContact;
    EditText etContactPhone;
    EditText etContactPosition;
    EditText etCompany;
    TextView tvCompanyTitle;
    EditText etCompanyNature;
    TextView tvCompanyNatureTitle;
    EditText etStaffNum;
    TextView tvStaffNumTitle;
    EditText etBusinessArea;
    TextView tvBusinessAreaTitle;
    RadioGroup etIsHaveCompanyBoard;
    TextView tvIsHaveCompanyBoardTitle;
    RadioGroup etIsHaveCompanyNameAtOfficeArea;
    TextView tvIsHaveCompanyNameAtOfficeAreaTitle;
    EditText etServiceContent;
    TextView tvServiceContentTitle;
    EditText etCompanyScale;
    TextView tvCompanyScaleTitle;
    EditText etProductionApparatus;
    TextView tvProductionApparatusTitle;
    EditText etInventory;
    TextView tvInventoryTitle;
    EditText etInterviewContent;
    EditText etSummary;
    EditText etOther;
    TextView tvOtherTitle;
    EditText etImageSize;
    TextView tvInvestigateEndTime;
    RadioButton rbIsHaveCompanyBoardYes;
    RadioButton rbIsHaveCompanyBoardNo;
    RadioButton rbIsHaveCompanyNameAtOfficeAreaYes;
    RadioButton rbIsHaveCompanyNameAtOfficeAreaNo;

    LinearLayout llContactName;
    LinearLayout llContactPhone;
    LinearLayout llContactPost;
    LinearLayout llInterviewContent;
    LinearLayout llSummary;

    Boolean isHaveCompanyBoard;
    Boolean isHaveCompanyNameAtOfficeArea;

    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_report);
        dbHelper = new DataBaseHelper(this);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add("main")
//                .setIcon(R.drawable.btn_menu_save_selector)
//                .setOnMenuItemClickListener(this)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        return super.onCreateOptionsMenu(menu);
//    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(submiting){
            menu.clear();
        }else{
            menu.add("main")
            .setIcon(R.drawable.btn_menu_save_selector)
            .setOnMenuItemClickListener(this)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void init() {
        tvAddress = (TextView) findViewById(R.id.inspect_report_address);
        btBrowser = (Button) findViewById(R.id.inspect_report_browser);
        etPhotoUploder = (EditText) findViewById(R.id.inspect_report_contact_upload_photo);
        btSubmit = (Button) findViewById(R.id.inspect_report_submit);
        etContact = (EditText) findViewById(R.id.inspect_report_contact);
        etContactPhone = (EditText) findViewById(R.id.inspect_report_contact_phone);
        etContactPosition = (EditText) findViewById(R.id.inspect_report_contact_position);
        etCompany = (EditText) findViewById(R.id.inspect_report_contact_company);
        etCompanyNature = (EditText) findViewById(R.id.inspect_report_contact_company_nature);
        etStaffNum = (EditText) findViewById(R.id.inspect_report_contact_company_employee_count);
        etBusinessArea = (EditText) findViewById(R.id.inspect_report_contact_ground_area);
        etIsHaveCompanyBoard = (RadioGroup) findViewById(R.id.inspect_report_contact_company_has_logo);
        etIsHaveCompanyNameAtOfficeArea = (RadioGroup) findViewById(R.id.inspect_report_contact_company_has_name);
        etServiceContent = (EditText) findViewById(R.id.inspect_report_contact_company_business_content);
        etCompanyScale = (EditText) findViewById(R.id.inspect_report_contact_company_scale);
        etProductionApparatus = (EditText) findViewById(R.id.inspect_report_contact_product_tool);
        etInventory = (EditText) findViewById(R.id.inspect_report_contact_stock);
        etInterviewContent = (EditText) findViewById(R.id.inspect_report_contact_interview_content);
        etSummary = (EditText) findViewById(R.id.inspect_report_contact_inspect_summary);
        etOther = (EditText) findViewById(R.id.inspect_report_contact_supplement);
        tvInvestigateEndTime = (TextView) findViewById(R.id.inspect_report_time);

        tvCompanyTitle = (TextView) findViewById(R.id.inspect_report_contact_company_title);
        tvCompanyNatureTitle = (TextView) findViewById(R.id.inspect_report_contact_company_nature_title);
        tvStaffNumTitle = (TextView) findViewById(R.id.inspect_report_contact_company_employee_count_title);
        tvBusinessAreaTitle = (TextView) findViewById(R.id.inspect_report_contact_ground_area_title);
        tvIsHaveCompanyBoardTitle = (TextView) findViewById(R.id.inspect_report_contact_company_has_logo_title);
        tvIsHaveCompanyNameAtOfficeAreaTitle = (TextView) findViewById(R.id.inspect_report_contact_company_has_name_title);
        tvServiceContentTitle = (TextView) findViewById(R.id.inspect_report_contact_company_business_content_title);
        tvCompanyScaleTitle = (TextView) findViewById(R.id.inspect_report_contact_company_scale_title);
        tvProductionApparatusTitle = (TextView) findViewById(R.id.inspect_report_contact_product_tool_title);
        tvInventoryTitle = (TextView) findViewById(R.id.inspect_report_contact_stock_title);
        tvOtherTitle = (TextView) findViewById(R.id.inspect_report_contact_supplement_title);

        llContactName = (LinearLayout) findViewById(R.id.inspect_report_contact_layout);
        llContactPhone = (LinearLayout) findViewById(R.id.inspect_report_contact_phone_layout);
        llContactPost = (LinearLayout) findViewById(R.id.inspect_report_contact_position_layout);
        llInterviewContent = (LinearLayout) findViewById(R.id.inspect_report_contact_interview_content_layout);
        llSummary = (LinearLayout) findViewById(R.id.inspect_report_contact_inspect_summary_layout);

        rbIsHaveCompanyBoardYes = (RadioButton) findViewById(R.id.inspect_report_contact_company_has_logo_yes);
        rbIsHaveCompanyBoardNo = (RadioButton) findViewById(R.id.inspect_report_contact_company_has_logo_no);
        rbIsHaveCompanyNameAtOfficeAreaNo = (RadioButton) findViewById(R.id.inspect_report_contact_company_has_name_no);
        rbIsHaveCompanyNameAtOfficeAreaYes = (RadioButton) findViewById(R.id.inspect_report_contact_company_has_name_yes);

        btSubmit.setOnClickListener(this);
        tvAddress.setText(address);
        btBrowser.setOnClickListener(this);
        tvInvestigateEndTime.setOnClickListener(this);
        tvInvestigateEndTime.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

        etIsHaveCompanyBoard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                if (id == R.id.inspect_report_contact_company_has_logo_yes) {
                    isHaveCompanyBoard = Boolean.valueOf(true);
                } else {
                    isHaveCompanyBoard = Boolean.valueOf(false);
                }
            }
        });

        etIsHaveCompanyNameAtOfficeArea
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int id = group.getCheckedRadioButtonId();
                        if (id == R.id.inspect_report_contact_company_has_name_yes) {
                            isHaveCompanyNameAtOfficeArea = Boolean.valueOf(true);
                        } else {
                            isHaveCompanyNameAtOfficeArea = Boolean.valueOf(false);
                        }
                    }
                });

        if (RequestBody.INVESTIGATE_TYPE_SECERT == reportType) {
            llContactName.setVisibility(View.GONE);
            llContactPhone.setVisibility(View.GONE);
            llContactPost.setVisibility(View.GONE);
            llInterviewContent.setVisibility(View.GONE);
        } else {
            llSummary.setVisibility(View.GONE);
            tvCompanyTitle.setText(tvCompanyTitle.getText() + "(*)");
            tvCompanyNatureTitle.setText(tvCompanyNatureTitle.getText() + "(*)");
            tvStaffNumTitle.setText(tvStaffNumTitle.getText() + "(*)");
            tvBusinessAreaTitle.setText(tvBusinessAreaTitle.getText() + "(*)");
            tvIsHaveCompanyBoardTitle.setText(tvIsHaveCompanyBoardTitle.getText() + "(*)");
            tvIsHaveCompanyNameAtOfficeAreaTitle.setText(tvIsHaveCompanyNameAtOfficeAreaTitle
                    .getText() + "(*)");
            tvServiceContentTitle.setText(tvServiceContentTitle.getText() + "(*)");
            tvCompanyScaleTitle.setText(tvCompanyScaleTitle.getText() + "(*)");
            tvProductionApparatusTitle.setText(tvProductionApparatusTitle.getText() + "(*)");
            tvInventoryTitle.setText(tvInventoryTitle.getText() + "(*)");
            tvOtherTitle.setText(tvOtherTitle.getText() + "(*)");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recoverInstance();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // 失败
        stopLoadingStatus();
        Toast.makeText(this, getResources().getString(R.string.inspect_report_submit_fail),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(RequestWrapper response) {
        stopLoadingStatus();
        String code = AeasyRequestUtil.checkoutResponseCode(response.getAeasyapp()
                .getResponseCode());
        if (AeaConstants.RESPONSE_CODE_200.equals(code)) {
            Toast.makeText(this,
                    getResources().getString(R.string.inspect_report_submit_succ),
                    Toast.LENGTH_SHORT).show();
            // 删除任务
//            if (dbHelper.haveRequestBody(taskId, reportType)) {
//                dbHelper.delRequestBodyByIdAndType(taskId, reportType);
//            }
            Intent intent = new Intent();
            intent.putExtra(AeaConstants.REPORT_TASK_ID, taskId);
            intent.putExtra(AeaConstants.REPORT_TYPE, reportType);
            setResult(RESULT_OK, intent);
            finish();
        } else if (AeaConstants.RESPONSE_CODE_600.equals(code)) {
            // 登陆超时跳转登陆界面
            Toast.makeText(this,
                    getResources().getString(R.string.inspect_checklogin_fail_timeout),
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, getResources().getString(R.string.inspect_report_submit_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public RequestBody mock() {
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
        return body;
    }

    public boolean validate() {
        if(TextUtils.isEmpty(tvInvestigateEndTime.getText())){
            Toast.makeText(this, "请选择考察完成时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (RequestBody.INVESTIGATE_TYPE_PERCEIVE == reportType) {
            if (TextUtils.isEmpty(etContact.getText())) {
                etContact.requestFocus();
                Toast.makeText(this, "请输入联系人", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etContactPhone.getText())) {
                etContactPhone.requestFocus();
                Toast.makeText(this, "请输入联系人电话", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etContactPosition.getText())) {
                etContactPosition.requestFocus();
                Toast.makeText(this, "请输入联系人职位", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etCompany.getText())) {
                etCompany.requestFocus();
                Toast.makeText(this, "请输入公司名称", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etCompanyNature.getText())) {
                etCompanyNature.requestFocus();
                Toast.makeText(this, "请输入公司公司性质", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etStaffNum.getText())) {
                etStaffNum.requestFocus();
                Toast.makeText(this, "请输入员工人数", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etBusinessArea.getText())) {
                etBusinessArea.requestFocus();
                Toast.makeText(this, "请输入营业用地面积", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (isHaveCompanyBoard == null) {
                Toast.makeText(this, "请选择是否有公司牌匾", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (isHaveCompanyNameAtOfficeArea == null) {
                Toast.makeText(this, "请选择办公区域是否有公司名称", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etServiceContent.getText())) {
                etServiceContent.requestFocus();
                Toast.makeText(this, "请输入业务内容", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etCompanyScale.getText())) {
                etCompanyScale.requestFocus();
                Toast.makeText(this, "请输入公司规模", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etProductionApparatus.getText())) {
                etProductionApparatus.requestFocus();
                Toast.makeText(this, "请输入生产器具", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etInventory.getText())) {
                etInventory.requestFocus();
                Toast.makeText(this, "请输入存货", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etInterviewContent.getText())) {
                etInterviewContent.requestFocus();
                Toast.makeText(this, "请输入访谈内容", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (TextUtils.isEmpty(etSummary.getText())) {
                etSummary.requestFocus();
                Toast.makeText(this, "请输入考察总结", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public RequestBody buildBody() {
        RequestBody body = new RequestBody();
        body.setTaskid(taskId);
        body.setInvestigateEndTime(DateTimePickDialogUtil.convertDateStrToNum(tvInvestigateEndTime.getText().toString()));
        body.setInvestigateType(reportType + "");
        body.setInvestigateAddr(address);
        body.setContactName(getEditTextString(etContact));
        body.setContactPhone(getEditTextString(etContactPhone));
        body.setContactPost(getEditTextString(etContactPosition));
        body.setCompanyName(getEditTextString(etCompany));
        body.setCompanyNature(getEditTextString(etCompanyNature));
        body.setStaffNumber(getEditTextString(etStaffNum));
        body.setBusinessArea(getEditTextString(etBusinessArea));
        if (isHaveCompanyBoard != null)
        {
            body.setIsHaveCompanyBoard(isHaveCompanyBoard.booleanValue());
        }
        if (isHaveCompanyNameAtOfficeArea != null) {
            body.setIsHaveCompanyNameAtOfficeArea(isHaveCompanyNameAtOfficeArea.booleanValue());
        }
        body.setServiceContent(getEditTextString(etServiceContent));
        body.setCompanyScale(getEditTextString(etCompanyScale));
        body.setProductionApparatus(getEditTextString(etProductionApparatus));
        body.setInventory(getEditTextString(etInventory));
        body.setInterviewContent(getEditTextString(etInterviewContent));
        body.setSummary(getEditTextString(etSummary));
        body.setOther(getEditTextString(etOther));
        if (photos != null && photos.size() > 0) {
            body.setImageSize(photos.size() + "");
            StringBuffer sb = new StringBuffer();
            for (PhotoModel photos : photos) {
                sb.append(photos.getOriginalPath())
                  .append(",");
            }
            String files = sb.toString();
            files = files.substring(0,files.lastIndexOf(","));
            body.setFiles(files);
        }
        return body;
    }

    public String getEditTextString(EditText et) {
        String str = null;
        if (et != null) {
            if (!TextUtils.isEmpty(et.getText())) {
                str = et.getText().toString();
            }
        }
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // save instance
        try {
            saveInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveInstance() throws Exception {
        RequestBody body = buildBody();
        if (dbHelper.haveRequestBody(taskId, reportType)) {
            dbHelper.updateRequestBody(body);
        } else {
            dbHelper.insertRequestBody(body);
        }
    }

    public void recoverInstance() {
        try {
            if (dbHelper.haveRequestBody(taskId, reportType)) {
                RequestBody body = dbHelper.getRequestBodyByTaskIdAndType(taskId, reportType);
                if (body != null) {
                    tvInvestigateEndTime.setText(DateTimePickDialogUtil.convertNumToDateStr(body.getInvestigateEndTime()));
                    etContact.setText(body.getContactName());
                    etContactPhone.setText(body.getContactPhone());
                    etContactPosition.setText(body.getContactPost());
                    etCompany.setText(body.getCompanyName());
                    etCompanyNature.setText(body.getCompanyNature());
                    etStaffNum.setText(body.getStaffNumber());
                    etBusinessArea.setText(body.getBusinessArea());
                    if(body.getIsHaveCompanyBoard()){
                        rbIsHaveCompanyBoardYes.setChecked(true);
                    }else{
                        rbIsHaveCompanyBoardNo.setChecked(true);
                    }
                    if(body.getIsHaveCompanyNameAtOfficeArea()){
                        rbIsHaveCompanyNameAtOfficeAreaYes.setChecked(true);
                    }else{
                        rbIsHaveCompanyNameAtOfficeAreaNo.setChecked(true);
                    }
                    etServiceContent.setText(body.getServiceContent());
                    etCompanyScale.setText(body.getCompanyScale());
                    etProductionApparatus.setText(body.getProductionApparatus());
                    etInventory.setText(body.getInventory());
                    etInterviewContent.setText(body.getInterviewContent());
                    etSummary.setText(body.getSummary());
                    etOther.setText(body.getOther());
                    try {
                        if(!TextUtils.isEmpty(body.getFiles())){
                            String[] files = body.getFiles().split(",");
                            if(files != null && files.length > 0){
                                photos = new ArrayList<PhotoModel>();
                                PhotoModel model;
                                for (String string : files) {
                                    model = new PhotoModel();
                                    model.setOriginalPath(string);
                                    photos.add(model);
                                }
                                String str = getResources().getString(R.string.inspect_report_photo_selected);
                                etPhotoUploder.setText(String.format(str, photos.size()));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

                String dateTime = sdf.format(calendar.getTime());
                
                tvInvestigateEndTime.setText(dateTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void startLoadingStatus() {
        super.startLoadingStatus();
        submiting = true;
        invalidateOptionsMenu();
    }
    
    @Override
    public void stopLoadingStatus() {
        super.stopLoadingStatus();
        submiting = false;
        invalidateOptionsMenu();
    }
    
    public void submit() {
        startLoadingStatus();

        if (!validate()) {
            stopLoadingStatus();
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                String requestJson = AeasyRequestUtil.getSubmitRequest(buildBody(),
                        InspectReportActivity.this);
                // MultipartEntity entity =
                // AeasyRequestUtil.imageFileUploadEntity(requestJson, photos);
                // requestJson =
                // "request=%7B%22aeasyapp%22%3A%7B%22method%22%3A%22user.login%22%2C%22private%22%3A%22private%22%2C%22requestBody%22%3A%7B%22password%22%3A%22a%22%2C%22usercode%22%3A%22a%22%7D%2C%22serialNumber%22%3A%22b0SustIKsa%22%2C%22version%22%3A%22v1.0%22%7D%7D";
                // AeaJsonResquest request = new
                // AeaJsonResquest(Request.Method.POST,
                // AeaConstants.REQUEST_URL, requestJson, this, this);
                // MultipartRequest request = new
                // MultipartRequest(Request.Method.POST,
                // AeaConstants.REQUEST_URL_TEST, InspectReportActivity.this,
                // InspectReportActivity.this);
                // request.setMultipartEntity(entity);
                MultiPartRequest request = new MultiPartRequest(Request.Method.POST,
                        AeaConstants.REQUEST_URL, InspectReportActivity.this,
                        InspectReportActivity.this);
                request.addStringUpload(AeaConstants.POST_PAR_REQUEST, requestJson);
                // request.addFileUpload(AeaConstants.POST_PAR_PROCESSDEFFILES,
                // );
                if (photos != null && photos.size() > 0) {
                    for (PhotoModel photo : photos) {
                        ByteArrayBodyWrapper wrapper = new ByteArrayBodyWrapper();
                        wrapper.setFileName(new File(photo.getOriginalPath()).getName());
                        wrapper.setBody(AeasyRequestUtil.compressImage(BitmapFactory
                                .decodeFile(photo.getOriginalPath())));
                        request.addBiaryUpload(wrapper.getFileName(), wrapper);
                    }
                }
                MyApplication.mRequestQueue.add(request);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.inspect_report_browser:
                // AeaCamera.getInstance().initialize(this);
                // AeaCamera.getInstance().choosePhoto(this, taskId);
                String dir = Environment.getExternalStorageDirectory()
                        + "/" + baselocalTempImgDir + "/" + taskId;
                Intent intent = new Intent(this, PhotoSelectorActivity.class);
                intent.putExtra(PhotoSelectorActivity.KEY_MAX, 15);
                intent.putExtra(PhotoSelectorActivity.KEY_DIR, dir);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, AeaCamera.REQUEST_PICK_PHOTO);
                break;
            case R.id.inspect_report_submit:
                submit();
                break;
            case R.id.inspect_report_time:
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(  
                        this, tvInvestigateEndTime.getText().toString());  
                dateTimePicKDialog.dateTimePicKDialog(tvInvestigateEndTime);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if ("main".equals(item.getTitle())) {
            try {
                saveInstance();
                Toast.makeText(this, "保存成功 ", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "保存数据异常", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }
}
