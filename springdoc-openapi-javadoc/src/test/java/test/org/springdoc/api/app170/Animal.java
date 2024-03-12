package test.org.springdoc.api.app170;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Interface of the Animal.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
		@JsonSubTypes.Type(value = Dog.class, name = "dog"),
		@JsonSubTypes.Type(value = Cat.class, name = "cat"),
})
@Schema(description = "Represents an Animal class.")
public interface Animal {}
