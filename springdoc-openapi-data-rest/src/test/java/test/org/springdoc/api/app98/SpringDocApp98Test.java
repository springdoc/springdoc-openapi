package test.org.springdoc.api.app98;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import test.org.springdoc.api.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Mar, 2020
 **/

public class SpringDocApp98Test extends AbstractSpringDocTest {
    @Test
    public void testApp() throws Exception {
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1")))
                .andExpect(jsonPath("$.paths./test.get.parameters[0].schema.$ref")
                        .value("#/components/schemas/DummyEntityPredicateG"))
                .andExpect(jsonPath("$.components.schemas.DummyEntityPredicateG.properties.notCode").exists());
    }

    @SpringBootApplication
    static class SpringDocTestApp {
        @Bean
        public GreetingController greetingController() {
            return new GreetingController();
        }
    }

    @RestController
    public static class GreetingController {

        @GetMapping("/test")
        public ResponseEntity<?> sayHello2(@QuerydslPredicate(bindings = DummyEntityPredicate.class, root = DummyEntity.class) Predicate predicate) {
            return ResponseEntity.ok().build();
        }

        @GetMapping("/test-multiple")
        public ResponseEntity<?> searchDummyEntity(@QuerydslPredicate(bindings = DummyEntityPredicate.class, root = DummyEntity.class) Predicate predicate) {
            return ResponseEntity.ok().build();
        }
    }

    public static class DummyEntityPredicate implements QuerydslBinderCustomizer<QDummyEntity> {

        @Override
        public void customize(QuerydslBindings querydslBindings, QDummyEntity qDummyEntity) {
            querydslBindings.bind(qDummyEntity.code).as("notCode").first((path, value) -> path.containsIgnoreCase(value));
        }
    }
}
