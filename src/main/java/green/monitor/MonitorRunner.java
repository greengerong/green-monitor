package green.monitor;

import java.util.List;

public interface MonitorRunner {

    void loadContext(List<Param> params);

    boolean run(ContextLogger logger);
}
