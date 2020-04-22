package test.org.springdoc.api.app102;

import org.springdoc.api.annotations.ParameterObject;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@GetMapping("test")
	public void getTest(@RequestParam @Nullable String param, @ParameterObject InheritedRequestParams requestParams) {
	}
}
