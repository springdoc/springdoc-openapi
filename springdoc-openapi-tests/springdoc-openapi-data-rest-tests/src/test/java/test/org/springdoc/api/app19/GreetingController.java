package test.org.springdoc.api.app19;

import com.querydsl.core.types.Predicate;

import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	@GetMapping("/test2")
	public ResponseEntity<?> sayHello2(@QuerydslPredicate(root = Application.class, bindings = ApplicationPredicate.class) Predicate predicate,
			@RequestParam String test) {
		return ResponseEntity.ok().build();
	}
}