package test.org.springdoc.api.app177;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnnotatedController {

	@Group1
	@GetMapping("/annotated")
	public String annotatedGet() {
		return "annotated";
	}

	@Group1
	@PostMapping("/annotated")
	public String annotatedPost() {
		return "annotated";
	}

	@Group2
	@PutMapping("/annotated")
	public String annotatedPut() {
		return "annotated";
	}

	@Bean
	public GroupedOpenApi group1OpenApi() {
		return GroupedOpenApi.builder()
			.group("annotatedGroup1")
			.addMethodFilter(method -> method.isAnnotationPresent(Group1.class))
			.build();
	}

	@Bean
	public GroupedOpenApi group2OpenApi() {
		return GroupedOpenApi.builder()
			.group("annotatedGroup2")
			.addMethodFilter(method -> method.isAnnotationPresent(Group2.class))
			.build();
	}

	@Bean
	public GroupedOpenApi group3OpenApi() {
		return GroupedOpenApi.builder()
			.group("annotatedCombinedGroup")
			.addMethodFilter(method -> method.isAnnotationPresent(Group1.class) || method.isAnnotationPresent(Group2.class))
			.build();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Group1 {

	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Group2 {

	}
}
