package test.org.springdoc.api.app157;

import com.fasterxml.jackson.annotation.JsonView;

public class FooBean {
    @JsonView(Views.View2.class)
    private String message;
    @JsonView(Views.View1.class)
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public FooBean(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
