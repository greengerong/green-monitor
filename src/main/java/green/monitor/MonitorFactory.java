package green.monitor;

import javax.xml.bind.JAXBException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class MonitorFactory {
    public final static MonitorFactory instance = new MonitorFactory();
    private final Map<String, Monitor> runner = new HashMap<String, Monitor>();
    private IGetMonitoringService getMonitoringService;
    private Monitoring monitoring;

    protected MonitorFactory() {
        this(new GetMonitoringService());
    }

    protected MonitorFactory(IGetMonitoringService getMonitoringService) {

        this.getMonitoringService = getMonitoringService;
        initMonitorRunner();
    }

    protected void initMonitorRunner() {
        final Monitor monitor = new Monitor("web-service", WebServiceMonitorRunner.class, "1.0");
        addRunner(monitor);
    }

    private void addRunner(Monitor monitor) {
        runner.put(monitor.getName(), monitor);
    }


    public static MonitorFactory getInstance() {
        return instance;
    }

    public Map<String, Monitor> getRunner() {
        return runner;
    }

    public synchronized Monitoring GetMonitoring(Reader reader) throws JAXBException {
        if (monitoring == null) {
            monitoring = getMonitoringService.getMonitoring(reader);
            fillRunners(monitoring);
        }
        return monitoring;
    }

    private void fillRunners(Monitoring monitoring) {
        for (Monitor monitor : monitoring.getMonitors()) {
            try {
                addRunner(monitor);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public MonitorResult run(String id) {
        return null;
    }
}
