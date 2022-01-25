package test.org.springdoc.api.app121;

import org.springdoc.core.annotations.ParameterObject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@PostMapping("test")
	public InheritedRequestParams getTest(@RequestParam String param, @ParameterObject InheritedRequestParams requestParams) {
		return requestParams;
	}
}
