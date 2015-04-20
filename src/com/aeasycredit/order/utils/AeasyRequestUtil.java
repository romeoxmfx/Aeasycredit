
package com.aeasycredit.order.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.aeasycredit.order.R;
import com.aeasycredit.order.models.RequestBody;
import com.aeasycredit.order.volley.MultipartEntity;
import com.aeasycredit.order.volley.toolbox.MultipartRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.photoselector.model.PhotoModel;

public class AeasyRequestUtil {

    public static String getLoginRequest(String name, String password) {
        RequestBody body = new RequestBody();
        body.setUsercode(name);
        body.setPassword(password);
        String requestJson = AeaConstants.POST_PAR_REQUEST + "=";
        // requestJson += "\"";
        requestJson += new Gson().toJson(AeasyRequestBuilder.getLoginAeasyapp(body));
        // requestJson += "\"";
        return requestJson;
    }

    public static String getCheckLoginRequest(String code, String uuid) {
        RequestBody body = new RequestBody();
        body.setUsercode(code);
        body.setUuid(uuid);
        String requestJson = AeaConstants.POST_PAR_REQUEST + "=";
        // requestJson += "\"";
        requestJson += new Gson().toJson(AeasyRequestBuilder.getCheckLoginAeasyapp(body));
        // requestJson += "\"";
        return requestJson;
    }

    public static String getSubmitRequest(RequestBody body, Context context) {
        // String requestJson = AeaConstants.POST_PAR_REQUEST+"=";
        String requestJson = "";
        // requestJson += "\"";
        String userCode = AeasySharedPreferencesUtil.getUserCode(context);
        String uuid = AeasySharedPreferencesUtil.getUuid(context);
        requestJson += new Gson().toJson(AeasyRequestBuilder
//        requestJson += new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(AeasyRequestBuilder
                .getSubmitAeasyapp(body, userCode, uuid));
        // requestJson += "\"";
        return requestJson;
    }

