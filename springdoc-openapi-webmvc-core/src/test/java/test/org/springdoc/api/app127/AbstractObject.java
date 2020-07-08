package test.org.springdoc.api.app127;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConcreteObjectA.class, name = "Type A")
})
public abstract class AbstractObject {

  private final ConcreteType type;
  private final String name;

  protected AbstractObject(ConcreteType type, String name) {
    this.type = type;
    this.name = name;
  }

  public ConcreteType getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
