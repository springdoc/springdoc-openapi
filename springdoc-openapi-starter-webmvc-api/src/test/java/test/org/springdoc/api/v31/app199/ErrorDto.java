package test.org.springdoc.api.v31.app199;

public class ErrorDto {
    private String message;

    public ErrorDto( final String message ) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage( final String message ) { this.message = message; }
}
