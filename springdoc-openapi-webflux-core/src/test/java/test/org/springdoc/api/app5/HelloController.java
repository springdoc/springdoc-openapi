package test.org.springdoc.api.app5;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @PostMapping(value = "/tweets/does-not-work", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Tweet> postNotWorks(@RequestBody Mono<Tweet> tweet) {
        return tweet;
    }

    @GetMapping("/test")
    public Mono<HttpEntity<String>> demo2() {
        return null;
    }

}
