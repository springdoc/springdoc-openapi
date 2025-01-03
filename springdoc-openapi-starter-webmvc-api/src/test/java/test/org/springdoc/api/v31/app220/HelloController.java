package test.org.springdoc.api.v31.app220;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
    @PostMapping("/parent")
    public void parentEndpoint(@RequestBody Superclass parent) {
        
    }
	
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
		@Type(value = IntermediateClass.class, name = IntermediateClass.SCHEMA_NAME),
})
sealed class Superclass permits IntermediateClass {

	public Superclass() {}
}

@Schema(name = IntermediateClass.SCHEMA_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
		@Type(value = FirstChildClass.class, name = FirstChildClass.SCHEMA_NAME),
		@Type(value = SecondChildClass.class, name = SecondChildClass.SCHEMA_NAME)
})
sealed class IntermediateClass extends Superclass permits FirstChildClass, SecondChildClass {

	public static final String SCHEMA_NAME = "IntermediateClass";
}

@Schema(name = FirstChildClass.SCHEMA_NAME)
final class FirstChildClass extends IntermediateClass {

	public static final String SCHEMA_NAME = "Image";
}

@Schema(name = SecondChildClass.SCHEMA_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
		@Type(value = ThirdChildClass.class, name = ThirdChildClass.SCHEMA_NAME)
})
sealed class SecondChildClass extends IntermediateClass {

	public static final String SCHEMA_NAME = "Mail";
}

@Schema(name = ThirdChildClass.SCHEMA_NAME)
final class ThirdChildClass extends SecondChildClass {

	public static final String SCHEMA_NAME = "Home";
}