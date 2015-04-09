package com.aeasycredit.order.utils;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.aeasycredit.order.models.RequestBody;
import com.aeasycredit.order.volley.MultipartEntity;
import com.aeasycredit.order.volley.toolbox.MultipartRequest;
import com.google.gson.Gson;
import com.photoselector.model.PhotoModel;

public class AeasyRequestUtil {

    public static String getLoginRequest(String name,String password){
        RequestBody body = new RequestBody();
        body.setUsercode(name);
        body.setPassword(password);
        String requestJson = AeaConstants.POST_PAR_REQUEST+"=";
//        requestJson += "\"";
        requestJson += new Gson().toJson(AeasyRequestBuilder.getLoginAeasyapp(body));
//        requestJson += "\"";
        return requestJson;
    }
    
    public static String getCheckLoginRequest(String code,String uuid){
        RequestBody body = new RequestBody();
        body.setUsercode(code);
        body.setUuid(uuid);
        String requestJson = AeaConstants.POST_PAR_REQUEST+"=";
//        requestJson += "\"";
        requestJson += new Gson().toJson(AeasyRequestBuilder.getCheckLoginAeasyapp(body));
//        requestJson += "\"";
        return requestJson;
    }
    
    public static String getSubmitRequest(RequestBody body, Context context){
//        String requestJson = AeaConstants.POST_PAR_REQUEST+"=";
        String requestJson = "";
//        requestJson += "\"";
        String userCode = AeasySharedPreferencesUtil.getUserCode(context);
        String uuid = AeasySharedPreferencesUtil.getUuid(context);
        requestJson += new Gson().toJson(AeasyRequestBuilder.getSubmitAeasyapp(body,userCode,uuid));
//        requestJson += "\"";
        return requestJson;
    }
    
    public static MultipartEntity imageFileUploadEntity(String requestParams,List<PhotoModel> photos){
        MultipartEntity multipartEntity = null;
        try {
            multipartEntity = new MultipartEntity();
            multipartEntity.addStringPart(AeaConstants.POST_PAR_REQUEST, requestParams);
            if(photos != null && photos.size() > 0){
                for (PhotoModel photoModel : photos) {
                    multipartEntity.addFilePart(AeaConstants.POST_PAR_PROCESSDEFFILES, new File(photoModel.getOriginalPath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartEntity;
    }
    
    public static String getTaskListRequest(String userCode,String uuid){
        RequestBody body = new RequestBody();
        body.setUsercode(userCode);
        body.setUuid(uuid);
        String requestJson = AeaConstants.POST_PAR_REQUEST+"=";
//        requestJson += "\"";
        requestJson += new Gson().toJson(AeasyRequestBuilder.getTaskList(userCode, uuid));
//        requestJson += "\"";
        return requestJson;
    }
    
    public static String checkoutResponseCode(String responseCode){
        if(TextUtils.isEmpty(responseCode)){
            return AeaConstants.RESPONSE_CODE_NULL;
        }else if(AeaConstants.RESPONSE_CODE_200.equals(responseCode)){
            return AeaConstants.RESPONSE_CODE_200;
        }else if(AeaConstants.RESPONSE_CODE_600.equals(responseCode)){
            return AeaConstants.RESPONSE_CODE_600;
        }else if(AeaConstants.RESPONSE_CODE_700.equals(responseCode)){
            return AeaConstants.RESPONSE_CODE_700;
        }else{
            return AeaConstants.RESPONSE_CODE_200;
        }
    }
}
