package test.org.springdoc.api.v31.app225;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping
public class HelloController {

	@PostMapping("/testBoolean")
	public void HelloController(@ParameterObject RequestDto requestDto) {
	}
}

@JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
class RequestDto {
	private String personalNumber;

	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}
}

