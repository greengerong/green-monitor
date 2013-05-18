package green.monitor.runner;

import green.monitor.ContextLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WebServiceMonitorRunnerTest {

    private WebServiceTemplate webServiceTemplate;
    private WebServiceMonitorRunner runner;
    private String responseXml;
    private Properties assertProperties = new Properties();

    @Before
    public void setUp() throws Exception {
        webServiceTemplate = new WebServiceTemplate() {
            @Override
            public boolean sendSourceAndReceiveToResult(String url, Source requestPayload, Result responseResult) {
                try {
                    ((StreamResult) responseResult).getWriter().write(responseXml);
                } catch (IOException e) {
                    return false;
                }
                return true;
            }
        };

        runner = new WebServiceMonitorRunner("http://localhost:8080/ws", "", assertProperties, webServiceTemplate);
    }

    @Test
    public void shouldFailedWhenAssertValueIsWrong() throws Exception {

        responseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><account><id>123</id></account>";
        assertProperties.setProperty("id", "1");
        final boolean isSuccess = runner.run(new ContextLogger());

        assertThat(isSuccess, is(false));
    }

    @Test
    public void shouldSuccessWhenAssertValueIsRight() throws Exception {

        responseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><account><id>123</id></account>";
        assertProperties.setProperty("id", "123");
        final boolean isSuccess = runner.run(new ContextLogger());

        assertThat(isSuccess, is(true));
    }

    @Test
    public void shouldSuccessWhenNoAssertValue() throws Exception {

        responseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><account><id>123</id></account>";
        assertProperties.setProperty("id", "");
        final boolean isSuccess = runner.run(new ContextLogger());

        assertThat(isSuccess, is(true));
    }

    @Test
    public void shouldFailedWhenNoTag() throws Exception {

        responseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><account><id>123</id></account>";
        assertProperties.setProperty("name", "");
        final boolean isSuccess = runner.run(new ContextLogger());

        assertThat(isSuccess, is(false));
    }

}
