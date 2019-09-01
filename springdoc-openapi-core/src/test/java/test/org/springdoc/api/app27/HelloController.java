package test.org.springdoc.api.app27;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private boolean flag = false;
    
    @RequestMapping("/")
    public String index() {
        return "";
    }

    @GetMapping("/test")
    public String test() {
        flag = !flag;
        throw flag ? new MyException() : new RuntimeException();
    }

}
