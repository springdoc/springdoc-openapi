package test.org.springdoc.api.app3;

import org.springdoc.core.converters.PageableAsQueryParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/demo",
        produces = MediaType.TEXT_PLAIN_VALUE)
public class HelloController {

    @GetMapping("operation4")
    @PageableAsQueryParam
    public String operation4() {
        return "operation4";
    }


}