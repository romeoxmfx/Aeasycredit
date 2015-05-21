package com.aeasycredit.order.volley.toolbox;

import android.util.Log;

import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.volley.NetworkResponse;
import com.aeasycredit.order.volley.ParseError;
import com.aeasycredit.order.volley.Response;
import com.aeasycredit.order.volley.Response.ErrorListener;
import com.aeasycredit.order.volley.Response.Listener;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class AeaJsonResquest extends JsonRequest<RequestWrapper> {
//    private static final String PROTOCOL_CONTENT_TYPE =
//          String.format("application/x-www-form-urlencoded");

    public AeaJsonResquest(int method, String url, String requestBody, Listener<RequestWrapper> listener,
            ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    @Override
    protected Response<RequestWrapper> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            Log.i(AeaConstants.TAG, jsonString);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
//            jsonString = new ObjectMapper().writeValueAsString(jsonString);
            RequestWrapper app = mapper.readValue(jsonString, RequestWrapper.class);
//            app.getAeasyapp().setResponseCode(AeaConstants.RESPONSE_CODE_601);
//            RequestWrapper app = new Gson().fromJson(jsonString, RequestWrapper.class);
            return Response.success(app,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

}
