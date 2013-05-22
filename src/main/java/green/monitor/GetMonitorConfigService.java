package green.monitor;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.apache.commons.lang.StringUtils.isBlank;

@Component
public class GetMonitorConfigService implements IGetMonitorConfigService {

    public static final String MONITOR_CONFIG_FILE = "monitor-config%s.xml";
    public static final String ENV_KEY = "appenv";

    @Override
    public Reader getMonitorConfigReader() {
        final String config = String.format(MONITOR_CONFIG_FILE, getEnv());
        final InputStream stream = ResourceUtil.getThreadResourceAsStream(config);
        return new InputStreamReader(stream);
    }

    private String getEnv() {
        final String env = System.getenv(ENV_KEY);
        return isBlank(env) ? "" : ("." + env);
    }

}
