package test.org.springdoc.api.v30.app244;

import org.springdoc.core.annotations.EnumDescription;

public class DeepModel {
    @EnumDescription
    private Status status;

    public Status getStatus() {
        return status;
    }
}
