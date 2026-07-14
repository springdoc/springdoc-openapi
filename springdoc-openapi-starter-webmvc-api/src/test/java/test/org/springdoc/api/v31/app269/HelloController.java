package test.org.springdoc.api.v31.app269;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/api/test")
	public PidLookupResponse test() {
		return null;
	}

	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public record PidLookupResponse(
			String familyName,
			String expiryDate,
			String issuanceDate,
			String issuingCountry,
			String issuingAuthority) {
	}
}
