package green.monitor;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.joda.time.DateTime;

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

    protected synchronized Monitoring loadMonitoring(Reader reader) throws JAXBException {
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

    public MonitorResult run(final String id) throws Exception {

        final Item item = Iterables.find(monitoring.getItems(), new Predicate<Item>() {
            @Override
            public boolean apply(Item item) {
                return item.getId().equals(id);
            }
        });

        final Monitor monitor = runner.get(item.getMonitor());
        assertMonitorNoNull(item, monitor);

        final MonitorRunner monitorRunner = loadRunner(item, monitor);
        final ContextLogger logger = new ContextLogger();
        final DateTime startDate = DateTime.now();
        final boolean isSuccess = monitorRunner.run(logger);
        final long timer = DateTime.now().getMillis() - startDate.getMillis();

        return new MonitorResult(isSuccess, logger.toString(), timer);
    }

    private void assertMonitorNoNull(Item item, Monitor monitor) {
        if (monitor == null) {
            final String errorMsg = String.format("Can not find monitor %s for item %s!", monitor.getMonitor(),
                    item.getName());
            throw new RuntimeException(errorMsg);
        }
    }

    private MonitorRunner loadRunner(Item item, Monitor monitor) throws InstantiationException, IllegalAccessException {
        final Class monitorClass = monitor.getRunnerClass();
        final MonitorRunner monitorRunner = (MonitorRunner) monitorClass.newInstance();
        monitorRunner.loadContext(item);
        return monitorRunner;
    }


}
