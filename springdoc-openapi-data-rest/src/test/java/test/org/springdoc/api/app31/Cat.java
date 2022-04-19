package test.org.springdoc.api.app31;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class Cat extends Pet {

    private final boolean meows;

    public Cat() {
        super();
        this.meows = false;
    }

    public Cat(boolean meows, String name) {
        super(name);
        this.meows = meows;
    }

    public boolean getMeows() {
        return meows;
    }

}
