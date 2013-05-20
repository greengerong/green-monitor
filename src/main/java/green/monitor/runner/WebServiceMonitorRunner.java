package green.monitor.runner;

import green.monitor.ContextLogger;
import green.monitor.Item;
import org.apache.commons.lang.StringUtils;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import static green.monitor.ResourceUtil.getThreadResourceAsStream;

public class WebServiceMonitorRunner implements MonitorRunner {
    private String url;
    private String request;
    private Properties assertProperties;
    private WebServiceTemplate webServiceTemplate;
    private static final String ENCODE = "utf-8";

    public WebServiceMonitorRunner() {
        webServiceTemplate = new WebServiceTemplate();
    }

    public WebServiceMonitorRunner(String url, String request, Properties assertProperties,
                                   WebServiceTemplate webServiceTemplate) {
        this.url = url;
        this.request = request;
        this.assertProperties = assertProperties;
        this.webServiceTemplate = webServiceTemplate;
    }

    @Override
    public void loadContext(Item item) {
        final Map<String, String> params = item.getParams();
        this.url = params.get("url");
        this.request = params.get("request");
        if (params.containsKey("assert")) {
            this.assertProperties = new Properties();
            try {
                final String anAssert = params.get("assert");
                assertProperties.load(getThreadResourceAsStream(anAssert));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private InputStream getRequest(String requestFile) {
        return getThreadResourceAsStream(requestFile);
    }

    @Override
    public boolean run(ContextLogger logger) {
        webServiceTemplate.setDefaultUri(url);
        final StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        final StreamSource requestPayload = new StreamSource(getRequest(request));
        try {
            webServiceTemplate.sendSourceAndReceiveToResult(url, requestPayload, result);
            sw.close();
            final String response = sw.toString();
            logger.append(response);
            return assertResult(response, logger);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.append(ex);
            return false;
        }
    }

    private boolean assertResult(String response, ContextLogger logger) throws Exception {
        if (assertProperties != null && !assertProperties.isEmpty()) {
            final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final InputSource source = new InputSource(new ByteArrayInputStream(response.getBytes(ENCODE)));
            final Document document = documentBuilder.parse(source);

            for (Object key : assertProperties.keySet()) {
                if (!assertResult(logger, document, key)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean assertResult(ContextLogger logger, Document document, Object key) {
        final String value = assertProperties.get(key).toString();
        final NodeList nodeList = document.getElementsByTagName(key.toString());
        if (StringUtils.isBlank(value)) {
            if (elementValueDoesNotExistes(nodeList)) {
                logger.append("tag %s does not exists!", key);
                return false;
            }
        } else {
            if (assertTagContextWrong(value, nodeList)) {
                logger.append("Tag %s's value is not %s!", key, value);
                return false;
            }
        }

        return true;
    }

    private boolean assertTagContextWrong(String value, NodeList nodeList) {
        return !nodeList.item(0).getTextContent().equals(value);
    }

    private boolean elementValueDoesNotExistes(NodeList nodeList) {
        return nodeList != null && nodeList.getLength() == 0;
    }

}
