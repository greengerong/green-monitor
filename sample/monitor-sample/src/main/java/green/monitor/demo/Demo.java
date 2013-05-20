package green.monitor.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class Demo {

    @RequestMapping("hello")
    @ResponseBody
    public String hello(String name) {
        return String.format("Hello %s", name);
    }

    @RequestMapping("failed")
    @ResponseBody
    public String hello2(String name) {
        throw new RuntimeException(name);
    }
}
