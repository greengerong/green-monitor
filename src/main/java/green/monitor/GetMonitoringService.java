package green.monitor;

import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;

@Component
public class GetMonitoringService implements IGetMonitoringService {

    @Override
    public Monitoring getMonitoring(Reader reader) throws Exception {
        Monitoring monitoring = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(Monitoring.class);
            Unmarshaller u = jc.createUnmarshaller();
            monitoring = (Monitoring) u.unmarshal(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return monitoring;
    }
}

