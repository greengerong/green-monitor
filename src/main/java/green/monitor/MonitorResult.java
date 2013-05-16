package green.monitor;


public class MonitorResult {
    private long time;
    private String log;
    private boolean success;

    public MonitorResult(boolean success, String log, long time) {
        this.success = success;
        this.log = log;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public String getLog() {
        return log;
    }

    public boolean isSuccess() {
        return success;
    }
}
