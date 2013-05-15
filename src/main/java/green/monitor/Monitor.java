package green.monitor;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "monitor")
@XmlAccessorType(XmlAccessType.FIELD)
public class Monitor {

    @XmlAttribute
    private String name;
    @XmlAttribute
    private String version;
    @XmlValue
    private String monitor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }
}
