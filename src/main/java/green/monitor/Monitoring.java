package green.monitor;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "monitoring")
@XmlAccessorType(XmlAccessType.FIELD)
public class Monitoring {

    @XmlAttribute
    private String version;

    @XmlAttribute
    private String name;

    @XmlElement(name = "monitor")
    @XmlElementWrapper(name = "monitors")
    private List<Monitor> monitors = Lists.newArrayList();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "items")
    private List<Item> items = Lists.newArrayList();

    public Monitoring() {
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
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
