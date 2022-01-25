package test.org.springdoc.api.app180;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface MapExclusion {
    @JsonIgnore
    boolean isEmpty();
}
