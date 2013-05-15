package green.monitor;

import javax.xml.bind.JAXBException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class MonitorFactory {
    public final static MonitorFactory instance = new MonitorFactory();
    private final static Map<String, Class> runner = new HashMap<String, Class>();
    private IGetMonitoringService getMonitoringService;
    private Monitoring monitoring;

    static {
        runner.put("web-server", WebServiceMonitorRunner.class);
    }

    protected MonitorFactory() {
        this(new GetMonitoringService());
    }

    protected MonitorFactory(IGetMonitoringService getMonitoringService) {

        this.getMonitoringService = getMonitoringService;
    }

    public static MonitorFactory getInstance() {
        return instance;
    }

    public static Map<String, Class> getRunner() {
        return runner;
    }

    public synchronized Monitoring GetMonitoring(Reader reader) throws JAXBException {
        if (monitoring == null) {
            monitoring = getMonitoringService.getMonitoring(reader);
        }
        return monitoring;
    }

    public MonitorResult run(String id) {
        return null;
    }
}
