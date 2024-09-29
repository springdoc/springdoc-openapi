package test.org.springdoc.api.app188;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OpenApiCustomizer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


public class OrderDemo {

	public static class Customizer2 implements OpenApiCustomizer, GlobalOpenApiCustomizer {
		public void customise(OpenAPI openApi) {
			openApi.addTagsItem(new Tag().name("2"));
		}
	}

	public static class Customizer3 implements OpenApiCustomizer, GlobalOpenApiCustomizer {
		public void customise(OpenAPI openApi) {
			openApi.addTagsItem(new Tag().name("3"));
		}
	}
	
	public static class Customizer1 implements OpenApiCustomizer, GlobalOpenApiCustomizer {
		public void customise(OpenAPI openApi) {
			openApi.addTagsItem(new Tag().name("1"));
		}
	}

	@RestController
	public static class MyController {

		@GetMapping("/test")
		public String testingMethod() {
			return "foo";
		}
	}

}