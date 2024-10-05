package test.org.springdoc.api.v30.app228;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnasslahsen
 */
@RestController
@RequestMapping
public class HelloController {


	@PostMapping("swaggerTest")
	public String swaggerTest(@RequestBody MyRequest myRequest) {
		return null;
	}

	public class MyRequest {
		@Schema(oneOf = {Child1.class, Child2.class})
		@JsonProperty
		private Parent parent;
	}

	@JsonSubTypes({
			@JsonSubTypes.Type(value = Child1.class),
			@JsonSubTypes.Type(value = Child2.class),
			@JsonSubTypes.Type(value = Child3.class),
	})
	public abstract class Parent {
		@JsonProperty
		private String parentProperty;
	}

	public class Child1 extends Parent {
		@JsonProperty
		private String childProperty1;
	}

	public class Child2 extends Parent {
		@JsonProperty
		private String childProperty2;
	}

	public class Child3 extends Parent {
		@JsonProperty
		private String childProperty3;
	}

}