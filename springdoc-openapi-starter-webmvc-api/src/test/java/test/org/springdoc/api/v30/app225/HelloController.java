package test.org.springdoc.api.v30.app225;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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

