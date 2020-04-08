package test.org.springdoc.api.app107;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping(path = "/entity-b", produces = { "application/json", "application/xml" })
	public EntityB getEntityB(){
		return new EntityB();
	}

	public class EntityB {

		@Schema(required = true)
		@JsonProperty("fieldB")
		private String fieldB;

		@Schema(required = true)
		@JsonProperty("entityA")
		private EntityA entityA;
		//Getters and setters...
	}

	public class EntityA {
		@Schema(required = true)
		@JsonProperty("fieldA")
		private String fieldA;
		//Getters and setters...
	}
}

