package test.org.springdoc.api.app20;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface EntityDefinition {

    @JsonIgnore
    String getKey();

    @JsonIgnore
    String getDescription();
}
