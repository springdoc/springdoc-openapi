package test.org.springdoc.api.app31;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class Dog extends Pet {

    private final boolean barks;

    public Dog() {
        super();
        this.barks = false;
    }

    public Dog(boolean barks, String name) {
        super(name);
        this.barks = barks;
    }

    public boolean getBarks() {
        return barks;
    }

}
