package green.monitor;

import org.junit.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class GetMonitoringServiceTest {
    @Test
    public void shouldGetMonitoring() throws Exception {
        final IGetMonitoringService service = new GetMonitoringService();
        final StringReader reader = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<monitoring version=\"1.0\">\n" +
                "    <monitors>\n" +
                "        <monitor name=\"webservice\" version=\"1.0\">monitor</monitor>\n" +
                "    </monitors>\n" +
                "    <items>\n" +
                "        <item monitor=\"webservice\" name=\"cbis\">\n" +
                "            <params>\n" +
                "                <param name=\"version\">1.0</param>\n" +
                "                <param name=\"version2\">2.0</param>\n" +
                "            </params>\n" +
                "            <description>This is a monitor for cbis service.</description>\n" +
                "        </item>\n" +
                "    </items>\n" +
                "</monitoring>");

        final Monitoring monitoring = service.getMonitoring(reader);

        assertThat(monitoring.getVersion(), is("1.0"));

        final List<Item> items = monitoring.getItems();
        assertThat(items.size(), is(1));
        final Item firstItem = items.get(0);
        assertThat(firstItem.getId(), is(not("0")));
        assertThat(firstItem.getName(), is("cbis"));
        assertThat(firstItem.getMonitor(), is("webservice"));
        assertThat(firstItem.getDescription(), is("This is a monitor for cbis service."));
        final Map<String, String> firstItemParams = firstItem.getParams();
        assertThat(firstItemParams.get("version"), is("1.0"));
        assertThat(firstItemParams.get("version2"), is("2.0"));

        final List<Monitor> monitors = monitoring.getMonitors();
        assertThat(monitors.size(), is(1));
        final Monitor firstMonitor = monitors.get(0);
        assertThat(firstMonitor.getName(), is("webservice"));
        assertThat(firstMonitor.getVersion(), is("1.0"));
        assertThat(firstMonitor.getMonitor(), is("monitor"));

    }
}
