package test.org.springdoc.api.app1;

public class ErrorMessage {

    private String id;
    private String message;

    public ErrorMessage(String id, String message2) {
        this.id = id;
        this.message = message2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
