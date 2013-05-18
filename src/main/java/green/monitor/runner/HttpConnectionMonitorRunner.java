package green.monitor.runner;


import green.monitor.ContextLogger;
import green.monitor.Item;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import static green.monitor.ResourceUtil.getResourceAsStream;

public class HttpConnectionMonitorRunner implements MonitorRunner {
    private String url;
    private String request;
    private Properties assertProperty;

    @Override
    public void loadContext(Item item) {
        final Map<String, String> params = item.getParams();
        this.url = params.get("url");
        this.request = getRequest(params.get("request"));
    }

    private String getRequest(String requestFile) {
        final InputStream stream = getResourceAsStream(requestFile);
        return "";
    }

    @Override
    public boolean run(ContextLogger logger) {
        try {
            final HttpClient httpClient = new HttpClient(new URL(url), "", 1);
        } catch (IOException e) {

        }
        return false;
    }
}
