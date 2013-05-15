package green.monitor;

import javax.xml.bind.JAXBException;
import java.io.Reader;

public interface IGetMonitoringService {
    aMonitoring getMonitoring(Reader reader) throws JAXBException;
}
