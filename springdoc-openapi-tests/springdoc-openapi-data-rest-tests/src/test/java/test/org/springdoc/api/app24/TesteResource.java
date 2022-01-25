package test.org.springdoc.api.app24;

import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class TesteResource {

    @GetMapping("/")
    public Page<User> testeQueryDslAndSpringDoc(@QuerydslPredicate(root = User.class, bindings = UserPredicate.class) Predicate predicate, Pageable pageable) {
        return null;
    }
}
