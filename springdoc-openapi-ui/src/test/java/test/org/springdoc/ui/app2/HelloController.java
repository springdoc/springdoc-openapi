package test.org.springdoc.ui.app2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@RestController
public class HelloController {

    @GetMapping(value = "/persons")
    public void persons(@Valid @RequestParam @Size(min = 4, max = 6) String name) {

    }

}
