package test.org.springdoc.api.app42;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController("/api")
public class HelloController {

    @GetMapping(value = "/tweets")
    public void tweets(@PathVariable TweetId id) {

    }

}