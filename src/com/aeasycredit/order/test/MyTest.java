package com.aeasycredit.order.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import com.aeasycredit.order.database.DataBaseHelper;
import com.aeasycredit.order.models.RequestBody;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.utils.AeasyRequestUtil;

import android.graphics.BitmapFactory;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

public class MyTest extends AndroidTestCase {
    public static final String taskId = "asdfsdfsafsafsf323232";
    
    public RequestBody getBody(int type){
        RequestBody body = new RequestBody();
        body.setTaskid(taskId);
        body.setInvestigateType(type+"");
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

    public void testInsert(){
        Assert.assertTrue(new DataBaseHelper(mContext).insertRequestBody(getBody(1)));
    }
    
    public void testSelectAll(){
        List<RequestBody> list = new DataBaseHelper(mContext).getRequestBodyList();
        Assert.assertNotNull(list);
        for (RequestBody requestBody : list) {
            Log.i(AeaConstants.TAG, requestBody.getTaskid() + requestBody.getInvestigateType() + requestBody.getIsHaveCompanyBoard() + requestBody.getIsHaveCompanyNameAtOfficeArea());
        }
    }
    
    public void testSelectByTaskAndType(){
        RequestBody requestBody = new DataBaseHelper(mContext).getRequestBodyByTaskIdAndType(taskId, 0);
        Assert.assertNotNull(requestBody);
        Log.i(AeaConstants.TAG, requestBody.getTaskid() + requestBody.getInvestigateType() + requestBody.getIsHaveCompanyBoard() + requestBody.getIsHaveCompanyNameAtOfficeArea());
    }
    
    public void testhaveRequestBody(){
        boolean succ = new DataBaseHelper(mContext).haveRequestBody(taskId, 1);
        Assert.assertTrue(succ);
    }
    
    public void updateRequestBody(){
        RequestBody body = getBody(0); 
        body.setIsHaveCompanyBoard(false);
        body.setIsHaveCompanyNameAtOfficeArea(false);
        boolean succ = new DataBaseHelper(mContext).updateRequestBody(body);
        Assert.assertTrue(succ);
    }
    
    public void testdelRequestBodyByIdAndType(){
        boolean succ = new DataBaseHelper(mContext).delRequestBodyByIdAndType(taskId, 0);
        Assert.assertTrue(succ);
    }
    
    public void testDelAll(){
        boolean succ = new DataBaseHelper(mContext).clearTable();
        Assert.assertTrue(succ);
    }
    
    public void testCompress(){
        File dir = new File(Environment.getExternalStorageDirectory()
                + "/" + "aeasy" + "/" + "taskid142950269620350531000/" + "20150420120638.jpg");
        File outFile = new File(Environment.getExternalStorageDirectory()
                + "/" + "aeasy" + "/" + "taskid142950269620350531000/" + "test.jpg");
        if(outFile.exists()){
            outFile.delete();
        }
        byte[] buffer = AeasyRequestUtil.compressImage(BitmapFactory.decodeFile(dir.getAbsolutePath()));
        FileOutputStream fot = null;
        try {
             fot = new FileOutputStream(outFile);
             fot.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                fot.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        Log.i(AeaConstants.TAG, "fileSize = " + outFile.length() /1024);
    }
}
