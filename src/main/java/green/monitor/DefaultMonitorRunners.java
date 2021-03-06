package green.monitor;

import com.google.common.collect.Lists;
import green.monitor.runner.DataBaseMonitorRunner;
import green.monitor.runner.HttpConnectionMonitorRunner;
import green.monitor.runner.WebServiceMonitorRunner;

import java.util.ArrayList;
import java.util.List;

public class DefaultMonitorRunners {
    static List<Monitor> getDefaultRunner() {
        final ArrayList<Monitor> list = Lists.newArrayList();
        list.add(new Monitor("web-service", WebServiceMonitorRunner.class, "1.0"));
        list.add(new Monitor("database", DataBaseMonitorRunner.class, "1.0"));
        list.add(new Monitor("http-connection", HttpConnectionMonitorRunner.class, "1.0"));
        return list;
    }
}
