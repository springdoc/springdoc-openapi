package test.org.springdoc.api.v30.app223;


import test.org.springdoc.api.v30.app223.apiobjects.AbstractChild;
import test.org.springdoc.api.v30.app223.apiobjects.AbstractParent;
import test.org.springdoc.api.v30.app223.apiobjects.Response;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ARestController {
	@PostMapping("/parent")
	public Response parentEndpoint(@RequestBody AbstractParent parent) {
		return null;
	}

	@PostMapping("/child")
	public Response childEndpoint(@RequestBody AbstractChild child) {
		return null;
	}
}
