
package com.aeasycredit.order.utils;

import java.util.Random;

import android.content.Context;

import com.aeasycredit.order.application.MyApplication;
import com.aeasycredit.order.models.Aeasyapp;
import com.aeasycredit.order.models.RequestBody;
import com.aeasycredit.order.models.RequestWrapper;
import com.google.gson.Gson;

public class AeasyRequestBuilder {
    private Aeasyapp aeasyapp;

    private static char ch[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w',
            'x', 'y', 'z', '0', '1'
    };// 最后又重复两个0和1，因为需要凑足数组长度为64

    private static Random random = new Random();

    // 生成指定长度的随机字符串
    public static synchronized String createRandomString(int length) {
        if (length > 0) {
            int index = 0;
            char[] temp = new char[length];
            int num = random.nextInt();
            for (int i = 0; i < length % 5; i++) {
                temp[index++] = ch[num & 63];// 取后面六位，记得对应的二进制是以补码形式存在的。
                num >>= 6;// 63的二进制为:111111
                // 为什么要右移6位？因为数组里面一共有64个有效字符。为什么要除5取余？因为一个int型要用4个字节表示，也就是32位。
            }
            for (int i = 0; i < length / 5; i++) {
                num = random.nextInt();
                for (int j = 0; j < 5; j++) {
                    temp[index++] = ch[num & 63];
                    num >>= 6;
                }
            }
            return new String(temp, 0, length);
        }
        else if (length == 0) {
            return "";
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public AeasyRequestBuilder() {
        aeasyapp = new Aeasyapp();
        aeasyapp.setSerialNumber(createRandomString(10));
        aeasyapp.setVersion(AeaConstants.VERSION);
        aeasyapp.setAppVersion(MyApplication.appVersion);
    }

    public AeasyRequestBuilder setPrivate(String mprivate) {
        aeasyapp.setMprivate(mprivate);
        return this;
    }

    public AeasyRequestBuilder setVersion(String version) {
        aeasyapp.setVersion(version);
        return this;
    }

    public AeasyRequestBuilder setMethod(String method) {
        aeasyapp.setMethod(method);
        return this;
    }

    public AeasyRequestBuilder setUserCode(String userCode) {
        aeasyapp.setLoginname(userCode);
        return this;
    }

    public AeasyRequestBuilder setUuid(String uuid) {
        aeasyapp.setUuid(uuid);
        return this;
    }

    public AeasyRequestBuilder setRequestBody(RequestBody requestBody) {
        aeasyapp.setRequestBody(requestBody);
        return this;
    }

    public AeasyRequestBuilder setSerNum(String serNum) {
        aeasyapp.setSerialNumber(serNum);
        return this;
    }

    public Aeasyapp getAeasyapp() {
        return aeasyapp;
    }

    public static RequestWrapper getSubmitAeasyapp(RequestBody requestBody,String userCode,String uuid) {
        RequestWrapper wrapper = new RequestWrapper();
        Aeasyapp app = new AeasyRequestBuilder()
                .setMethod(AeaConstants.METHOD_SUBMIT)
                // .setPrivate(AeaConstants.PRIVATE_LOGIN)
                .setRequestBody(requestBody)
                .setUserCode(userCode)
                .setUuid(uuid)
                // .setSerNum("8ed5a52093c7445c956da09dd3dc1cc4")
                .getAeasyapp();
        wrapper.setAeasyapp(app);
        return wrapper;
    }

    public static RequestWrapper getLoginAeasyapp(RequestBody requestBody) {

        // RequestBody requestBody = new RequestBody();
        // requestBody.setUsercode(name);
        // requestBody.setPassword(password);
        RequestWrapper wrapper = new RequestWrapper();
        Aeasyapp app = new AeasyRequestBuilder()
                .setMethod(AeaConstants.METHOD_LOGIN)
                // .setPrivate(AeaConstants.PRIVATE_LOGIN)
                .setRequestBody(requestBody)
                .setUserCode(requestBody.getLoginname())
                // .setUuid(requestBody.getUsercode())
                // .setSerNum("8ed5a52093c7445c956da09dd3dc1cc4")
                .getAeasyapp();
        wrapper.setAeasyapp(app);
        return wrapper;
    }

    public static RequestWrapper getCheckLoginAeasyapp(RequestBody requestBody) {

        // RequestBody requestBody = new RequestBody();
        // requestBody.setUsercode(name);
        // requestBody.setPassword(password);
        RequestWrapper wrapper = new RequestWrapper();
        Aeasyapp app = new AeasyRequestBuilder()
                .setMethod(AeaConstants.METHOD_CHECK_LOGIN)
                // .setPrivate(AeaConstants.PRIVATE_CHECKOUK_LOGIN)
                .setRequestBody(requestBody)
                .setUserCode(requestBody.getLoginname())
                .setUuid(requestBody.getUuid())
                // .setSerNum("8ed5a52093c7445c956da09dd3dc1cc4")
                .getAeasyapp();
        wrapper.setAeasyapp(app);
        return wrapper;
    }

    public static RequestWrapper getTaskList(String userCode, String uuid) {

        // RequestBody requestBody = new RequestBody();
        // requestBody.setUsercode(name);
        // requestBody.setPassword(password);
        RequestWrapper wrapper = new RequestWrapper();
        Aeasyapp app = new AeasyRequestBuilder()
                .setMethod(AeaConstants.METHOD_TASK_REQUEST)
                // .setPrivate(AeaConstants.PRIVATE_CHECKOUK_LOGIN)
                // .setRequestBody(requestBody)
                .setUserCode(userCode)
                .setUuid(uuid)
                // .setSerNum("8ed5a52093c7445c956da09dd3dc1cc4")
                .getAeasyapp();
        wrapper.setAeasyapp(app);
        return wrapper;
    }
}
