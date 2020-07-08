package test.org.springdoc.api.app127;

public enum ConcreteType {
  TYPE_A("Type A"),
  TYPE_B("Type B");

  private final String name;

  ConcreteType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
