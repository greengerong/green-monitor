package green.monitor.runner;

import green.monitor.ContextLogger;
import green.monitor.Item;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLDataException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class DataBaseMonitorRunnerTest {
    @Test
    public void shouldFailedWhenSqlException() throws Exception {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void execute(String sql) throws DataAccessException {
                throw new BadSqlGrammarException("", "", new SQLDataException());
            }
        };
        final DataBaseMonitorRunner runner = new DataBaseMonitorRunner(jdbcTemplate) {
            @Override
            protected DriverManagerDataSource getDataSource() {
                return null;
            }
        };

        runner.loadContext(buildParams());

        final boolean isSuccess = runner.run(new ContextLogger());

        assertThat(isSuccess, is(false));
    }

    @Test
    public void shouldSuccessWhenSqlun() throws Exception {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void execute(String sql) throws DataAccessException {

            }
        };
        final DataBaseMonitorRunner runner = new DataBaseMonitorRunner(jdbcTemplate) {
            @Override
            protected DriverManagerDataSource getDataSource() {
                return null;
            }
        };

        runner.loadContext(buildParams());

        final boolean isSuccess = runner.run(new ContextLogger());

        assertThat(isSuccess, is(true));
    }

    private Item buildParams() {
        final Item item = new Item();
        item.getParams().put("url", "jdbc:jtds:sqlserver://localhost:1433;DatabaseName=Test ");
        item.getParams().put("driverClassName", "net.sourceforge.jtds.jdbc.Driver");
        return item;
    }
}
