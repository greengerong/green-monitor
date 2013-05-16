package green.monitor;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public  class ParamsMapType {
    @XmlElement(name ="param")
    public List<Param> entryList = new ArrayList<Param>();
}
