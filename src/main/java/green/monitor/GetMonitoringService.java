package green.monitor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;

public class GetMonitoringService implements IGetMonitoringService {

    @Override
    public aMonitoring getMonitoring(Reader reader) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(aMonitoring.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (aMonitoring) u.unmarshal(reader);
    }
}
