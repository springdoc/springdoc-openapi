package test.org.springdoc.api.v31.app222;


import test.org.springdoc.api.v31.app222.SpringDocApp222Test.FirstHierarchyUser;
import test.org.springdoc.api.v31.app222.SpringDocApp222Test.SecondHierarchyUser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */

@RestController
class HelloController {

	@GetMapping("/hello1")
	public FirstHierarchyUser getItems1() {
		return null;
	}

	@GetMapping("/hello2")
	public SecondHierarchyUser getItems2() {
		return null;
	}

}