package green.monitor;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "param")
@XmlAccessorType(XmlAccessType.FIELD)
class Param {

    @XmlAttribute
    private String name;

    @XmlValue
    private String value;

    public Param() {
    }

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
