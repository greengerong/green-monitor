package green.monitor.controller;

import com.ibm.wsdl.util.IOUtils;
import green.monitor.IMonitorFactory;
import green.monitor.MonitorResult;
import green.monitor.Monitoring;
import green.monitor.ResourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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
    @ResponseBody
    public Monitoring getConfig() throws Exception {
        return monitorFactory.getMonitoring();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public MonitorResult run(@PathVariable String id) throws Exception {
        return monitorFactory.run(id);
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, MonitorResult> runAll() throws Exception {
        return monitorFactory.runAll();
    }

    @RequestMapping(value = "resource/{type}/{template}", method = RequestMethod.GET)
    @ResponseBody
    public String getResource(@PathVariable String type, @PathVariable String template) throws IOException {
        return getFileResource(type, template);
    }

    private String getFileResource(String type, String template) throws IOException {
        InputStream inputStream = null;
        try {
            System.out.println(template);
            inputStream = ResourceUtil.getCurrentResourceAsStream(String.format("%s.%s", template, type));
            return IOUtils.getStringFromReader(new InputStreamReader(inputStream));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public String index() throws IOException {
        return getResource("html", "full");
    }


}
