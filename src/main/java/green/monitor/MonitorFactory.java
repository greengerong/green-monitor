package green.monitor;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;

import javax.xml.bind.JAXBException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static green.monitor.DefaultMonitorRunners.getDefaultRunner;

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
        for (Monitor monitor : getDefaultRunner()) {
            addRunner(monitor);
        }
    }

    private void addRunner(Monitor monitor) {
        final String key = monitor.getName();
        if (!runner.containsKey(key)) {
            runner.put(key, monitor);
        }
    }

    public static MonitorFactory getInstance() {
        return instance;
    }

    public Map<String, Monitor> getRunner() {
        return runner;
    }

    public Monitoring loadMonitoring(Reader reader) throws JAXBException {
        synchronized (this) {
            if (monitoring == null) {
                monitoring = getMonitoringService.getMonitoring(reader);
                fillRunners(monitoring);
            }
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

    public Map<String, MonitorResult> runAll() throws Exception {
        final Map<String, MonitorResult> map = Maps.newHashMap();
        for (Item item : monitoring.getItems()) {
            final String id = item.getId();
            map.put(id, run(id));
        }

        return map;
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
