package test.org.springdoc.api.app32;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class HelloController {

	@RequestMapping(value = "/filter", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public String filterPost(@RequestBody final MyTestDto filter) {
		return "OK";
	}

	class MyTestDto {
		public String object1;
		public String object2;
		public String object3;
	}

}
