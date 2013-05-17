package green.monitor;

import java.io.Reader;

public interface IGetMonitoringService {
    Monitoring getMonitoring(Reader reader) throws Exception;
}
