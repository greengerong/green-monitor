package green.monitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.bind.JAXBException;
import java.io.StringReader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class MonitorFactoryTest {

    @Mock
    private IGetMonitoringService getMonitoringService;

    @Test
    public void shouldGetMonitoring() throws JAXBException {
        final StringReader reader = new StringReader("");
        final Monitoring exceptedMonitoring = new Monitoring();
        given(getMonitoringService.getMonitoring(reader)).willReturn(exceptedMonitoring);

        final Monitoring monitoring = new MonitorFactory(getMonitoringService).GetMonitoring(reader);

        assertThat(monitoring, is(exceptedMonitoring));
    }
}
