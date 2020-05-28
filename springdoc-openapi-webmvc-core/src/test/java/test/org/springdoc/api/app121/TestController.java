package test.org.springdoc.api.app121;

import com.sun.istack.internal.Nullable;
import org.springdoc.api.annotations.ParameterObject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@PostMapping("test")
	public InheritedRequestParams getTest(@RequestParam @Nullable String param, @ParameterObject InheritedRequestParams requestParams) {
		return requestParams;
	}
}
