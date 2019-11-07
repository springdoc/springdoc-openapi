package test.org.springdoc.api.app26;

import io.swagger.v3.oas.annotations.media.Schema;

public class MyModel {

    @Schema(description = "Hello", type = "object", oneOf = {Foo.class, Bar.class})
    private Object thing;

    public Object getThing() {
        return thing;
    }

    public void setThing(Object thing) {
        this.thing = thing;
    }
}
