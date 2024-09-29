package test.org.springdoc.api.v30.app227.wrapper;


import org.springframework.http.HttpStatusCode;
import test.org.springdoc.api.v30.app227.model.Item;

public class NoGenericWrapper extends ResponseEntityWrapper<String> {
    public NoGenericWrapper(Item<String> body, HttpStatusCode status) {
        super(body, status);
    }
}
