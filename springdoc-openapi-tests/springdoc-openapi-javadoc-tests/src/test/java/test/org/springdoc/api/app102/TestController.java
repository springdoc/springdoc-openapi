package test.org.springdoc.api.app102;

import org.springdoc.core.annotations.ParameterObject;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
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
	 */
	@GetMapping("test")
	public void getTest(@RequestParam @Nullable String param, @ParameterObject InheritedRequestParams requestParams) {
	}
}
