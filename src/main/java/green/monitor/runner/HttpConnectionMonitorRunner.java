package green.monitor.runner;


import com.google.common.collect.Lists;
import green.monitor.ContextLogger;
import green.monitor.Item;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class HttpConnectionMonitorRunner implements MonitorRunner {
    private String url;
    private String method = "GET";
    private List<NameValuePair> requestParams = Lists.newArrayList();
    private String proxy = null;
    private int proxyPort = 0;
    private int responseCode = 200;
    private String contentType = "text/html";
    private HttpClient httpClient;

    public HttpConnectionMonitorRunner(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpConnectionMonitorRunner() {
        this(new HttpClient());
    }

    @Override
    public void loadContext(Item item) {
        final Map<String, String> params = item.getParams();
        this.url = params.get("url");
        if (params.containsKey("method")) {
            method = params.get("method").toUpperCase();
        }
        if (params.containsKey("proxy")) {
            proxy = params.get("proxy");
            proxyPort = Integer.parseInt(params.get("proxy-port"));
        }

        if (params.containsKey("response-code")) {
            responseCode = Integer.parseInt(params.get("response-code"));
        }

        if (params.containsKey("param")) {
            buildParams(requestParams, params.get("param"));
        }

        if (params.containsKey("content-tpe")) {
            this.contentType = params.get("content-type");
        }

    }

    private void buildParams(List<NameValuePair> list, String paramStr) {
        final String[] ps = paramStr.trim().split("&");
        for (String p : ps) {
            final String[] keyValue = p.split("=");
            list.add(new NameValuePair(keyValue[0].trim(), keyValue[1].trim()));
        }
    }

    @Override
    public boolean run(ContextLogger logger) {

        setConfig(httpClient);

        final HttpMethod httpMethod = getHttpMethod();

        setHttpHeader(httpMethod);
        try {
            final int code = httpClient.executeMethod(httpMethod);
            logger.append(String.format("response code status: %d.", code));

            final String responseBodyAsString = httpMethod.getResponseBodyAsString();
            String responseText = "";
            if (!StringUtils.isBlank(responseBodyAsString)) {
                responseText = new String(responseBodyAsString.getBytes("utf-8"));
            }
            logger.append(responseText);

            return assertResult(code, responseText);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.append(ex);
        }

        return false;
    }

    protected void setHttpHeader(HttpMethod httpMethod) {
        httpMethod.setRequestHeader("Cache-Control", "no-cache");
        httpMethod.setRequestHeader("Content-Type", "text/html");
    }

    private void setConfig(HttpClient httpClient) {
        if (!StringUtils.isBlank(proxy)) {
            setProxy(httpClient);
        }
    }

    protected boolean assertResult(int code, String responseText) {
        return code == responseCode;
    }

    protected HttpMethod getHttpMethod() {
        HttpMethod httpMethod;
        if ("GET".equals(method)) {
            httpMethod = getGetMethod();
        } else if ("POST".equals(method)) {
            httpMethod = getPostMethod();
        } else {
            final String msg = String.format("For http connection monitor do not implement for method %s.", method);
            throw new RuntimeException(msg);
        }
        return httpMethod;
    }

    private void setProxy(HttpClient httpClient) {
        httpClient.getHostConfiguration().setProxy(proxy, proxyPort);
    }

    private PostMethod getPostMethod() {
        PostMethod post = new PostMethod(url);
        post.setRequestBody(requestParams.toArray(new NameValuePair[0]));
        return post;
    }

    private GetMethod getGetMethod() {
        final GetMethod getMethod = new GetMethod(url);
        getMethod.setQueryString(requestParams.toArray(new NameValuePair[0]));
        return getMethod;
    }


}
