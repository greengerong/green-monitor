package green.monitor.runner;

import green.monitor.ContextLogger;
import green.monitor.Item;

public interface MonitorRunner {

    void loadContext(Item item);

    boolean run(ContextLogger logger);
}
