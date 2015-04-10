/**
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.LineParser;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.CharArrayBuffer;

import com.aeasycredit.order.utils.AeaConstants;
import com.aeasycredit.order.volley.Request.Method;
import com.aeasycredit.order.volley.toolbox.HttpStack;

/**
 * Custom implementation of com.android.volley.toolboox.HttpStack Uses apache
 * HttpClient-4.2.5 jar to take care of . You can download it from here
 * http://hc.apache.org/downloads.cgi
 * 
 * 
 */
public class MultiPartHttpStack implements HttpStack {
    private final static int TIMEOUT = 5 * 1000;
    private final static int SOCKET_BUFFER_SIZE = 8192;
    private final static String HEADER_CONTENT_TYPE = "Content-Type";
    private final static String HEADER_CONTENT_TYPE_BODY = "application/x-www-form-urlencoded";
    private final static String DEFAULT_PARAMS_ENCODING = "UTF-8";
    // protected final AbstractHttpClient mClient;
    protected final HttpClientBuilder builder;
    
    public MultiPartHttpStack() {
        // mClient = createHttpClient();
        builder = createHttpClientBuilder();
    }
    
    public MultiPartHttpStack(AbstractHttpClient client) {
        // mClient = client;
        builder = createHttpClientBuilder();
    }
    
    public void setCookieStore(CookieStore cookieStore) {
        // mClient.setCookieStore(cookieStore);
        builder.setDefaultCookieStore(cookieStore);
    }
    
