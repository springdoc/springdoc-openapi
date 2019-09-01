package test.org.springdoc.api.app9;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyApiController implements MyApi {
	public String get(String language) {
		return language;
	}
}