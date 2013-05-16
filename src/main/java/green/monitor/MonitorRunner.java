package green.monitor;

public interface MonitorRunner {

    void loadContext(Item item);

    boolean run(ContextLogger logger);
}
