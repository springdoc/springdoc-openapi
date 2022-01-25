package test.org.springdoc.api.app5;

import java.util.List;

import com.querydsl.core.types.Predicate;
import test.org.springdoc.api.app5.Country.Status;

import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	@GetMapping("/test")
	public ResponseEntity<?> sayHello2(@QuerydslPredicate(bindings = CountryPredicate.class, root = Country.class) Predicate predicate,
			@RequestParam List<Status> statuses) {
		return ResponseEntity.ok().build();
	}
}