    public static MultipartEntity imageFileUploadEntity(String requestParams,
            List<PhotoModel> photos) {
        MultipartEntity multipartEntity = null;
        try {
            multipartEntity = new MultipartEntity();
            multipartEntity.addStringPart(AeaConstants.POST_PAR_REQUEST, requestParams);
            if (photos != null && photos.size() > 0) {
                for (PhotoModel photoModel : photos) {
                    // multipartEntity.addFilePart(AeaConstants.POST_PAR_PROCESSDEFFILES,
                    // new File(photoModel.getOriginalPath()));
                    // multipartEntity.addBinaryPart(AeaConstants.POST_PAR_PROCESSDEFFILES,
                    // compressImage(BitmapFactory.decodeFile(photoModel.getOriginalPath())),
                    // new
                    // File(photoModel.getOriginalPath()).getName());
                    multipartEntity.addImageBinaryPart(AeaConstants.POST_PAR_PROCESSDEFFILES,
                            compressImage(BitmapFactory.decodeFile(photoModel.getOriginalPath())),
                            new File(photoModel.getOriginalPath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartEntity;
    }

    public static MultipartEntity imageFileUploadEntity(String requestParams,
            List<PhotoModel> photos, Context mContext) {
        MultipartEntity multipartEntity = null;
        try {
            multipartEntity = new MultipartEntity();
            multipartEntity.addStringPart(AeaConstants.POST_PAR_REQUEST, requestParams);
            // if (photos != null && photos.size() > 0) {
            // for (PhotoModel photoModel : photos) {
            // multipartEntity.addFilePart(AeaConstants.POST_PAR_PROCESSDEFFILES,
            // new File(photoModel.getOriginalPath()));
            // multipartEntity.addBinaryPart(AeaConstants.POST_PAR_PROCESSDEFFILES,
            // compressImage(BitmapFactory.decodeFile(photoModel.getOriginalPath())),
            // new
            // File(photoModel.getOriginalPath()).getName());
            multipartEntity.addImageBinaryPart(AeaConstants.POST_PAR_PROCESSDEFFILES,
                    // compressImage(BitmapFactory.decodeFile(photoModel.getOriginalPath())),
                    Bitmap2Bytes(BitmapFactory.decodeResource(mContext.getResources(),
                            R.drawable.logo)),
                    "logo.png");
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartEntity;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    // 按比例大小和质量压缩图片
    public static byte[] compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        Log.i("munion", "Picture size before compression："
                + baos.toByteArray().length);
        if (baos.toByteArray().length / 1024 >= 300) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);// 压缩50%
        }
        ByteArrayInputStream isBm = new
                ByteArrayInputStream(baos.toByteArray());
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        // 开始读入图片
//        newOpts.inJustDecodeBounds = true;
//        Bitmap bm = BitmapFactory.decodeStream(isBm, null, newOpts);
//        newOpts.inJustDecodeBounds = false;
//        int w = newOpts.outWidth;
//        int h = newOpts.outHeight;
//        Log.i("munion", "Pictures of old size ：" + newOpts.outWidth + "x"
//                + newOpts.outHeight);
        // AppUtils appUtils = AlimmContext.getAliContext().getAppUtils();
//        int ww = 320;
//        int hh = 640;
        // int aw = appUtils.getAppWidth();
        // int ah = appUtils.getAppHeight();
        // Log.i("munion", "Screen size : " + aw + "x" + ah);
        // if (aw > 0 && ah > 0) {
        // ww = aw;
        // hh = ah;
        // }
//        Log.i("munion", "Pictures compression size : " + ww + "x" + hh);
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//        int be = 1;
//        if (w > h && w > ww) {
//            be = (int) (newOpts.outWidth / ww);
//        } else if (w < h && h > hh) {
//            be = (int) (newOpts.outHeight / hh);
//        }
//        if (be <= 0)
//            be = 1;
//        Log.i("munion", "Pictures Compression ratio ：" + be);
//        newOpts.inSampleSize = be;// 设置缩放比例
//        isBm = new ByteArrayInputStream(baos.toByteArray());
//        bm = BitmapFactory.decodeStream(isBm, null, newOpts);
//        bitmap.recycle();
//        bitmap = BitmapFactory.decodeStream(isBm);
        // 图片按质量压缩
//        ByteArrayOutputStream baost = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, baost);
        int options = 70;
        while (baos.toByteArray().length / 1024 >= 300) { //
        // 循环判断如果压缩后图片是否大于512kb,大于继续压缩
            baos.reset();
            options -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
//            if(isBm != null){
//                try {
//                    isBm.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            isBm = new ByteArrayInputStream(baos.toByteArray());
//            bitmap.recycle();
//            bitmap = BitmapFactory.decodeStream(isBm);
            
        }
        Log.i("munion", "Picture size after compression："
                + baos.toByteArray().length);
        return baos.toByteArray();
    }

    public static String getTaskListRequest(String userCode, String uuid) {
        RequestBody body = new RequestBody();
        body.setUsercode(userCode);
        body.setUuid(uuid);
        String requestJson = AeaConstants.POST_PAR_REQUEST + "=";
        // requestJson += "\"";
        requestJson += new Gson().toJson(AeasyRequestBuilder.getTaskList(userCode, uuid));
        // requestJson += "\"";
        return requestJson;
    }

    public static String checkoutResponseCode(String responseCode) {
        if (TextUtils.isEmpty(responseCode)) {
            return AeaConstants.RESPONSE_CODE_NULL;
        } else if (AeaConstants.RESPONSE_CODE_200.equals(responseCode)) {
            return AeaConstants.RESPONSE_CODE_200;
        } else if (AeaConstants.RESPONSE_CODE_600.equals(responseCode)) {
            return AeaConstants.RESPONSE_CODE_600;
        } else if (AeaConstants.RESPONSE_CODE_700.equals(responseCode)) {
            return AeaConstants.RESPONSE_CODE_700;
        } else {
            return AeaConstants.RESPONSE_CODE_NULL;
        }
    }
}
