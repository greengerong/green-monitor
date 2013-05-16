package green.monitor;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

public class HashMapAdapter extends XmlAdapter<ParamsMapType, Map<String, String>> {

    @Override
    public ParamsMapType marshal(Map<String, String> map) {
        ParamsMapType paramsMapType = new ParamsMapType();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            paramsMapType.entryList.add(new Param(entry.getKey(), entry.getValue()));
        }
        return paramsMapType;
    }

    @Override
    public Map<String, String> unmarshal(ParamsMapType typeParams) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        for (Param param : typeParams.entryList) {
            map.put(param.getName(), param.getValue());
        }
        return map;
    }
}
