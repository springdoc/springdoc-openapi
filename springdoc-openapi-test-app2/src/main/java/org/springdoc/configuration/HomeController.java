package org.springdoc.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class HomeController {


	@GetMapping("/oauth/authorize")
    public String authorize() {
        return "forward:/api/oauth/dialog";
    }

}
