package green.monitor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;

public class GetMonitoringService implements IGetMonitoringService {

    @Override
    public Monitoring getMonitoring(Reader reader) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Monitoring.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (Monitoring) u.unmarshal(reader);
    }
}

