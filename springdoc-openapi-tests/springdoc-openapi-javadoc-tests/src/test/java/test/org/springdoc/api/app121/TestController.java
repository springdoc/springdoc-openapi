package test.org.springdoc.api.app121;

import org.springdoc.core.annotations.ParameterObject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Test controller.
 */
@RestController
class TestController {

	/**
	 * Gets test.
	 *
	 * @param param the param 
	 * @param requestParams the request params 
	 * @return the test
	 */
	@PostMapping("test")
	public InheritedRequestParams getTest(@RequestParam String param, @ParameterObject InheritedRequestParams requestParams) {
		return requestParams;
	}
}
