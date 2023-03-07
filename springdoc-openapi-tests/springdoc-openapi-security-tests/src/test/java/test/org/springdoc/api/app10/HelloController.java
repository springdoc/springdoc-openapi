package test.org.springdoc.api.app10;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author James Selvakumar
 * @since 8.0.0
 */
@RestController
@RequestMapping("/api")
public class HelloController
{
    @GetMapping("/hello")
    public String hello()
    {
        return "Hello";
    }
}
