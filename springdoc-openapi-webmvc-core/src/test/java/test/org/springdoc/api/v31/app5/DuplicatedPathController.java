package test.org.springdoc.api.v31.app5;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DuplicatedPathController {

	@GetMapping("/duplicated")
	public String duplicated1() {
		return "globalBeanFiltered";
	}

	@GetMapping(value = "/duplicated", params = "filter=params")
	public String duplicated2() {
		return "beanFiltered";
	}

}
