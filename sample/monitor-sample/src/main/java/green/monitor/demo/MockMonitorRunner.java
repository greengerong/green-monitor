package green.monitor.demo;

import green.monitor.ContextLogger;
import green.monitor.Item;
import green.monitor.runner.MonitorRunner;

public class MockMonitorRunner implements MonitorRunner {
    @Override
    public void loadContext(Item item) {

    }

    @Override
    public boolean run(ContextLogger logger) {
        final double random = Math.random();
        logger.append(String.format("Current random : %s", random));
        return random > 0.5;
    }
}
