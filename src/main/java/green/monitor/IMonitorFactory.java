package green.monitor;

import java.util.Map;

public interface IMonitorFactory {

    Map<String, Monitor> getRunner();

    Monitoring getMonitoring() throws Exception;

    MonitorResult run(String id) throws Exception;

    Map<String, MonitorResult> runAll() throws Exception;
}
