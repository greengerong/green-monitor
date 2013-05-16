package green.monitor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.bind.JAXBException;
import java.io.StringReader;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MonitorFactoryTest {

    @Mock
    private IGetMonitoringService getMonitoringService;
    private MonitorFactory monitorFactory;

    @Before
    public void setUp() throws Exception {
        monitorFactory = new MonitorFactory(getMonitoringService);
    }

    @Test
    public void shouldGetMonitoring() throws JAXBException {
        final StringReader reader = new StringReader("");
        final Monitoring exceptedMonitoring = new Monitoring();
        final Monitor monitor = new Monitor();
        final Class runnerClass = mock(MonitorRunner.class).getClass();
        monitor.setMonitor(runnerClass.getName());
        monitor.setName("test");
        monitor.setVersion("1.0");
        exceptedMonitoring.getMonitors().add(monitor);
        given(getMonitoringService.getMonitoring(reader)).willReturn(exceptedMonitoring);

        final Monitoring monitoring = monitorFactory.loadMonitoring(reader);

        assertThat(monitoring, is(exceptedMonitoring));
        final Map<String, Monitor> runner = monitorFactory.getRunner();
        assertThat(runner.containsKey(monitor.getName()), is(true));
        assertThat(runner.get(monitor.getName()), is(monitor));
    }

    @Test
    public void shouldRunMonitor() throws Exception {
        //given
        final Monitor monitor = new Monitor("test", MockMonitorRunner.class, "1.0");
        final Item item = new Item();
        item.setName("test item");
        item.setMonitor(monitor.getName());
        item.getParams().add(new Param("key", "value"));

        final Monitoring monitoring = new Monitoring();
        monitoring.getItems().add(item);
        monitoring.getMonitors().add(monitor);

        given(getMonitoringService.getMonitoring(null)).willReturn(monitoring);
        monitorFactory.loadMonitoring(null);

        //when
        final MonitorResult result = monitorFactory.run(item.getId());

        //then
        assertThat(result.isSuccess(), is(true));
        assertThat(result.getTime(), is(not(0L)));
        assertThat(result.getLog(), is("log"));
    }
}

