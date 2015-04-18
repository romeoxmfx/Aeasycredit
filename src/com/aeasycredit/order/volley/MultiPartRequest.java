/**
 * Copyright 2013 Mani Selvaraj
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.aeasycredit.order.volley;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.text.TextUtils;

import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.volley.Response.ErrorListener;
import com.aeasycredit.order.volley.Response.Listener;
import com.aeasycredit.order.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

/**
 * MultipartRequest - To handle the large file uploads. Extended from
 * JSONRequest. You might want to change to StringRequest based on your response
 * type.
 * 
 */
public class MultiPartRequest extends Request<RequestWrapper> {
    
    /* To hold the parameter name and the File to upload */
    private Map<String, File> fileUploads = new HashMap<String, File>();
    
    private Map<String, ByteArrayBodyWrapper> binaryUploads = new HashMap<String, ByteArrayBodyWrapper>();
    
    /* To hold the parameter name and the string content to upload */
    private Map<String, String> stringUploads = new HashMap<String, String>();
    
    private Map<String, String> headers = new HashMap<String, String>();
    
    private final Listener<RequestWrapper> mListener;
    
    /**
     * Creates a new request.
     * 
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is
     *            allowed and indicates no parameters will be posted along with
     *            request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public MultiPartRequest(int method, String url,Listener<RequestWrapper> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
//        isMultiPartRequest = true;
        setRetryPolicy(new NoRetryPolicy());
        this.mListener = listener;
        isMultiPartRequest = true;
    }
    
    public void addFileUpload(String param, File file) {
        fileUploads.put(param, file);
    }
    
    public void addBiaryUpload(String param,ByteArrayBodyWrapper wrapper){
        binaryUploads.put(param, wrapper);
    }
    
    public void addStringUpload(String param, String content) {
        stringUploads.put(param, content);
    }
    
    public Map<String, File> getFileUploads() {
        return fileUploads;
    }
    
    public Map<String, ByteArrayBodyWrapper> getByteArray() {
        return binaryUploads;
    }
    
    public Map<String, String> getStringUploads() {
        return stringUploads;
    }
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }
    
    public void setHeader(String title, String content) {
        headers.put(title, content);
    }
    
    @Override
    protected void deliverResponse(RequestWrapper response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<RequestWrapper> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            JSONObject jo = new JSONObject(jsonString);
            JSONObject joAea = jo.optJSONObject("aeasyapp");
            String resbody = joAea.optString("responseBody");
            if(TextUtils.isEmpty(resbody)){
                joAea.remove("responseBody");
                jo.put("aeasyapp", joAea);
                jsonString = jo.toString();
            }
//            jsonString = "{\"aeasyapp\":{\"serialNumber\":\"CwpQal0cIC\",\"version\":\"v1.0\",\"method\":\"investigate.subimttask\",\"usercode\":\"0271\",\"uuid\":\"0271\",\"responseCode\":200,\"responseBody\":{\"uuid\":\"0271\"}}";
            RequestWrapper app = new Gson().fromJson(jsonString, RequestWrapper.class);
            return Response.success(app,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
}