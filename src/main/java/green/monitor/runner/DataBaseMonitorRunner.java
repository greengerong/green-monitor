package green.monitor.runner;

import green.monitor.ContextLogger;
import green.monitor.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Map;

public class DataBaseMonitorRunner implements MonitorRunner {

    private String password;
    private String username;
    private String script;
    private String url;
    private String driverClassName;
    private JdbcTemplate jdbcTemplate;

    public DataBaseMonitorRunner() {
        this(new JdbcTemplate());

    }

    public DataBaseMonitorRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void loadContext(Item item) {
        final Map<String, String> params = item.getParams();
        password = params.get("password");
        username = params.get("username");
        url = params.get("url");
        driverClassName = params.get("driverClassName");
        script = params.get("script");

    }

    @Override
    public boolean run(ContextLogger logger) {
        jdbcTemplate.setDataSource(getDataSource());
        try {
            logger.append(String.format("Start to connection %s...", url));
            jdbcTemplate.execute(script);
            logger.append(String.format("Execute sql {%s} success!", script));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.append(ex);
        }
        return false;
    }

    protected DriverManagerDataSource getDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }
}
