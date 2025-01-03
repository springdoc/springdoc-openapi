package test.org.springdoc.api.v31.app198;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class HelloController {

	@GetMapping
	public Response test() {
		return new Response(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
	}
}