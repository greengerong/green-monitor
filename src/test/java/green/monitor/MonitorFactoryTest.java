package green.monitor;

import green.monitor.runner.MockMonitorRunner;
import green.monitor.runner.MonitorRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MonitorFactoryTest {

    @Mock
    private IGetMonitoringService getMonitoringService;
    @Mock
    private IGetMonitorConfigService getMonitorConfigService;

    private IMonitorFactory IMonitorFactory;

    @Before
    public void setUp() throws Exception {
        IMonitorFactory = new MonitorFactory(getMonitoringService, getMonitorConfigService);
    }

    @Test
    public void shouldGetMonitoring() throws Exception {
        //given
        final StringReader reader = new StringReader("");
        final Monitoring exceptedMonitoring = new Monitoring();
        final Monitor monitor = new Monitor();
        final Class runnerClass = mock(MonitorRunner.class).getClass();
        monitor.setMonitor(runnerClass.getName());
        monitor.setName("test");
        monitor.setVersion("1.0");
        exceptedMonitoring.getMonitors().add(monitor);
        given(getMonitorConfigService.getMonitorConfigReader()).willReturn(reader);
        given(getMonitoringService.getMonitoring(reader)).willReturn(exceptedMonitoring);

        //when
        final Monitoring monitoring = IMonitorFactory.getMonitoring();

        //then
        assertThat(monitoring, is(exceptedMonitoring));
        final Map<String, Monitor> runner = IMonitorFactory.getRunner();
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

        final Monitoring monitoring = new Monitoring();
        monitoring.getItems().add(item);
        monitoring.getMonitors().add(monitor);

        given(getMonitoringService.getMonitoring(any(Reader.class))).willReturn(monitoring);
        IMonitorFactory.getMonitoring();

        //when
        final MonitorResult result = IMonitorFactory.run(item.getId());

        //then
        assertThat(result.isSuccess(), is(true));
        assertThat(result.getTime(), is(not(0L)));
        assertThat(result.getLog(), is("log"));
    }
}

