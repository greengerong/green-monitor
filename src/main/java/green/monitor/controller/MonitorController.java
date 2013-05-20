package green.monitor.controller;

import green.monitor.IMonitorFactory;
import green.monitor.MonitorResult;
import green.monitor.Monitoring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/monitor")
public class MonitorController {

    private final IMonitorFactory monitorFactory;

    @Autowired
    public MonitorController(IMonitorFactory monitorFactory) {
        this.monitorFactory = monitorFactory;
    }

    @RequestMapping(value = "config", method = RequestMethod.GET)
    public
    @ResponseBody
    Monitoring getConfig() throws Exception {
        return monitorFactory.getMonitoring();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MonitorResult run(@PathVariable String id) throws Exception {
        return monitorFactory.run(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, MonitorResult> runAll() throws Exception {
        return monitorFactory.runAll();
    }
}
