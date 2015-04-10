package com.aeasycredit.order.volley;

public class NoRetryPolicy implements RetryPolicy {
    
    @Override
    public int getCurrentTimeout() {
        return 0;
    }
    
    @Override
    public int getCurrentRetryCount() {
        return 0;
    }
    
    @Override
    public void retry(VolleyError error) throws VolleyError {
        throw error;
    }
    
}
