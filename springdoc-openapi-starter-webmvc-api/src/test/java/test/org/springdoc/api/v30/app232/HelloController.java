package test.org.springdoc.api.v30.app232;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping
public class HelloController {


	@RequestMapping("/")
	public String sayHello() {
		return null;
	}

}