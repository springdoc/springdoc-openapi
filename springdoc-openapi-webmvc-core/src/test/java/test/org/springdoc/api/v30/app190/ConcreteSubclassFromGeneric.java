package test.org.springdoc.api.v30.app190;

public class ConcreteSubclassFromGeneric extends SimpleGeneric<MyData> {
    private String topName;

    public String getTopName() {
        return topName;
    }
}
