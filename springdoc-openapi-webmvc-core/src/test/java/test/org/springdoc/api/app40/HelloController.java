package test.org.springdoc.api.app40;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class HelloController {

    @RequestMapping(value = "/iae_error", method = RequestMethod.GET)
    public ObjectNode getStartFormProperties() {
        return null;
    }
}