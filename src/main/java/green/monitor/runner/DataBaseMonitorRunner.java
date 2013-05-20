package green.monitor.runner;

import green.monitor.ContextLogger;
import green.monitor.Item;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import static green.monitor.ResourceUtil.getResourceAsStream;

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
