package test.org.springdoc.api.app107;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Gets entity b.
	 *
	 * @return the entity b
	 */
	@GetMapping(path = "/entity-b", produces = { "application/json", "application/xml" })
	public EntityB getEntityB() {
		return new EntityB();
	}

	/**
	 * The type Entity b.
	 */
	class EntityB {

		/**
		 * The Field b.
		 */
		@Schema(required = true)
		@JsonProperty("fieldB")
		private String fieldB;

		/**
		 * The Entity a.
		 */
		@Schema(required = true)
		@JsonProperty("entityA")
		private EntityA entityA;
		//Getters and setters...
	}

	/**
	 * The type Entity a.
	 */
	class EntityA {
		/**
		 * The Field a.
		 */
		@Schema(required = true)
		@JsonProperty("fieldA")
		private String fieldA;
		//Getters and setters...
	}
}

