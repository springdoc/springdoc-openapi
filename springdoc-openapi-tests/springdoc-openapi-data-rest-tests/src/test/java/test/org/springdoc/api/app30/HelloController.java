package test.org.springdoc.api.app30;

import com.querydsl.core.types.Predicate;

import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

	@GetMapping("/")
	public User testQueryDslAndSpringDoc(@QuerydslPredicate(root = User.class, bindings = UserPredicate.class) Predicate predicate) {
		return null;
	}
}
