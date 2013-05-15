package green.monitor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "monitoring")
@XmlAccessorType(XmlAccessType.FIELD)
public class Monitoring {

    @XmlAttribute
    private String version;

    @XmlElement(name = "monitor")
    @XmlElementWrapper(name = "monitors")
    private List<Monitor> monitors;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "items")
    private List<Item> items;

    public Monitoring() {
        monitors = new ArrayList<Monitor>();
        items = new ArrayList<Item>();
    }

    public List<Monitor> getMonitors() {
        return monitors;
    }

    public void setMonitors(List<Monitor> monitors) {
        this.monitors = monitors;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
        System.out.println(version);
    }
}
