
package com.aeasycredit.order.volley.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.volley.DefaultRetryPolicy;
import com.aeasycredit.order.volley.MultipartEntity;
import com.aeasycredit.order.volley.NetworkResponse;
import com.aeasycredit.order.volley.ParseError;
import com.aeasycredit.order.volley.Request;
import com.aeasycredit.order.volley.Response;
import com.aeasycredit.order.volley.RetryPolicy;
import com.aeasycredit.order.volley.Response.ErrorListener;
import com.aeasycredit.order.volley.Response.Listener;
import com.google.gson.Gson;

import android.util.Log;

/**
 * MultipartRequest，返回的结果是String格式的
 */
public class MultipartRequest extends Request<RequestWrapper> {

    protected static final String PROTOCOL_CHARSET = "utf-8";

    private MultipartEntity mMultiPartEntity;
    private Listener<RequestWrapper> mListener;

    // private String requestBody;

    public MultipartRequest(int method, String url,
            Listener<RequestWrapper> listener,
            ErrorListener errorListener) {
        // super(method, url, params, errorListener);
        super(method, url, errorListener);
        // this.requestBody = requestBody;
        mListener = listener;
    }

    public void setMultipartEntity(MultipartEntity entity) {
        this.mMultiPartEntity = entity;
    }

    /**
     * @return
     */
    public MultipartEntity getMultiPartEntity() {
        return mMultiPartEntity;
    }

    @Override
    public String getBodyContentType() {
        return mMultiPartEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            // 将mMultiPartEntity中的参数写入到bos中
            mMultiPartEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e("", "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected void deliverResponse(RequestWrapper response) {
        mListener.onResponse(response);
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(1000 * 10,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }

    @Override
    protected Response<RequestWrapper> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            RequestWrapper app = new Gson().fromJson(jsonString, RequestWrapper.class);
            return Response.success(app,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

}
