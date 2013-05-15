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

    public Monitor() {
    }

    public Monitor(String name, Class runnerClass, String version) {
        this.version = version;
        this.name = name;
        this.monitor = runnerClass.getName();
    }

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

    public Class getRunnerClass() {
        try {
            return Class.forName(monitor);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }
}
