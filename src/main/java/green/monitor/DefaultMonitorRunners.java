package green.monitor;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class DefaultMonitorRunners {
    static List<Monitor> getDefaultRunner() {
        final ArrayList<Monitor> list = Lists.newArrayList();
        list.add(new Monitor("web-service", WebServiceMonitorRunner.class, "1.0"));
        return list;
    }
}
