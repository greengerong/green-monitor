package green.monitor.runner;

import green.monitor.ContextLogger;
import green.monitor.Item;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


public class HttpConnectionMonitorRunnerTest {
    private int responseCode = 200;
    private HttpConnectionMonitorRunner runner;

    @Before
    public void setUp() throws Exception {
        HttpClient httpClient = new HttpClient() {
            @Override
            public int executeMethod(HttpMethod method) throws IOException, HttpException {
                return responseCode;
            }
        };

        runner = new HttpConnectionMonitorRunner(httpClient);
    }

    @Test
    public void shouldFailedWhenCodeWrong() throws Exception {
        assertResponseCode(404, false);
    }

    @Test
    public void shouldFailedWhenException() {
        HttpClient httpClient = new HttpClient() {
            @Override
            public int executeMethod(HttpMethod method) throws IOException, HttpException {
                throw new HttpException("test");
            }

        };

        runner = new HttpConnectionMonitorRunner(httpClient);

        final boolean isSuccess = runner.run(new ContextLogger());

        assertThat(isSuccess, is(false));
    }

    @Test
    public void shouldSuccessWhenCodeRight() throws Exception {
        assertResponseCode(200, true);
    }


    private void assertResponseCode(int code, boolean excepted) {
        responseCode = code;

        final Item item = new Item();
        item.getParams().put("response-code", "200");
        runner.loadContext(item);
        final ContextLogger logger = new ContextLogger();
        final boolean isSuccess = runner.run(logger);

        assertThat(isSuccess, is(excepted));
        assertThat(logger.toString(), is(notNullValue()));
    }
}
