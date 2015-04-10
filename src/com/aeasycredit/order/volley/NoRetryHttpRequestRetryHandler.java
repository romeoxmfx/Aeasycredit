package com.aeasycredit.order.volley;

import java.io.IOException;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

public class NoRetryHttpRequestRetryHandler implements HttpRequestRetryHandler{

    @Override
    public boolean retryRequest(IOException paramIOException, int paramInt, HttpContext paramHttpContext) {
        return false;
    }
    
}
