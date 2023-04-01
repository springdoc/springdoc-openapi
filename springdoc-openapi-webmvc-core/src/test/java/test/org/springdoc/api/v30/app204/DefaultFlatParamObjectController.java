package test.org.springdoc.api.v30.app204;

import org.springdoc.api.annotations.ParameterObject;

import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultFlatParamObjectController {
	@GetMapping("/test1")
	public String test1(@RequestParam String email, @Validated Person person, @SortDefault("name") @ParameterObject Sort sort) {
		return null;
	}
}
