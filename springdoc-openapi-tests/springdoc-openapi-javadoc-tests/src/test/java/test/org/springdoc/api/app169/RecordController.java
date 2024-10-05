package test.org.springdoc.api.app169;

import org.springdoc.core.annotations.ParameterObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("record")
class RecordController {
	@GetMapping
	public SimpleOuterClass index(@ParameterObject SimpleOuterClass filter) {
		return null;
	}
}
