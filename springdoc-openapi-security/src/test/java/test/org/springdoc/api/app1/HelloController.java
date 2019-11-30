package test.org.springdoc.api.app1;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping(value = "/persons")
    public String persons(@RequestBody() Person person) {
        return "OK";
    }

    @PostMapping(value = "/persons-with-user")
    public String personsWithUser(@RequestBody() Person person,
                                  @AuthenticationPrincipal User user) {
        return "OK";
    }

}