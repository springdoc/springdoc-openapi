package test.org.springdoc.api.app100;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.org.springdoc.api.AbstractSpringDocTest;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Mar, 2020
 **/

public class SpringDocApp100Test extends AbstractSpringDocTest {
    @Test
    public void testApp() throws Exception {
        mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", is("3.0.1")))
                .andExpect(jsonPath("$.paths./test.get.parameters[2].name")
                        .value("name"))
                .andExpect(jsonPath("$.paths./test.get.parameters[3].name")
                        .value("notCode"))
                .andExpect(jsonPath("$.paths./test.get.parameters[4].name")
                        .value("status"))
                .andExpect(jsonPath("$.paths./test.get.parameters[4].schema.enum").isArray());
    }

    public enum Status {
        ACTIVE, INACTIVE
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
        public ResponseEntity<?> sayHello2(@QuerydslPredicate(bindings = CountryPredicate.class, root = Country.class) Predicate predicate,
                                           @RequestParam List<Status> statuses) {
            return ResponseEntity.ok().build();
        }
    }

    public static class CountryPredicate implements QuerydslBinderCustomizer<QCountry> {

        @Override
        public void customize(QuerydslBindings querydslBindings, QCountry qCountry) {
            querydslBindings.bind(qCountry.codeISO3166).as("code").first((path, value) -> path.containsIgnoreCase(value));
            querydslBindings.bind(qCountry.dialingCode).as("postCode").first((path, value) -> path.containsIgnoreCase(value));
        }
    }
}
