package green.monitor;

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
