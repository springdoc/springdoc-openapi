package test.org.springdoc.api.app185;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(Dog.class),
        @JsonSubTypes.Type(Cat.class)
})
public class Pet {

    public final String name;

    public Pet() {
        this.name = null;
    }

    public Pet(String name) {
        this.name = name;
    }

}
