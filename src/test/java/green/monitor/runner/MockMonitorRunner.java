package green.monitor.runner;

import green.monitor.ContextLogger;
import green.monitor.Item;
import green.monitor.runner.MonitorRunner;

public class MockMonitorRunner implements MonitorRunner {

    @Override
    public void loadContext(Item item) {
    }

    @Override
    public boolean run(ContextLogger logger) {
        logger.append("log");
        return true;
    }

}
