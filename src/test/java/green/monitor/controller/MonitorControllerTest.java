package green.monitor.controller;

import green.monitor.IMonitorFactory;
import green.monitor.MonitorResult;
import green.monitor.Monitoring;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class MonitorControllerTest {
    @Mock
    private IMonitorFactory monitorFactory;
    private MonitorController controller;

    @Before
    public void setUp() throws Exception {
        controller = new MonitorController(monitorFactory);
    }

    @Test
    public void testGetConfig() throws Exception {
        final Monitoring monitoring = new Monitoring();
        given(monitorFactory.getMonitoring()).willReturn(monitoring);

        final Monitoring config = controller.getConfig();

        assertThat(config, is(monitoring));
    }

    @Test
    public void shouldGetRunResultById() throws Exception {
        final MonitorResult monitorResult = new MonitorResult(true, "", 100);
        final String id = UUID.randomUUID().toString();
        given(monitorFactory.run(id)).willReturn(monitorResult);

        final MonitorResult result = controller.run(id);

        assertThat(result, is(monitorResult));
    }

    @Test
    public void shouldGetAllResult() throws Exception {
        final Map<String, MonitorResult> map = new HashMap<String, MonitorResult>();
        given(monitorFactory.runAll()).willReturn(map);

        final Map<String, MonitorResult> resultMap = controller.runAll();

        assertThat(resultMap, is(map));
    }
}
