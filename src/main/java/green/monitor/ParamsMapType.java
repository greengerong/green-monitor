package green.monitor;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

class ParamsMapType {
    @XmlElement(name = "param")
    public List<Param> entryList = Lists.newArrayList();
}
