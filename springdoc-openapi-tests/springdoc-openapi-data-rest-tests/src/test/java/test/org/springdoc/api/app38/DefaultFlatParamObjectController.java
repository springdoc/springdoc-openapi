package test.org.springdoc.api.app38;

import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultFlatParamObjectController {
	@GetMapping("/test1")
	public String test1(@SortDefault("name") Sort sort) {
		return null;
	}
}
