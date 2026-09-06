package org.springdoc.core.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springdoc.core.deserializers.TypeSetDeserializer;

import java.util.Set;

public interface SchemaTypeMixin {

    @JsonProperty("type")
    @JsonDeserialize(using = TypeSetDeserializer.class)
    void setTypes(Set<String> types);

}
