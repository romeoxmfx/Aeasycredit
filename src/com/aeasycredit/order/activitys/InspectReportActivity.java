
package com.aeasycredit.order.activitys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.aeasycredit.order.utils.CustomCrashHandler;
import com.aeasycredit.order.utils.DateTimePickDialogUtil;
import com.aeasycredit.order.volley.ByteArrayBodyWrapper;
import com.aeasycredit.order.volley.MultiPartRequest;
import com.aeasycredit.order.volley.Request;
import com.aeasycredit.order.volley.VolleyError;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoSelectorActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.AsyncTask;
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
    // String endTime;

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
    TextView tvInvestigateStartTime;
    RadioButton rbIsHaveCompanyBoardYes;
    RadioButton rbIsHaveCompanyBoardNo;
    RadioButton rbIsHaveCompanyNameAtOfficeAreaYes;
    RadioButton rbIsHaveCompanyNameAtOfficeAreaNo;

    LinearLayout llContactName;
    LinearLayout llContactPhone;
    LinearLayout llContactPost;
    LinearLayout llInterviewContent;
    LinearLayout llSummary;

    Boolean isHaveCompanyBoard = false;
    Boolean isHaveCompanyNameAtOfficeArea = false;

    DataBaseHelper dbHelper;
    boolean doNotrecoverFiles = false;

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

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // menu.add("main")
    // .setIcon(R.drawable.btn_menu_save_selector)
    // .setOnMenuItemClickListener(this)
    // .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    // return super.onCreateOptionsMenu(menu);
    // }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (submiting) {
            menu.clear();
        } else {
            menu.add("main")
                    .setIcon(R.drawable.save_selector)
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
        tvInvestigateStartTime = (TextView) findViewById(R.id.inspect_report_start_time);

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
        tvInvestigateEndTime.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        tvInvestigateStartTime.setOnClickListener(this);
        tvInvestigateStartTime.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线

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
            tvCompanyTitle.setText(tvCompanyTitle.getText() + "：");
            tvCompanyNatureTitle.setText(tvCompanyNatureTitle.getText() + "：");
            tvStaffNumTitle.setText(tvStaffNumTitle.getText() + "：");
            tvBusinessAreaTitle.setText(tvBusinessAreaTitle.getText() + "：");
            tvIsHaveCompanyBoardTitle.setText(tvIsHaveCompanyBoardTitle.getText() + "：");
            tvIsHaveCompanyNameAtOfficeAreaTitle.setText(tvIsHaveCompanyNameAtOfficeAreaTitle
                    .getText() + "：");
        } else {
            llSummary.setVisibility(View.GONE);
            tvCompanyTitle.setText(tvCompanyTitle.getText() + "(*)：");
            tvCompanyNatureTitle.setText(tvCompanyNatureTitle.getText() + "(*)：");
            tvStaffNumTitle.setText(tvStaffNumTitle.getText() + "(*)：");
            tvBusinessAreaTitle.setText(tvBusinessAreaTitle.getText() + "(*)：");
            tvIsHaveCompanyBoardTitle.setText(tvIsHaveCompanyBoardTitle.getText() + "(*)：");
            tvIsHaveCompanyNameAtOfficeAreaTitle.setText(tvIsHaveCompanyNameAtOfficeAreaTitle
                    .getText() + "(*)：");
            tvServiceContentTitle.setText(tvServiceContentTitle.getText() + "(*)");
            tvCompanyScaleTitle.setText(tvCompanyScaleTitle.getText() + "(*)");
            tvProductionApparatusTitle.setText(tvProductionApparatusTitle.getText() + "(*)");
            tvInventoryTitle.setText(tvInventoryTitle.getText() + "(*)");
            tvOtherTitle.setText(tvOtherTitle.getText());
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        stopLoadingStatus();
        Toast.makeText(this, getResources().getString(R.string.inspect_report_submit_fail),
                Toast.LENGTH_SHORT).show();
        CustomCrashHandler.savaInfoToSD(this,error);
    }

    @Override
    public void onResponse(RequestWrapper response) {
        // super.onResponse(response);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (onResponseBase(response)) {
            return;
        }
        // stopLoadingStatus();
        // String code =
        // AeasyRequestUtil.checkoutResponseCode(response.getAeasyapp()
        // .getResponseCode());
        String code = response.getAeasyapp().getResponseCode();
        if (AeaConstants.RESPONSE_CODE_200.equals(code)) {
            Toast.makeText(this,
                    getResources().getString(R.string.inspect_report_submit_succ),
                    Toast.LENGTH_SHORT).show();
            // 删除任务
            // if (dbHelper.haveRequestBody(taskId, reportType)) {
            // dbHelper.delRequestBodyByIdAndType(taskId, reportType);
            // }
            Intent intent = new Intent();
            intent.putExtra(AeaConstants.REPORT_TASK_ID, taskId);
            intent.putExtra(AeaConstants.REPORT_TYPE, reportType);
            setResult(RESULT_OK, intent);
            finish();
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
        // Toast.makeText(this,
        // getResources().getString(R.string.inspect_report_submit_fail),
        // Toast.LENGTH_SHORT).show();
        // }
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
        if (TextUtils.isEmpty(tvInvestigateEndTime.getText())) {
            Toast.makeText(this, "请选择考察完成时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(tvInvestigateStartTime.getText())) {
            Toast.makeText(this, "请选择考察开始时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tvInvestigateEndTime.getText().toString()
                .compareTo(tvInvestigateStartTime.getText().toString()) <= 0) {
            Toast.makeText(this, "考察完成时间必须大于考察开始时间", Toast.LENGTH_SHORT).show();
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
        if (!validateLength(etContact, 15, "联系人超过15个字符"))
            return false;
        if (!validateLength(etContactPhone, 11, "联系电话超过11位"))
            return false;
        if (!validateLength(etContactPosition, 15, "联系人职位超过15个字符"))
            return false;
        if (!validateLength(etCompany, 15, "公司名称超过15个字符"))
            return false;
        if (!validateLength(etCompanyNature, 25, "公司性质超过25个字符"))
            return false;
        if (!validateLength(etStaffNum, 6, "员工人数超过6位"))
            return false;
        if (!validateLength(etBusinessArea, 8, "营业用地面积超过8位"))
            return false;
        if (!validateLength(etServiceContent, 5000, "业务内容超过5000个字符"))
            return false;
        if (!validateLength(etCompanyScale, 5000, "公司规模超过5000个字符"))
            return false;
        if (!validateLength(etProductionApparatus, 5000, "生产器具超过5000个字符"))
            return false;
        if (!validateLength(etInventory, 5000, "存货超过5000个字符"))
            return false;
        if (!validateLength(etInterviewContent, 5000, "访谈内容超过5000个字符"))
            return false;
        if (!validateLength(etSummary, 5000, "考察总结超过5000个字符"))
            return false;
        if (!validateLength(etOther, 5000, "其他补充事项超过5000个字符"))
            return false;
        if(photos != null && photos.size() > 0){
            if(photos.size() > 100){
                Toast.makeText(this, "上传图片数不能大于100张", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public boolean validateLength(EditText et, int length, String text) {
        if (!TextUtils.isEmpty(et.getText())) {
            int len = et.getText().length();
            if (len > length) {
                et.requestFocus();
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public RequestBody buildBody() {
        RequestBody body = new RequestBody();
        body.setTaskid(taskId);
        body.setInvestigateEndTime(DateTimePickDialogUtil.convertDateStrToNum(tvInvestigateEndTime
                .getText().toString()));
        body.setInvestigateStartTime(DateTimePickDialogUtil
                .convertDateStrToNum(tvInvestigateStartTime.getText().toString()));
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
            files = files.substring(0, files.lastIndexOf(","));
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
                    tvInvestigateEndTime.setText(DateTimePickDialogUtil.convertNumToDateStr(body
                            .getInvestigateEndTime()));
                    tvInvestigateStartTime.setText(DateTimePickDialogUtil.convertNumToDateStr(body
                            .getInvestigateStartTime()));
                    if (TextUtils.isEmpty(tvInvestigateEndTime.getText())) {
                        Calendar calendar = Calendar.getInstance();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

                        String dateTime = sdf.format(calendar.getTime());

                        tvInvestigateEndTime.setText(dateTime);
                    }
                    if (TextUtils.isEmpty(tvInvestigateStartTime.getText())) {
                        Calendar calendar = Calendar.getInstance();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

                        String dateTime = sdf.format(calendar.getTime());

                        tvInvestigateStartTime.setText(dateTime);
                    }
                    etContact.setText(body.getContactName());
                    etContactPhone.setText(body.getContactPhone());
                    etContactPosition.setText(body.getContactPost());
                    etCompany.setText(body.getCompanyName());
                    etCompanyNature.setText(body.getCompanyNature());
                    etStaffNum.setText(body.getStaffNumber());
                    etBusinessArea.setText(body.getBusinessArea());
                    if (body.getIsHaveCompanyBoard()) {
                        rbIsHaveCompanyBoardYes.setChecked(true);
                    } else {
                        rbIsHaveCompanyBoardNo.setChecked(true);
                    }
                    if (body.getIsHaveCompanyNameAtOfficeArea()) {
                        rbIsHaveCompanyNameAtOfficeAreaYes.setChecked(true);
                    } else {
                        rbIsHaveCompanyNameAtOfficeAreaNo.setChecked(true);
                    }
                    etServiceContent.setText(body.getServiceContent());
                    etCompanyScale.setText(body.getCompanyScale());
                    etProductionApparatus.setText(body.getProductionApparatus());
                    etInventory.setText(body.getInventory());
                    etInterviewContent.setText(body.getInterviewContent());
                    etSummary.setText(body.getSummary());
                    etOther.setText(body.getOther());
                    if (!doNotrecoverFiles) {
                        try {
                            if (!TextUtils.isEmpty(body.getFiles())) {
                                String[] files = body.getFiles().split(",");
                                if (files != null && files.length > 0) {
                                    photos = new ArrayList<PhotoModel>();
                                    PhotoModel model;
                                    for (String string : files) {
                                        model = new PhotoModel();
                                        model.setOriginalPath(string);
                                        photos.add(model);
                                    }
                                    String str = getResources().getString(
                                            R.string.inspect_report_photo_selected);
                                    etPhotoUploder.setText(String.format(str, photos.size()));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    doNotrecoverFiles = false;
                }
            } else {
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

                String dateTime = sdf.format(calendar.getTime());

                tvInvestigateEndTime.setText(dateTime);

                tvInvestigateStartTime.setText(dateTime);

                isHaveCompanyBoard = false;
                isHaveCompanyNameAtOfficeArea = false;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        startLoadingStatus();

        if (!validate()) {
            stopLoadingStatus();
            return;
        }

        new AsyncTask<String, Integer, MultiPartRequest>() {
            @Override
            protected MultiPartRequest doInBackground(String... params) {
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
                        // wrapper.setBody(AeasyRequestUtil.compressImage(BitmapFactory
                        // .decodeFile(photo.getOriginalPath())));
                        // wrapper.setBody(getPhotoByte(photo.getOriginalPath()));
//                        wrapper.setBody(getByteArrayFromFile(photo.getOriginalPath()));
                        wrapper.setBody(comporess(photo.getOriginalPath()));
                        request.addBiaryUpload(wrapper.getFileName(), wrapper);
                    }
                }

                return request;
            }

            protected void onPostExecute(MultiPartRequest result) {
                if (result != null) {
//                     stopLoadingStatus();
                    MyApplication.mRequestQueue.add(result);
                }
            };
        }.execute();
    }

    public byte[] getByteArrayFromFile(String fileName) {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        File file = null;
        try {
            file = new File(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (!file.exists() || !file.isFile() || !file.canRead()) {
            return null;
        }

        byte[] byteArray = null;

        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream();

            int count;
            byte buffer[] = new byte[1024];
            while ((count = fis.read(buffer)) > 0) {
                baos.write(buffer, 0, count);
            }
            byteArray = baos.toByteArray();
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return byteArray;
    }
    
    public Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();            //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);            //这里一定要将其设置回false，因为之前我们将其设置成了true
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static int computeSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
     
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
     
        return roundedSize;
    }
    
    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
     
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));
     
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
     
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
    
    public byte[] comporess(String path){
        Bitmap bitmap = null;
        byte[] bytes = null;
        File f = null;
        try {
            if(TextUtils.isEmpty(path)){
                return null;
            }else{
                f = new File(path);
            }
            if(f.length() > 3 * 1024){
                bitmap = getBitmapFromFile(f, 1080, 1920);
                int degree = readPictureDegree(f.getAbsolutePath());
                bitmap = rotateBitmap(bitmap,degree);
                bytes = compressImage(bitmap,300);
            }else{
                bytes = getByteArrayFromFile(f.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(bitmap != null && !bitmap.isRecycled()){
                    bitmap.recycle();
                    System.gc();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bytes;
    }
    
    private byte[] compressImage(Bitmap image, int size) {
//      Bitmap bitmap = null;
      ByteArrayInputStream isBm = null;
      ByteArrayOutputStream baos = null;
//      FileOutputStream fos = null;
      try {
          baos = new ByteArrayOutputStream();
          // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
          image.compress(Bitmap.CompressFormat.JPEG, 90, baos);
          int options = 100;
          // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
          while (baos.toByteArray().length / 1024 > size) {
              // 重置baos即清空baos
              baos.reset();
              // 每次都减少10
              options -= 10;
              // 这里压缩options%，把压缩后的数据存放到baos中
              image.compress(Bitmap.CompressFormat.JPEG, options, baos);
          }
          // 把压缩后的数据baos存放到ByteArrayInputStream中
//          isBm = new ByteArrayInputStream(baos.toByteArray());
          // 把ByteArrayInputStream数据生成图片
//          bitmap = BitmapFactory.decodeStream(isBm, null, null);
//          ByteArrayOutputStream ba = new ByteArrayOutputStream();
//          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ba);
//          int a = ba.toByteArray().length;
//          fos = new FileOutputStream(f);
//          fos.write(baos.toByteArray());
//          fos.flush();
      } catch (Exception e) {
          e.printStackTrace();
      } finally{
          try {
              if(baos != null){
                  baos.close();
              }
              if(isBm != null){
                  isBm.close();
              }
//              if(fos != null){
//                  fos.close();
//              }
              if(image != null && !image.isRecycled()){
                  image.recycle();
              }
          } catch (Exception e2) {
              e2.printStackTrace();
          }
      }
      return baos.toByteArray();
  }
    
    /**
     * 读取图片属性：旋转的角度
     * 
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     * 
     * @param bitmap 原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bmp;
    }

    public byte[] getPhotoByte(String filePath) {
        Bitmap bitmap = null;
        ByteArrayOutputStream baos = null;
        try {
            if (TextUtils.isEmpty(filePath)) {
                return null;
            }
            bitmap = BitmapFactory.decodeFile(filePath);
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();
                return bytes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
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
                if (photos != null && photos.size() > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (PhotoModel photos : photos) {
                        sb.append(photos.getOriginalPath())
                                .append(",");
                    }
                    String seledtedFiles = sb.toString();
                    seledtedFiles = seledtedFiles.substring(0, seledtedFiles.lastIndexOf(","));
                    intent.putExtra(PhotoSelectorActivity.KEY_SELECTED, seledtedFiles);
                }
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
            case R.id.inspect_report_start_time:
                DateTimePickDialogUtil dateTimePicKDialogStart = new DateTimePickDialogUtil(
                        this, tvInvestigateStartTime.getText().toString());
                dateTimePicKDialogStart.dateTimePicKDialog(tvInvestigateStartTime);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AeaCamera.REQUEST_PICK_PHOTO) {// selected
                                                                                     // image
            doNotrecoverFiles = true;
            if (data != null && data.getExtras() != null) {
                @SuppressWarnings("unchecked")
                List<PhotoModel> photosSel = (List<PhotoModel>) data.getExtras().getSerializable(
                        "photos");
                if (photosSel != null && photosSel.size() > 0) {
                    this.photos = photosSel;
                    String str = getResources().getString(R.string.inspect_report_photo_selected);
                    etPhotoUploder.setText(String.format(str, photos.size()));
                }
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == AeaCamera.REQUEST_PICK_PHOTO) {
            doNotrecoverFiles = true;
            this.photos = null;
            etPhotoUploder.setText("");
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