    private static void addHeaders(HttpUriRequest httpRequest, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            httpRequest.setHeader(key, headers.get(key));
        }
    }
    
    @SuppressWarnings("unused")
    private static List<NameValuePair> getPostParameterPairs(Map<String, String> postParams) {
        List<NameValuePair> result = new ArrayList<NameValuePair>(postParams.size());
        for (String key : postParams.keySet()) {
            result.add(new BasicNameValuePair(key, postParams.get(key)));
        }
        return result;
    }
    
    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        HttpUriRequest httpRequest = createHttpRequest(request, additionalHeaders);
        builder.setRetryHandler(new NoRetryHttpRequestRetryHandler());
        
        CloseableHttpClient client = builder.build();
        
        addHeaders(httpRequest, additionalHeaders);
        addHeaders(httpRequest, request.getHeaders());
        onPrepareRequest(httpRequest);
        HttpParams httpParams = httpRequest.getParams();
        int timeoutMs = request.getTimeoutMs();
        // TODO: Reevaluate this connection timeout based on more wide-scale
        // data collection and possibly different for wifi vs. 3G.
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
        // if (request instanceof MultiPartRequest) {
        // mClient.setHttpRequestRetryHandler(new
        // NoRetryHttpRequestRetryHandler());
        // } else {
        // mClient.setHttpRequestRetryHandler(new
        // DefaultHttpRequestRetryHandler());
        // }
        return client.execute(httpRequest);
        // return mClient.execute(httpRequest);
    }
    
    /**
     * Creates the appropriate subclass of HttpUriRequest for passed in request.
     * 
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    @SuppressWarnings("deprecation")
    /* protected */static HttpUriRequest createHttpRequest(Request<?> request, Map<String, String> additionalHeaders) throws AuthFailureError,
            UnsupportedOperationException, IOException {
        switch (request.getMethod()) {
            case Method.DEPRECATED_GET_OR_POST: {
                // This is the deprecated way that needs to be handled for
                // backwards compatibility.
                // If the request's post body is null, then the assumption is
                // that the request is
                // GET. Otherwise, it is assumed that the request is a POST.
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    HttpPost postRequest = new HttpPost(request.getUrl());
                    postRequest.addHeader(HEADER_CONTENT_TYPE, request.getPostBodyContentType());
                    HttpEntity entity;
                    entity = new ByteArrayEntity(postBody);
                    postRequest.setEntity(entity);
                    return postRequest;
                } else {
                    return new HttpGet(request.getUrl());
                }
            }
            case Method.GET:
                return new HttpGet(request.getUrl());
            case Method.DELETE:
                return new HttpDelete(request.getUrl());
            case Method.POST: {
                HttpPost postRequest = new HttpPost(request.getUrl());
                if (request instanceof MultiPartRequest == false) {
                    postRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                } else {
                    setMultiPartBody(postRequest, request);
                }
                setEntityIfNonEmptyBody(postRequest, request);
                return postRequest;
            }
            case Method.PUT: {
                HttpPut putRequest = new HttpPut(request.getUrl());
                putRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                setMultiPartBody(putRequest, request);
                setEntityIfNonEmptyBody(putRequest, request);
                return putRequest;
            }
            default:
                throw new IllegalStateException("Unknown request method.");
        }
    }
    
    private static void setEntityIfNonEmptyBody(HttpEntityEnclosingRequestBase httpRequest, Request<?> request) throws AuthFailureError {
        byte[] body = request.getBody();
        if (body != null) {
            HttpEntity entity = new ByteArrayEntity(body);
            httpRequest.setEntity(entity);
        }
    }
    
    /**
     * If Request is MultiPartRequest type, then set MultipartEntity in the
     * httpRequest object.
     * 
     * @param httpRequest
     * @param request
     * @throws AuthFailureError
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void setMultiPartBody(HttpEntityEnclosingRequestBase httpRequest, Request<?> request) throws AuthFailureError,
            UnsupportedOperationException, IOException {
        
        // Return if Request is not MultiPartRequest
        if (request instanceof MultiPartRequest == false) {
            return;
        }
        
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        // Iterate the fileUploads
        Map<String, File> fileUpload = ((MultiPartRequest) request).getFileUploads();
        for (Map.Entry<String, File> entry : fileUpload.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            multipartEntity.addPart(((String) entry.getKey()), new FileBody((File) entry.getValue()));
        }
        
        // Iterate the stringUploads
        Map<String, String> stringUpload = ((MultiPartRequest) request).getStringUploads();
        for (Map.Entry<String, String> entry : stringUpload.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            try {
                multipartEntity.addPart(((String) entry.getKey()),
                        new StringBody((String) entry.getValue(), HEADER_CONTENT_TYPE_BODY, Charset.forName(DEFAULT_PARAMS_ENCODING)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        Map<String, ByteArrayBodyWrapper> byteArrayUpload = ((MultiPartRequest) request).getByteArray();
        for (Map.Entry<String, ByteArrayBodyWrapper> entry : byteArrayUpload.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            multipartEntity.addPart(AeaConstants.POST_PAR_PROCESSDEFFILES, new ByteArrayBody(entry.getValue().getBody(),entry.getValue().getFileName()));
        }
        
        httpRequest.setEntity(multipartEntity);
    }
    
    /**
     * Called before the request is executed using the underlying HttpClient.
     * 
     * <p>
     * Overwrite in subclasses to augment the request.
     * </p>
     */
    protected void onPrepareRequest(HttpUriRequest request) throws IOException {
        // Nothing.
    }
    
    private HttpClientBuilder createHttpClientBuilder() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        
        // SSL context for secure connections can be created either based on
        // system or application specific properties.
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        // Use custom hostname verifier to customize SSL hostname verification.
        X509HostnameVerifier hostnameVerifier = new BrowserCompatHostnameVerifier();
        // Create a registry of custom connection socket factories for supported
        // protocol schemes.
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext, hostnameVerifier)).build();
        
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 关闭空闲连接
        cm.closeIdleConnections(0, TimeUnit.SECONDS);
        // 连接池最大生成连接数200
        cm.setMaxTotal(50);
        // 默认设置route最大连接数为20
        cm.setDefaultMaxPerRoute(20);
        builder.setConnectionManager(cm);
        ConnectionConfig config = ConnectionConfig.custom().setBufferSize(SOCKET_BUFFER_SIZE).build();
        builder.setDefaultConnectionConfig(config);
        return builder;
    }
    
    public final DefaultHttpClient createHttpClient() {
        // Sets up the http part of the service.
        final SchemeRegistry supportedSchemes = new SchemeRegistry();
        
        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));
        supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        
        // Set some client http client parameter defaults.
        final HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, false);
        
        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, supportedSchemes);
        AbstractHttpClient client = new DefaultHttpClient(ccm, httpParams);
        // to resolve The target server failed to respond
        // client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,
        // true);
        HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
        HttpProtocolParams.setVersion(client.getParams(), HttpVersion.HTTP_1_1);
        // client.setHttpRequestRetryHandler(new
        // NoRetryHttpRequestRetryHandler());
        
        return new DefaultHttpClient(ccm, httpParams);
    }
    
    /**
     * Create the default HTTP protocol parameters.
     */
    private final HttpParams createHttpParams() {
        final HttpParams params = new BasicHttpParams();
        
        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
        return params;
    }
}
