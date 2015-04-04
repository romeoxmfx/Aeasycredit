package com.aeasycredit.order.volley.toolbox;

import com.aeasycredit.order.models.RequestWrapper;
import com.aeasycredit.order.volley.NetworkResponse;
import com.aeasycredit.order.volley.ParseError;
import com.aeasycredit.order.volley.Response;
import com.aeasycredit.order.volley.Response.ErrorListener;
import com.aeasycredit.order.volley.Response.Listener;
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
            RequestWrapper app = new Gson().fromJson(jsonString, RequestWrapper.class);
            return Response.success(app,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

}
